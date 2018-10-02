package com.tpago.movil.app.ui.main.transaction.insurance.micro.purchase.term;

import com.tpago.movil.R;
import com.tpago.movil.api.Api;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.app.ui.Presenter;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.app.ui.main.transaction.insurance.micro.purchase.MicroInsurancePurchase;
import com.tpago.movil.app.ui.main.transaction.insurance.micro.purchase.MicroInsurancePurchaseComponent;
import com.tpago.movil.insurance.micro.MicroInsurancePartner;
import com.tpago.movil.insurance.micro.MicroInsurancePlan;
import com.tpago.movil.reactivex.DisposableUtil;
import com.tpago.movil.util.FailureData;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Result;
import com.tpago.movil.util.StringHelper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public final class MicroInsurancePurchaseTermPresenter
  extends Presenter<MicroInsurancePurchaseTermPresentation> {

  static MicroInsurancePurchaseTermPresenter create(
    MicroInsurancePurchaseTermPresentation presentation,
    MicroInsurancePurchaseComponent component
  ) {
    return new MicroInsurancePurchaseTermPresenter(presentation, component);
  }

  @Inject Api api;

  @Inject MicroInsurancePurchase purchase;

  @Inject StringMapper stringMapper;
  @Inject AlertManager alertManager;
  @Inject TakeoverLoader takeoverLoader;

  private Disposable disposable = Disposables.disposed();

  private final int DEFAULT_TERM_SELECTED = 1;

  private MicroInsurancePurchaseTermPresenter(
    MicroInsurancePurchaseTermPresentation presentation,
    MicroInsurancePurchaseComponent component
  ) {
    super(presentation);

    // Injects all annotated dependencies.
    component.inject(this);
  }

  final void onTermSelected(int id) {
    final MicroInsurancePlan.Data planData = ObjectHelper
      .checkNotNull(this.purchase.plan(), "plan")
      .data();
    MicroInsurancePlan.Term selectedTerm = null;
    for (MicroInsurancePlan.Term term : planData.terms()) {
      if (term.id() == id) {
        selectedTerm = term;
        break;
      }
    }
    this.purchase.term(selectedTerm);
  }

  private void handleError(Throwable throwable) {
    Timber.e(throwable);

    this.alertManager.showAlertForGenericFailure();
  }

  private void handleSuccess(Result<MicroInsurancePlan.Request> result) {
    if (!result.isSuccessful()) {
      final FailureData failureData = result.failureData();
      this.alertManager.builder()
        .message(failureData.description())
        .show();
      return;
    }
    this.purchase.request(result.successData());
    this.presentation.moveToNextScreen();
  }

  final void onSubmitButtonPressed() {
    final MicroInsurancePartner partner = ObjectHelper
      .checkNotNull(this.purchase.partner(), "partner");
    final MicroInsurancePlan plan = ObjectHelper
      .checkNotNull(this.purchase.plan(), "plan");
    final MicroInsurancePlan.Term term = ObjectHelper
      .checkNotNull(this.purchase.term(), "term");

    this.disposable = this.api
      .generateMicroInsurancePurchaseRequest(partner, plan, term)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnSubscribe((d) -> this.takeoverLoader.show())
      .doFinally(this.takeoverLoader::hide)
      .subscribe(this::handleSuccess, this::handleError);
  }

  @Override
  public void onPresentationResumed() {
    final String title = StringHelper.builder()
      .append(this.stringMapper.apply(R.string.buy))
      .append(" ")
      .append(this.stringMapper.apply(R.string.main_transaction_insurance_micro))
      .toString();
    this.presentation.setTitle(title);
    final String subTitle = ObjectHelper.checkNotNull(this.purchase.plan(), "plan")
      .data()
      .name();
    this.presentation.setSubtitle(subTitle);

    final MicroInsurancePlan.Data planData = ObjectHelper
      .checkNotNull(this.purchase.plan(), "plan")
      .data();
    final List<MicroInsurancePlan.Term> terms = Observable.fromIterable(planData.terms())
      .toSortedList(MicroInsurancePlan.Term::compareTo)
      .blockingGet();
    final MicroInsurancePlan.Term term = this.purchase.term();

    int selectedTermIndex = terms.get(0).id();
    if (ObjectHelper.isNotNull(term)) {
      selectedTermIndex = term.id();
    }
    this.presentation.setTerms(terms, selectedTermIndex);
  }

  @Override
  public void onPresentationPaused() {
    DisposableUtil.dispose(this.disposable);
  }
}
