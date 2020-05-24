package com.mono40.movil.app.ui.main.transaction.disburse.product.amount;

import android.net.Uri;

import com.mono40.movil.R;
import com.mono40.movil.api.Api;
import com.mono40.movil.app.StringMapper;
import com.mono40.movil.app.ui.Presenter;
import com.mono40.movil.app.ui.alert.AlertManager;
import com.mono40.movil.app.ui.loader.takeover.TakeoverLoader;
import com.mono40.movil.company.Company;
import com.mono40.movil.company.CompanyHelper;
import com.mono40.movil.product.disbursable.Disbursable;
import com.mono40.movil.product.disbursable.DisbursableProduct;
import com.mono40.movil.product.disbursable.DisbursableProductHelper;
import com.mono40.movil.reactivex.DisposableUtil;
import com.mono40.movil.util.BuilderChecker;
import com.mono40.movil.util.FailureData;
import com.mono40.movil.util.Money;
import com.mono40.movil.util.ObjectHelper;
import com.mono40.movil.util.Result;
import com.mono40.movil.util.StringHelper;
import com.mono40.movil.util.digit.Digit;

import java.math.BigDecimal;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author hecvasro
 */
final class PresenterDisburseProductAmount extends Presenter<PresentationDisburseProductAmount> {

  static Builder build() {
    return new Builder();
  }

  private final DisbursableProduct product;
  private final Money.Creator amount;
  private final Api api;
  private final StringMapper stringMapper;
  private final CompanyHelper companyHelper;
  private final DisbursableProductHelper productHelper;
  private final AlertManager alertManager;
  private final TakeoverLoader takeoverLoader;

  private Disposable disposable = Disposables.disposed();

  private PresenterDisburseProductAmount(Builder builder) {
    super(builder.presentation);

    this.product = builder.product;
    this.amount = builder.amount;
    this.api = builder.api;
    this.stringMapper = builder.stringMapper;
    this.companyHelper = builder.companyHelper;
    this.productHelper = builder.productHelper;
    this.alertManager = builder.alertManager;
    this.takeoverLoader = builder.takeoverLoader;
  }

  private void updateAmount() {
    this.presentation.setAmount(this.amount.toString());
  }

  final void onDigitButtonPressed(@Digit int digit) {
    this.amount.addDigit(digit);
    this.updateAmount();
  }

  final void onDeleteButtonPressed() {
    this.amount.removeLastDigit();
    this.updateAmount();
  }

  final void onDotButtonPressed() {
    this.amount.switchToFractional();
    this.updateAmount();
  }

  private void handleError(Throwable throwable) {
    this.alertManager.showAlertForGenericFailure();
    Timber.e(throwable, "Fetching term data");
  }

  private void handleResult(Result<DisbursableProduct.TermData> result) {
    if (result.isSuccessful()) {
      this.product.termData(result.successData());
      this.presentation.moveToNextScreen();
    } else {
      final FailureData failureData = result.failureData();
      this.alertManager.builder()
        .message(failureData.description())
        .show();
    }
  }

  final void onSubmitButtonPressed() {
    final Disbursable disbursable = this.product.disbursable();
    final BigDecimal min = disbursable.minimumAmount();
    final BigDecimal max = disbursable.maximumAmount();

    final BigDecimal a = this.amount.create();
    if (a.compareTo(min) < 0 || a.compareTo(max) > 0) {
      final String c = disbursable.currency()
        .value();
      final String m = this.stringMapper.apply(R.string.disburse_product_amount_incorrect_value);
      this.alertManager.builder()
        .message(String.format(m, Money.format(c, min), Money.format(c, max)))
        .show();
    } else {
      this.disposable = this.api.fetchDisbursementTermData(this.product, a)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe((d) -> this.takeoverLoader.show())
        .doFinally(this.takeoverLoader::hide)
        .subscribe(this::handleResult, this::handleError);
    }
  }

  @Override
  public void onPresentationResumed() {
    final Disbursable disbursable = this.product.disbursable();

    final String currency = disbursable.currency()
      .value();
    this.presentation.setCurrency(currency);

    this.presentation.setBalance(Money.format(disbursable.balance()));

    final Uri bankLogo = this.companyHelper
      .getLogoUri(this.product.bank(), Company.LogoStyle.GRAY_20);
    this.presentation.setBankLogo(bankLogo);

    final String destinationProductTypeAndNumber = StringHelper.builder()
      .append(this.stringMapper.apply(R.string.to))
      .append(' ')
      .append(this.productHelper.getDestinationTypeNameAndNumber(disbursable))
      .toString();
    this.presentation.setDestinationProductTypeAndName(destinationProductTypeAndNumber);

    this.updateAmount();
  }

  @Override
  public void onPresentationPaused() {
    DisposableUtil.dispose(this.disposable);
  }

  static final class Builder {

    private DisbursableProduct product;
    private Money.Creator amount;
    private Api api;
    private StringMapper stringMapper;
    private CompanyHelper companyHelper;
    private DisbursableProductHelper productHelper;
    private AlertManager alertManager;
    private TakeoverLoader takeoverLoader;
    private PresentationDisburseProductAmount presentation;

    private Builder() {
    }

    final Builder product(DisbursableProduct product) {
      this.product = ObjectHelper.checkNotNull(product, "product");
      return this;
    }

    final Builder amount(Money.Creator creator) {
      this.amount = ObjectHelper.checkNotNull(creator, "amount");
      return this;
    }

    final Builder api(Api api) {
      this.api = ObjectHelper.checkNotNull(api, "api");
      return this;
    }

    final Builder stringMapper(StringMapper mapper) {
      this.stringMapper = ObjectHelper.checkNotNull(mapper, "stringMapper");
      return this;
    }

    final Builder companyHelper(CompanyHelper helper) {
      this.companyHelper = ObjectHelper.checkNotNull(helper, "helper");
      return this;
    }

    final Builder productHelper(DisbursableProductHelper helper) {
      this.productHelper = ObjectHelper.checkNotNull(helper, "productHelper");
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

    final Builder presentation(PresentationDisburseProductAmount presentation) {
      this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
      return this;
    }

    final PresenterDisburseProductAmount build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("product", ObjectHelper.isNull(this.product))
        .addPropertyNameIfMissing("amount", ObjectHelper.isNull(this.amount))
        .addPropertyNameIfMissing("api", ObjectHelper.isNull(this.api))
        .addPropertyNameIfMissing("stringMapper", ObjectHelper.isNull(this.stringMapper))
        .addPropertyNameIfMissing("companyHelper", ObjectHelper.isNull(this.companyHelper))
        .addPropertyNameIfMissing("productHelper", ObjectHelper.isNull(this.productHelper))
        .addPropertyNameIfMissing("alertManager", ObjectHelper.isNull(this.alertManager))
        .addPropertyNameIfMissing("takeoverLoader", ObjectHelper.isNull(this.takeoverLoader))
        .addPropertyNameIfMissing("presentation", ObjectHelper.isNull(this.presentation))
        .checkNoMissingProperties();
      return new PresenterDisburseProductAmount(this);
    }
  }
}
