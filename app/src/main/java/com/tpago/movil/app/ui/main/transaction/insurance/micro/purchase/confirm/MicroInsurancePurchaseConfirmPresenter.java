package com.tpago.movil.app.ui.main.transaction.insurance.micro.purchase.confirm;

import com.tpago.movil.Code;
import com.tpago.movil.R;
import com.tpago.movil.api.Api;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.app.ui.Presenter;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.app.ui.main.transaction.insurance.micro.purchase.MicroInsurancePurchase;
import com.tpago.movil.app.ui.main.transaction.insurance.micro.purchase.MicroInsurancePurchaseComponent;
import com.tpago.movil.d.data.Formatter;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.insurance.micro.MicroInsurancePartner;
import com.tpago.movil.insurance.micro.MicroInsurancePlan;
import com.tpago.movil.reactivex.DisposableUtil;
import com.tpago.movil.transaction.TransactionSummary;
import com.tpago.movil.util.FailureData;
import com.tpago.movil.util.Money;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Result;

import java.math.BigDecimal;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public final class MicroInsurancePurchaseConfirmPresenter
        extends Presenter<MicroInsurancePurchaseConfirmPresentation> {

    static MicroInsurancePurchaseConfirmPresenter create(
            MicroInsurancePurchaseConfirmPresentation presentation,
            MicroInsurancePurchaseComponent component
    ) {
        return new MicroInsurancePurchaseConfirmPresenter(presentation, component);
    }

    @Inject
    MicroInsurancePurchase purchase;

    @Inject
    ProductManager productManager;

    @Inject
    Api api;

    @Inject
    StringMapper stringMapper;
    @Inject
    AlertManager alertManager;
    @Inject
    TakeoverLoader takeoverLoader;

    private Product paymentMethod;

    private Disposable disposable = Disposables.disposed();

    private MicroInsurancePurchaseConfirmPresenter(
            MicroInsurancePurchaseConfirmPresentation presentation,
            MicroInsurancePurchaseComponent component
    ) {
        super(presentation);

        // Injects all annotated dependencies
        component.inject(this);
    }

    private void handleError(Throwable throwable) {
        Timber.e(throwable);

        this.alertManager.showAlertForGenericFailure();
    }

    private void handleSuccess(Result<TransactionSummary> result) {
        if (!result.isSuccessful()) {
            final FailureData failureData = result.failureData();
            this.alertManager.builder()
                    .message(failureData.description())
                    .show();
            return;
        }
        this.presentation.finish(result.successData());
    }

    final void onPaymentMethodSelected(Product paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    final void onSubmitButtonPressed() {
        final MicroInsurancePlan.Data planData = ObjectHelper
                .checkNotNull(this.purchase.plan(), "plan")
                .data();
        final MicroInsurancePlan.Request request = ObjectHelper
                .checkNotNull(this.purchase.request(), "request");

        final double taxPercentage = 0.15;
        double taxAmount = request.premium().doubleValue() * (taxPercentage / 100);
        String taxAmountText = Formatter.amount("RD", new BigDecimal(taxAmount));

        this.presentation.requestPin(String.format(
                this.stringMapper.apply(R.string.main_transaction_insurance_micro_confirm_message),
                planData.name(),
                Money.format(planData.currency(), request.premium()),
                taxPercentage + "%",
                taxAmountText
        ));
    }

    final void onPinInputted(Code pin) {
        final MicroInsurancePartner partner = this.purchase.partner();
        final MicroInsurancePlan plan = ObjectHelper
                .checkNotNull(this.purchase.plan(), "plan");
        final MicroInsurancePlan.Term term = ObjectHelper
                .checkNotNull(this.purchase.term(), "term");
        final MicroInsurancePlan.Request request = ObjectHelper
                .checkNotNull(this.purchase.request(), "request");

        this.disposable = this.api
                .confirmMicroInsurancePurchase(partner, plan, term, request, this.paymentMethod, pin)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> this.takeoverLoader.show())
                .doFinally(this.takeoverLoader::hide)
                .subscribe(this::handleSuccess, this::handleError);
    }

    @Override
    public void onPresentationResumed() {
        final MicroInsurancePlan.Data planData = ObjectHelper
                .checkNotNull(this.purchase.plan(), "plan")
                .data();
        final MicroInsurancePlan.Term term = ObjectHelper
                .checkNotNull(this.purchase.term(), "term");
        final MicroInsurancePlan.Request request = ObjectHelper
                .checkNotNull(this.purchase.request(), "request");

        this.presentation.setCurrency(planData.currency());
        this.presentation.setCoverage(Money.format(request.coverage()));
        this.presentation.setPremium(Money.format(request.premium()));
        this.presentation.setTerm(this.stringMapper.apply(term.stringId()));
        this.presentation.setTotal(Money.format(request.premium()));
        this.presentation.setPaymentMethods(this.productManager.getPaymentOptionList());
    }

    @Override
    public void onPresentationPaused() {
        DisposableUtil.dispose(this.disposable);
    }
}
