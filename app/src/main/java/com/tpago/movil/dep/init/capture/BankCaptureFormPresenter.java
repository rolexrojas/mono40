package com.tpago.movil.dep.init.capture;

import com.tpago.movil.R;
import com.tpago.movil.api.Api;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.company.bank.BankStore;
import com.tpago.movil.dep.content.StringResolver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

final class BankCaptureFormPresenter  extends CaptureFormPresenter<BankCaptureFormPresenter.View> {
  Api api;
  BankStore bankStore;
  AlertManager alertManager;
  BankCaptureFormFragment view;

  BankCaptureFormPresenter(
      BankCaptureFormPresenter.View view,
      StringResolver stringResolver,
      CaptureData captureData,
      Api api,
      BankStore bankStore,
      AlertManager alertManager,
      BankCaptureFormFragment fragment
  ) {
    super(view, stringResolver, captureData);
    this.api = api;
    this.bankStore = bankStore;
    this.alertManager = alertManager;
    this.view = fragment;
  }

  private void updateView() {
  }

  @Override
  void onMoveToNextScreenButtonClicked() {
  }

  @Override
  public void onViewStarted() {
    super.onViewStarted();
    updateView();
  }

  public void selectedBank(Bank bank) {
    captureData.setBank(bank);
  }

  public void onContinue() {
    this.api.preRegisterInformation(
        captureData.getPhoneNumber(),
        captureData.getEmail().value(),
        captureData.getFirstName(),
        captureData.getLastName(),
        captureData.getBank())
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnSubscribe((d) -> {})
      .doFinally(() -> {})
      .subscribe((response) -> {
        if (response.isSuccessful()) {
          this.alertManager.builder()
            .title(response.successData().title())
            .message(response.successData().message())
            .positiveButtonAction(() -> view.moveToNextScreen())
            .show();
        } else {
          this.alertManager.builder()
            .title(R.string.error_generic_title)
            .message(response.failureData().description())
            .show();
        }
      }, (e) -> {
        alertManager.showAlertForGenericFailure();
      });
  }


  public void fetchBanks() {
    this.api.fetchBanks()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnSubscribe((d) -> {})
      .doFinally(() -> {})
      .subscribe((banks) -> this.bankStore.sync(banks)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe((d) -> {})
        .doFinally(() -> {})
        .subscribe(() -> view.onResume(), (e) -> alertManager.showAlertForGenericFailure()), (e) -> alertManager.showAlertForGenericFailure());
  }

  interface View extends CaptureFormPresenter.View {

  }
}
