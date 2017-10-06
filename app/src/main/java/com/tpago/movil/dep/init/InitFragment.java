package com.tpago.movil.dep.init;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tpago.movil.app.ui.AlertData;
import com.tpago.movil.app.ui.AlertManager;
import com.tpago.movil.app.ui.init.unlock.UnlockFragment;
import com.tpago.movil.app.ui.ActivityQualifier;
import com.tpago.movil.app.ui.FragmentReplacer;
import com.tpago.movil.data.StringMapper;
import com.tpago.movil.dep.Permissions;
import com.tpago.movil.d.domain.InitialDataLoader;
import com.tpago.movil.d.ui.main.DepMainActivity;
import com.tpago.movil.dep.init.intro.IntroFragment;
import com.tpago.movil.session.SessionManager;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class InitFragment extends BaseInitFragment {

  private static final int REQUEST_CODE_PHONE = 0;

  private Subscription subscription = Subscriptions.unsubscribed();

  @Inject SessionManager sessionManager;

  @Inject StringMapper stringMapper;
  @Inject AlertManager alertManager;
  @Inject @ActivityQualifier FragmentReplacer fragmentReplacer;

  @Inject LogoAnimator logoAnimator;
  @Inject InitialDataLoader initialDataLoader;

  private boolean werePermissionsRequested = false;

  public static InitFragment create() {
    return new InitFragment();
  }

  private void resetAndStartLogoAnimator() {
    this.logoAnimator.reset();
    this.logoAnimator.start();
  }

  private void handleSuccess() {
    final Activity activity = this.getActivity();
    activity.startActivity(DepMainActivity.getLaunchIntent(activity));
    activity.finish();
  }

  private void handleError(Throwable throwable) {
    Timber.e(throwable, "Loading initial data");
    this.alertManager.show(AlertData.createForGenericFailure(this.stringMapper));
  }

  private void resolve() {
    if (!this.sessionManager.isUserSet()) {
      this.fragmentReplacer.begin(IntroFragment.create())
        .transition(FragmentReplacer.Transition.SRFO)
        .commit();
    } else if (!this.sessionManager.isSessionOpen()) {
      this.fragmentReplacer.begin(UnlockFragment.create())
        .transition(FragmentReplacer.Transition.FIFO)
        .commit();
    } else {
      this.subscription = this.initialDataLoader.load()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe((s) -> this.resetAndStartLogoAnimator())
        .subscribe(this::handleSuccess, this::handleError);
    }
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Injects all annotated dependencies.
    getInitComponent()
      .inject(this);
  }

  @Override
  public void onResume() {
    super.onResume();

    final Context context = getContext();
    if (Permissions.checkIfGranted(context, Manifest.permission.READ_PHONE_STATE)
      && Permissions.checkIfGranted(context, Manifest.permission.READ_SMS)) {
      this.resolve();
    } else if (!werePermissionsRequested) {
      werePermissionsRequested = true;
      Permissions.requestPermissions(
        this,
        REQUEST_CODE_PHONE,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_SMS
      );
    } else {
      // TODO: Let the user know that those permissions are required.
    }
  }

  @Override
  public void onStop() {
    if (!subscription.isUnsubscribed()) {
      subscription.unsubscribe();
    }

    super.onStop();
  }
}
