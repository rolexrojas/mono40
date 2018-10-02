package com.tpago.movil.dep.init;

import com.tpago.movil.api.Api;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.reactivex.DisposableUtil;
import com.tpago.movil.util.Result;
import com.tpago.movil.util.StringHelper;
import com.tpago.movil.util.digit.Digit;
import com.tpago.movil.util.digit.DigitUtil;
import com.tpago.movil.dep.Presenter;
import com.tpago.movil.dep.reactivex.Disposables;
import com.tpago.movil.util.ObjectHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public final class OneTimePasswordPresenter extends Presenter<OneTimePasswordPresenter.View> {

  @Inject AlertManager alertManager;
  @Inject Api api;
  @Inject StringMapper stringMapper;
  @Inject TakeoverLoader takeoverLoader;

  private boolean isOneTimePasswordValid = false;
  private List<Integer> oneTimePasswordDigits = new ArrayList<>();

  private Disposable disposable = Disposables.disposed();

  OneTimePasswordPresenter(View view, InitComponent component) {
    super(view);
    // Injects all the annotated dependencies.
    ObjectHelper.checkNotNull(component, "component")
      .inject(this);
  }

  private void updateView() {
    final String oneTimePassword = DigitUtil.toDigitString(this.oneTimePasswordDigits);
    this.isOneTimePasswordValid = oneTimePassword.length() == 6;
    if (ObjectHelper.isNotNull(this.view)) {
      this.view.setTextInputContent(oneTimePassword);
      if(isOneTimePasswordValid){
        this.verifyActivationCode(this.view.getMsisdn(), oneTimePassword);
      }
    }
  }

  private void showTakeoverLoader(Disposable disposable) {
    this.takeoverLoader.show();
  }

  private void hideTakeoverLoader() {
    this.takeoverLoader.hide();
  }

  final void addDigit(@Digit int digit) {
    if (oneTimePasswordDigits.size() < 6) {
      oneTimePasswordDigits.add(digit);
      updateView();
    }
  }

  public void setValue(String value){
    String otpValue = DigitUtil.removeNonDigits(value);
    oneTimePasswordDigits.addAll(DigitUtil.toDigitList(otpValue));
    updateView();
  }

  final void removeDigit() {
    if (!oneTimePasswordDigits.isEmpty()) {
      oneTimePasswordDigits.remove(oneTimePasswordDigits.size() - 1);
      updateView();
    }
  }

  private void handleSuccess(Result result) {
    if(result.isSuccessful()){
      if(this.view.getShouldMoveToSignUpScreen()){
        this.view.moveToSignUpScreen();
      }else{
        this.view.moveToSignInScreen();
      }
    }else{

      oneTimePasswordDigits = new ArrayList<>();
      updateView();

      if(StringHelper.isNullOrEmpty(result.failureData().description())){
        this.alertManager.showAlertForGenericFailure();
      }else{
        this.alertManager.builder()
          .message(result.failureData().description())
          .show();
      }
    }
  }

  private void handleError(Throwable throwable) {
    Timber.e(throwable, "Fetching activation code");
    this.alertManager.showAlertForGenericFailure();
  }

  public void requestActivationCode(String msisdn){
    this.disposable = this.api.requestOneTimePasswordActivationCode(msisdn)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnSubscribe(this::showTakeoverLoader)
      .doFinally(this::hideTakeoverLoader)
      .subscribe((s) -> {}, this::handleError);
  }

  public void verifyActivationCode(String msisdn, String activationCode){
    this.disposable = this.api.verifyOneTimePasswordActivationCode(msisdn, activationCode)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnSubscribe(this::showTakeoverLoader)
      .doFinally(this::hideTakeoverLoader)
      .subscribe((s) -> handleSuccess(s), this::handleError);
  }

  @Override
  public void onViewStarted() { this.updateView(); }

  @Override
  public void onViewStopped() {
    DisposableUtil.dispose(this.disposable);
  }

  interface View extends Presenter.View {

    void setTextInputContent(String text);

    void moveToSignInScreen();

    void moveToSignUpScreen();

    String getMsisdn();

    boolean getShouldMoveToSignUpScreen();
  }
}
