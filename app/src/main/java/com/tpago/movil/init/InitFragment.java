package com.tpago.movil.init;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.tpago.movil.R;
import com.tpago.movil.Session;
import com.tpago.movil.User;
import com.tpago.movil.UserStore;
import com.tpago.movil.app.ActivityQualifier;
import com.tpago.movil.app.FragmentReplacer;
import com.tpago.movil.app.Permissions;
import com.tpago.movil.d.domain.InitialDataLoader;
import com.tpago.movil.d.domain.session.SessionRepo;
import com.tpago.movil.d.ui.main.DepMainActivity;
import com.tpago.movil.init.intro.IntroFragment;
import com.tpago.movil.init.unlock.UnlockFragment;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class InitFragment extends BaseInitFragment {
  private static final int REQUEST_CODE_PHONE = 0;

  private Subscription subscription = Subscriptions.unsubscribed();

  @Inject UserStore userStore;
  @Inject Session.Builder sessionBuilder;

  @Inject SessionRepo sessionRepo;
  @Inject InitialDataLoader initialDataLoader;

  @Inject @ActivityQualifier FragmentReplacer fragmentReplacer;

  @Inject LogoAnimator logoAnimator;

  private boolean werePermissionsRequested = false;

  public static InitFragment create() {
    return new InitFragment();
  }

  private void resolve() {
    if (!userStore.isSet()) {
      fragmentReplacer.begin(IntroFragment.create())
        .setTransition(FragmentReplacer.Transition.SRFO)
        .commit();
    } else if (!sessionBuilder.canBuild()) {
      fragmentReplacer.begin(UnlockFragment.create())
        .setTransition(FragmentReplacer.Transition.FIFO)
        .commit();
    } else {
      final User user = userStore.get();
      final Session session = sessionBuilder.build();
      final com.tpago.movil.d.domain.session.Session s
        = new com.tpago.movil.d.domain.session.Session(
        user.phoneNumber().getValue(),
        user.email().getValue(),
        session.getToken());
      sessionRepo.setSession(s);
      subscription = initialDataLoader.load(session)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Action1<Subscription>() {
          @Override
          public void call(Subscription subscription) {
            logoAnimator.reset();
            logoAnimator.start();
          }
        })
        .subscribe(new Action0() {
          @Override
          public void call() {
            final Activity activity = getActivity();
            activity.startActivity(DepMainActivity.getLaunchIntent(activity, session));
            activity.finish();
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "Loading initial data");
            final Activity activity = getActivity();
            new AlertDialog.Builder(activity)
              .setTitle(R.string.error_generic_title)
              .setMessage(R.string.error_generic)
              .setPositiveButton(
                R.string.error_positive_button_text,
                new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    activity.finish();
                  }
                })
              .create()
              .show();
          }
        });
    }
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all annotated dependencies.
    getInitComponent().inject(this);
  }

  @Override
  public void onResume() {
    super.onResume();
    final Context context = getContext();
    if (Permissions.checkIfGranted(context, Manifest.permission.READ_PHONE_STATE)
      && Permissions.checkIfGranted(context, Manifest.permission.READ_SMS)) {
      resolve();
    } else if (!werePermissionsRequested) {
      werePermissionsRequested = true;
      Permissions.requestPermissions(
        this,
        REQUEST_CODE_PHONE,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_SMS);
    } else {
      // TODO: Let the user know that those permissions are required.
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    if (!subscription.isUnsubscribed()) {
      subscription.unsubscribe();
    }
  }
}
