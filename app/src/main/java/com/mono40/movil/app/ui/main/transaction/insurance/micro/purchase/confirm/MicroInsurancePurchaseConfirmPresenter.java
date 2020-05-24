package com.mono40.movil.app.ui.main.transaction.insurance.micro.purchase.confirm;

import com.mono40.movil.Code;
import com.mono40.movil.api.Api;
import com.mono40.movil.app.StringMapper;
import com.mono40.movil.app.ui.Presenter;
import com.mono40.movil.app.ui.alert.AlertManager;
import com.mono40.movil.app.ui.loader.takeover.TakeoverLoader;
import com.mono40.movil.app.ui.main.transaction.insurance.micro.purchase.MicroInsurancePurchase;
import com.mono40.movil.app.ui.main.transaction.insurance.micro.purchase.MicroInsurancePurchaseComponent;
import com.mono40.movil.d.domain.Product;
import com.mono40.movil.d.domain.ProductManager;
import com.mono40.movil.insurance.micro.MicroInsurancePartner;
import com.mono40.movil.insurance.micro.MicroInsurancePlan;
import com.mono40.movil.reactivex.DisposableUtil;
import com.mono40.movil.transaction.TransactionSummary;
import com.mono40.movil.util.FailureData;
import com.mono40.movil.util.Money;
import com.mono40.movil.util.ObjectHelper;
import com.mono40.movil.util.Result;
import com.mono40.movil.util.TaxUtil;
import com.mono40.movil.util.TransactionType;

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


        this.presentation.requestPin(TaxUtil.getConfirmPinTransactionMessage(TransactionType.MICRO_INSURANCE,
                request.premium().doubleValue(), paymentMethod, planData.name(), paymentMethod.getCurrency(), "",
                stringMapper, 0, 0, request.premium().doubleValue(), request.premium().doubleValue())
        );
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
