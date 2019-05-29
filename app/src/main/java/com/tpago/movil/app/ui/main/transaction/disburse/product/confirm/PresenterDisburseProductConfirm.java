package com.tpago.movil.app.ui.main.transaction.disburse.product.confirm;

import com.tpago.movil.Code;
import com.tpago.movil.api.Api;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.app.ui.Presenter;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.product.disbursable.Disbursable;
import com.tpago.movil.product.disbursable.DisbursableProduct;
import com.tpago.movil.product.disbursable.DisbursableProductHelper;
import com.tpago.movil.reactivex.DisposableUtil;
import com.tpago.movil.transaction.TransactionSummary;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.FailureData;
import com.tpago.movil.util.Money;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Rate;
import com.tpago.movil.util.Result;
import com.tpago.movil.util.StringHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author hecvasro
 */
final class PresenterDisburseProductConfirm extends Presenter<PresentationDisburseProductConfirm> {

  static Builder builder() {
    return new Builder();
  }

  private final DisbursableProduct product;
  private final Api api;
  private final DisbursableProductHelper productHelper;
  private final StringMapper stringMapper;
  private final AlertManager alertManager;
  private final TakeoverLoader takeoverLoader;

  private Disposable disposable = Disposables.disposed();

  private PresenterDisburseProductConfirm(Builder builder) {
    super(builder.presentation);

    this.product = builder.product;
    this.api = builder.api;
    this.productHelper = builder.productHelper;
    this.stringMapper = builder.stringMapper;
    this.alertManager = builder.alertManager;
    this.takeoverLoader = builder.takeoverLoader;
  }

  public String getDisbursementType(){
    return this.product.type();
  }

  private void handleError(Throwable throwable) {
    this.alertManager.showAlertForGenericFailure();
    Timber.e(throwable, "Fetching term data");
  }

  private void handleResult(Result<TransactionSummary> result) {
    if (result.isSuccessful()) {
      this.presentation.finish(result.successData());
    } else {
      final FailureData failureData = result.failureData();
      this.alertManager.builder()
        .message(failureData.description())
        .show();
    }
  }

  final void consumePin(Code pin) {
    this.disposable = this.api.confirmDisbursement(this.product, pin)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnSubscribe((d) -> this.takeoverLoader.show())
      .doFinally(this.takeoverLoader::hide)
      .subscribe(this::handleResult, this::handleError);
  }

  final void onSubmitButtonPressed() {
    final Disbursable amountData = this.product.disbursable();
    final String currency = amountData.currency()
      .value();
    final DisbursableProduct.TermData termData = this.product.termData();
    if (ObjectHelper.isNull(termData)) {
      throw new IllegalStateException("ObjectHelper.isNull(termData)");
    }
    final String text = StringHelper.builder()
      .append("Desembolsar")
      .append(' ')
      .append(Money.format(currency, termData.amount()))
      .append(' ')
      .append('a')
      .append(' ')
      .append(this.productHelper.getDestinationTypeNameAndNumber(this.product))
      .append('.')
      .toString();
    this.presentation.requestPin(text);
  }

  @Override
  public void onPresentationResumed() {
    final Disbursable amountData = this.product.disbursable();
    final DisbursableProduct.TermData termData = this.product.termData();
    if (ObjectHelper.isNull(termData)) {
      throw new IllegalStateException("ObjectHelper.isNull(termData)");
    }
    final DisbursableProduct.FeeData feeData = this.product.feeData();
    if (ObjectHelper.isNull(feeData)) {
      throw new IllegalStateException("ObjectHelper.isNull(feeData)");
    }

    final String currency = amountData.currency()
      .value();
    this.presentation.setCurrency(currency);

    this.presentation.setAmount(Money.format(termData.amount()));
    this.presentation.setTerm(feeData.term() + " Meses");
    this.presentation.setRate(Rate.format(feeData.rate()) + "% " + feeData.rateType(this.stringMapper));
    this.presentation.setInsurance(Money.format(feeData.insurance()));
    this.presentation.setBalance(Money.format(feeData.newBalance()));

    this.presentation.setFee(Money.format(feeData.fee()));
  }

  @Override
  public void onPresentationPaused() {
    DisposableUtil.dispose(this.disposable);
  }

  static final class Builder {

    private DisbursableProduct product;
    private Api api;
    private DisbursableProductHelper productHelper;
    private StringMapper stringMapper;
    private AlertManager alertManager;
    private TakeoverLoader takeoverLoader;
    private PresentationDisburseProductConfirm presentation;

    private Builder() {
    }

    final Builder product(DisbursableProduct product) {
      this.product = ObjectHelper.checkNotNull(product, "product");
      return this;
    }

    final Builder api(Api api) {
      this.api = ObjectHelper.checkNotNull(api, "api");
      return this;
    }

    final Builder productHelper(DisbursableProductHelper helper) {
      this.productHelper = ObjectHelper.checkNotNull(helper, "productHelper");
      return this;
    }

    final Builder stringMapper(StringMapper mapper) {
      this.stringMapper = ObjectHelper.checkNotNull(mapper, "stringMapper");
      return this;
    }

    final Builder alertManager(AlertManager manager) {
      this.alertManager = ObjectHelper.checkNotNull(manager, "alertManager");
      return this;
    }

    final Builder takeoverLoader(TakeoverLoader loader) {
      this.takeoverLoader = ObjectHelper.checkNotNull(loader, "takeoverLoader");
      return this;
    }

    final Builder presentation(PresentationDisburseProductConfirm presentation) {
      this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
      return this;
    }

    final PresenterDisburseProductConfirm build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("product", ObjectHelper.isNull(this.product))
        .addPropertyNameIfMissing("api", ObjectHelper.isNull(this.api))
        .addPropertyNameIfMissing("productHelper", ObjectHelper.isNull(this.productHelper))
        .addPropertyNameIfMissing("stringMapper", ObjectHelper.isNull(this.stringMapper))
        .addPropertyNameIfMissing("alertManager", ObjectHelper.isNull(this.alertManager))
        .addPropertyNameIfMissing("takeoverLoader", ObjectHelper.isNull(this.takeoverLoader))
        .addPropertyNameIfMissing("presentation", ObjectHelper.isNull(this.presentation))
        .checkNoMissingProperties();
      return new PresenterDisburseProductConfirm(this);
    }
  }
}
