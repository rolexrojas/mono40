package com.tpago.movil.dep.init.register;

import com.tpago.movil.Code;
import com.tpago.movil.Email;
import com.tpago.movil.Password;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.app.ui.AlertData;
import com.tpago.movil.app.ui.AlertManager;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.data.DeviceIdSupplier;
import com.tpago.movil.data.StringMapper;
import com.tpago.movil.reactivex.DisposableHelper;
import com.tpago.movil.session.SessionManager;
import com.tpago.movil.session.User;
import com.tpago.movil.util.Digit;
import com.tpago.movil.dep.Presenter;
import com.tpago.movil.dep.reactivex.Disposables;
import com.tpago.movil.util.DigitValueCreator;
import com.tpago.movil.util.ObjectHelper;

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

  private void handleSuccess(com.tpago.movil.util.Result<User> result) {
    if (result.isSuccessful()) {
      this.view.moveToNextScreen();
    } else {
      final com.tpago.movil.util.FailureData failureData = result.failureData();

      final AlertData data = AlertData.builder(this.stringMapper)
        .message(failureData.description())
        .build();
      this.alertManager.show(data);
    }
  }

  private void handleError(Throwable throwable) {
    Timber.e(throwable, "Signing up");

    this.alertManager.show(AlertData.createForGenericFailure(this.stringMapper));
  }

  private void updateView() {
    this.view.setTextInputContent(this.pinCreator.toString());

    if (this.pinCreator.canCreate()) {
      final PhoneNumber phoneNumber = this.registerData.getPhoneNumber();
      final Email email = this.registerData.getEmail();
      final String firstName = this.registerData.getFirstName();
      final String lastName = this.registerData.getLastName();
      final Password password = Password.create(this.registerData.getPassword());
      final Code pin = this.pinCreator.create();
      final String deviceId = this.deviceIdSupplier.get();

      this.disposable = this.sessionManager.createSession(
        phoneNumber,
        email,
        firstName,
        lastName,
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
    DisposableHelper.dispose(this.disposable);

    super.onViewStopped();
  }

  interface View extends Presenter.View {

    void setTextInputContent(String content);

    void moveToNextScreen();
  }
}
