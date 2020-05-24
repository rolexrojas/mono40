package com.mono40.movil.dep.init.register;

import com.mono40.movil.Code;
import com.mono40.movil.Email;
import com.mono40.movil.lib.Password;
import com.mono40.movil.PhoneNumber;
import com.mono40.movil.app.ui.alert.AlertManager;
import com.mono40.movil.app.ui.loader.takeover.TakeoverLoader;
import com.mono40.movil.data.DeviceIdSupplier;
import com.mono40.movil.app.StringMapper;
import com.mono40.movil.reactivex.DisposableUtil;
import com.mono40.movil.session.SessionManager;
import com.mono40.movil.session.User;
import com.mono40.movil.util.digit.Digit;
import com.mono40.movil.dep.Presenter;
import com.mono40.movil.dep.reactivex.Disposables;
import com.mono40.movil.util.digit.DigitValueCreator;
import com.mono40.movil.util.ObjectHelper;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class PinRegisterFormPresenter extends Presenter<PinRegisterFormPresenter.View> {

  @Inject AlertManager alertManager;
  @Inject DeviceIdSupplier deviceIdSupplier;
  @Inject RegisterData registerData;
  @Inject SessionManager sessionManager;
  @Inject StringMapper stringMapper;
  @Inject TakeoverLoader takeoverLoader;

  private DigitValueCreator<Code> pinCreator;

  private Disposable disposable = Disposables.disposed();

  PinRegisterFormPresenter(View view, RegisterComponent component) {
    super(view);

    // Injects all annotated dependencies.
    ObjectHelper.checkNotNull(component, "component")
      .inject(this);
  }

  private void handleSuccess(com.mono40.movil.util.Result<User> result) {
    if (result.isSuccessful()) {
      this.registerData.setSubmitted(true);

      this.view.moveToNextScreen();
    } else {
      this.registerData.setSubmitted(false);

      final com.mono40.movil.util.FailureData failureData = result.failureData();
      this.alertManager.builder()
        .message(failureData.description())
        .show();
    }
  }

  private void handleError(Throwable throwable) {
    Timber.e(throwable, "Signing up");
    this.registerData.setSubmitted(false);
    this.alertManager.showAlertForGenericFailure();
  }

  private void updateView() {
    this.view.setTextInputContent(this.pinCreator.toString());

    if (this.pinCreator.canCreate()) {
      final PhoneNumber phoneNumber = this.registerData.getPhoneNumber();
      final Email email = this.registerData.getEmail();
      final String firstName = this.registerData.getFirstName();
      final String lastName = this.registerData.getLastName();
      final File picture = this.registerData.getPicture();
      final Password password = Password.create(this.registerData.getPassword());
      final Code pin = this.pinCreator.create();
      final String deviceId = this.deviceIdSupplier.get();

      this.disposable = this.sessionManager.createSession(
        phoneNumber,
        email,
        firstName,
        lastName,
        picture,
        password,
        pin,
        deviceId
      )
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe((d) -> this.takeoverLoader.show())
        .doFinally(this.takeoverLoader::hide)
        .subscribe(this::handleSuccess, this::handleError);
    }
  }

  final void onDigitButtonClicked(@Digit int digit) {
    this.pinCreator.addDigit(digit);
    this.updateView();
  }

  final void onDeleteButtonClicked() {
    this.pinCreator.removeLastDigit();
    this.updateView();
  }

  @Override
  public void onViewStarted() {
    super.onViewStarted();

    this.pinCreator = Code.creator();

    this.updateView();
  }

  @Override
  public void onViewStopped() {
    DisposableUtil.dispose(this.disposable);

    super.onViewStopped();
  }

  interface View extends Presenter.View {

    void setTextInputContent(String content);

    void moveToNextScreen();
  }
}
