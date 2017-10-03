package com.tpago.movil.app.ui.main.settings.profile;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tpago.movil.R;
import com.tpago.movil.app.ui.AlertData;
import com.tpago.movil.app.ui.AlertManager;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.app.ui.main.BaseMainFragment;
import com.tpago.movil.data.picasso.CircleTransformation;
import com.tpago.movil.dep.UserStore;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.RecipientManager;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.domain.pos.PosResult;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.ui.main.DepMainActivity;
import com.tpago.movil.data.StringMapper;
import com.tpago.movil.dep.init.InitActivity;
import com.tpago.movil.dep.widget.TextInput;
import com.tpago.movil.domain.auth.alt.AltAuthMethodManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class ProfileFragment extends BaseMainFragment implements ProfilePresentation {

  public static ProfileFragment create() {
    return new ProfileFragment();
  }

  private Subscription signOutSubscription = Subscriptions.unsubscribed();

  @BindView(R.id.pictureImageView) ImageView pictureImageView;
  @BindView(R.id.firstNameTextInput) TextInput firstNameTextInput;
  @BindView(R.id.lastNameTextInput) TextInput lastNameTextInput;
  @BindView(R.id.phoneNumberTextInput) TextInput phoneNumberTextInput;
  @BindView(R.id.emailTextInput) TextInput emailTextInput;

  @Inject AlertManager alertManager;
  @Inject AltAuthMethodManager altAuthMethodManager;
  @Inject PosBridge posBridge;
  @Inject ProductManager productManager;
  @Inject ProfilePresenter presenter;
  @Inject RecipientManager recipientManager;
  @Inject SessionManager sessionManager;
  @Inject StringMapper stringMapper;
  @Inject TakeoverLoader takeoverLoader;
  @Inject UserStore userStore;

  @StringRes
  @Override
  protected int titleResId() {
    return R.string.profile;
  }

  @Override
  @LayoutRes
  protected int layoutResId() {
    return R.layout.profile;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Injects all annotated dependencies.
    DepMainActivity.get(this.getActivity())
      .getComponent()
      .create(ProfileModule.create(this))
      .inject(this);
  }

  @Override
  public void onResume() {
    super.onResume();

    this.presenter.onPresentationResumed();
  }

  @Override
  public void onPause() {
    if (!this.signOutSubscription.isUnsubscribed()) {
      this.signOutSubscription.unsubscribe();
    }

    this.presenter.onPresentationPaused();

    super.onPause();
  }

  private void handleSignOutSuccess(PosResult result) {
    if (result.isSuccessful()) {
      this.recipientManager.clear();
      this.productManager.clear();
      this.sessionManager.deactivate();
      this.userStore.clear();

      final Activity activity = this.getActivity();
      activity.startActivity(InitActivity.getLaunchIntent(activity));
      activity.finish();
    } else {
      this.alertManager.show(AlertData.createForGenericFailure(this.stringMapper));
    }
  }

  private void handleSignOutFailure(Throwable throwable) {
    Timber.e(throwable, "Signing out");

    this.alertManager.show(AlertData.createForGenericFailure(this.stringMapper));
  }

  @OnClick(R.id.signOutSettingsOption)
  final void onSignOutSettingsOptionClicked() {
    if (this.signOutSubscription.isUnsubscribed()) {
      final String phoneNumber = this.userStore.get()
        .phoneNumber()
        .value();
      this.signOutSubscription = this.posBridge.unregister(phoneNumber)
        .subscribeOn(Schedulers.io())
        .doOnSuccess((result) -> {
          if (result.isSuccessful()) {
            this.altAuthMethodManager.disable()
              .blockingAwait();
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(this.takeoverLoader::show)
        .doOnUnsubscribe(this.takeoverLoader::hide)
        .subscribe(this::handleSignOutSuccess, this::handleSignOutFailure);
    }
  }

  @Override
  public void setProfilePictureUri(String uri) {
    Picasso.with(this.getContext())
      .load(uri)
      .resizeDimen(R.dimen.largeProfilePictureSize, R.dimen.largeProfilePictureSize)
      .transform(new CircleTransformation())
      .into(this.pictureImageView);
  }

  @Override
  public void setFirstNameTextInputContent(String content) {
    this.firstNameTextInput.setText(content);
  }

  @Override
  public void setLastNameTextInputContent(String content) {
    this.lastNameTextInput.setText(content);
  }

  @Override
  public void setPhoneNumberTextInputContent(String content) {
    this.phoneNumberTextInput.setText(content);
  }

  @Override
  public void setEmailTextInputContent(String content) {
    this.emailTextInput.setText(content);
  }
}
