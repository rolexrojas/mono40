package com.tpago.movil.dep.init.unlock;

import android.net.Uri;

import com.tpago.movil.Password;
import com.tpago.movil.R;
import com.tpago.movil.app.ui.AlertData;
import com.tpago.movil.app.ui.AlertManager;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.data.DeviceIdSupplier;
import com.tpago.movil.data.StringMapper;
import com.tpago.movil.dep.Presenter;
import com.tpago.movil.dep.init.InitComponent;
import com.tpago.movil.reactivex.DisposableHelper;
import com.tpago.movil.session.SessionManager;
import com.tpago.movil.user.User;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Placeholder;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class UnlockPresenter extends Presenter<UnlockPresenter.View> {

  private static String sanitize(String content) {
    return ObjectHelper.firstNonNull(content, "")
      .trim();
  }

  private String passwordTextInputContent;
  private boolean isPasswordTextInputContentValid = false;

  private Disposable disposable = io.reactivex.disposables.Disposables.disposed();

  @Inject AlertManager alertManager;
  @Inject DeviceIdSupplier deviceIdSupplier;
  @Inject SessionManager sessionManager;
  @Inject StringMapper stringMapper;
  @Inject TakeoverLoader takeoverLoader;

  UnlockPresenter(View view, InitComponent component) {
    super(view);

    // Injects all the annotated dependencies.
    ObjectHelper.checkNotNull(component, "component")
      .inject(this);
  }

  private void updateView() {
    if (this.isPasswordTextInputContentValid) {
      this.view.showPasswordTextInputAsErratic(false);
    }
    this.view.showUnlockButtonAsEnabled(this.isPasswordTextInputContentValid);
  }

  private void startLoading() {
    this.view.setUnlockButtonEnabled(false);
    this.view.showUnlockButtonAsEnabled(false);

    this.takeoverLoader.show();
  }

  private void stopLoading() {
    this.takeoverLoader.hide();

    this.view.showUnlockButtonAsEnabled(this.isPasswordTextInputContentValid);
    this.view.setUnlockButtonEnabled(this.isPasswordTextInputContentValid);
  }

  final void onPasswordTextInputContentChanged(String content) {
    final String sanitizedContent = sanitize(content);
    if (!sanitizedContent.equals(this.passwordTextInputContent)) {
      this.passwordTextInputContent = sanitizedContent;
      this.isPasswordTextInputContentValid = Password.isValid(this.passwordTextInputContent);

      this.updateView();
    }
  }

  final void handleSuccess(com.tpago.movil.util.Result<Placeholder> result) {
    if (result.isSuccessful()) {
      this.view.moveToInitScreen();
    } else {
      final com.tpago.movil.util.FailureData failureData = result.failureData();

      final AlertData data = AlertData.builder(this.stringMapper)
        .message(failureData.description())
        .build();
      this.alertManager.show(data);
    }
  }

  private void handleError(Throwable throwable) {
    Timber.e(throwable, "Opening session with password");

    this.alertManager.show(AlertData.createForGenericFailure(this.stringMapper));
  }

  final void onUnlockButtonClicked() {
    if (this.isPasswordTextInputContentValid) {
      final Password password = Password.create(this.passwordTextInputContent);
      final String deviceId = this.deviceIdSupplier.get();

      this.disposable = this.sessionManager.openSession(password, deviceId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe((disposable) -> this.startLoading())
        .doFinally(this::stopLoading)
        .subscribe(this::handleSuccess, this::handleError);
    } else {
      this.view.showPasswordTextInputAsErratic(true);

      final AlertData data = AlertData.builder(this.stringMapper)
        .title(R.string.incorrectCredentials)
        .message(R.string.bothUsernameAndPasswordAreRequired)
        .build();
      this.alertManager.show(data);
    }
  }

  @Override
  public void onViewStarted() {
    super.onViewStarted();

    final User user = this.sessionManager.getUser();

    this.view.setUserPictureUri(user.pictureUri());
    this.view.setUserFirstName(user.firstName());

    this.view.setPasswordTextInputContent(this.passwordTextInputContent);

    this.updateView();
  }

  @Override
  public void onViewStopped() {
    DisposableHelper.dispose(this.disposable);

    super.onViewStopped();
  }

  interface View extends Presenter.View {

    void setUserPictureUri(Uri pictureUri);

    void setUserFirstName(String firstName);

    void setPasswordTextInputContent(String content);

    void showPasswordTextInputAsErratic(boolean showAsErratic);

    void setUnlockButtonEnabled(boolean enabled);

    void showUnlockButtonAsEnabled(boolean showAsEnabled);

    void moveToInitScreen();
  }
}
