package com.mono40.movil.app.ui.main.transaction.disburse.product.term;

import com.mono40.movil.R;
import com.mono40.movil.api.Api;
import com.mono40.movil.app.StringMapper;
import com.mono40.movil.app.ui.Presenter;
import com.mono40.movil.app.ui.alert.AlertManager;
import com.mono40.movil.app.ui.loader.takeover.TakeoverLoader;
import com.mono40.movil.product.disbursable.DisbursableProduct;
import com.mono40.movil.reactivex.DisposableUtil;
import com.mono40.movil.util.BuilderChecker;
import com.mono40.movil.util.FailureData;
import com.mono40.movil.util.Number;
import com.mono40.movil.util.ObjectHelper;
import com.mono40.movil.util.Result;
import com.mono40.movil.util.digit.Digit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author hecvasro
 */
final class PresenterDisburseProductTerm extends Presenter<PresentationDisburseProductTerm> {

  static Builder build() {
    return new Builder();
  }

  private final DisbursableProduct product;
  private final Number.Creator term;
  private final Api api;
  private final StringMapper stringMapper;
  private final AlertManager alertManager;
  private final TakeoverLoader takeoverLoader;

  private Disposable disposable = Disposables.disposed();

  private PresenterDisburseProductTerm(Builder builder) {
    super(builder.presentation);

    this.product = builder.product;
    this.term = builder.term;
    this.api = builder.api;
    this.stringMapper = builder.stringMapper;
    this.alertManager = builder.alertManager;
    this.takeoverLoader = builder.takeoverLoader;
  }

  private void updateTerm() {
    this.presentation.setTerm(this.term.toString());
  }

  final void onDigitButtonPressed(@Digit int digit) {
    this.term.addDigit(digit);
    this.updateTerm();
  }

  final void onDeleteButtonPressed() {
    this.term.removeLastDigit();
    this.updateTerm();
  }

  private void handleError(Throwable throwable) {
    this.alertManager.showAlertForGenericFailure();
    Timber.e(throwable, "Fetching term data");
  }

  private void handleResult(Result<DisbursableProduct.FeeData> result) {
    if (result.isSuccessful()) {
      this.product.feeData(result.successData());
      this.presentation.moveToNextScreen();
    } else {
      final FailureData failureData = result.failureData();
      this.alertManager.builder()
        .message(failureData.description())
        .show();
    }
  }

  final void onSubmitButtonPressed() {
    final DisbursableProduct.TermData data = this.product.termData();
    if (ObjectHelper.isNull(data)) {
      throw new IllegalStateException("ObjectHelper.isNull(this.product.termData())");
    }
    final int min = data.minimumTerm();
    final int max = data.maximumTerm();

    final int t = this.term.create();
    if (t < min || t > max) {
      final String m = this.stringMapper.apply(R.string.disburse_product_term_incorrect_value);
      this.alertManager.builder()
        .message(String.format(m, min, max))
        .show();
    } else {
      this.disposable = this.api.fetchDisbursementFeeData(this.product, t)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe((d) -> this.takeoverLoader.show())
        .doFinally(this.takeoverLoader::hide)
        .subscribe(this::handleResult, this::handleError);
    }
  }

  @Override
  public void onPresentationResumed() {
    final DisbursableProduct.TermData data = this.product.termData();
    if (ObjectHelper.isNull(data)) {
      throw new IllegalStateException("ObjectHelper.isNull(this.product.termData())");
    }
    final int min = data.minimumTerm();
    final int max = data.maximumTerm();
    final String text = this.stringMapper.apply(R.string.disburse_product_term_description);
    this.presentation.setDescription(String.format(text, min, max));

    this.updateTerm();
  }

  @Override
  public void onPresentationPaused() {
    DisposableUtil.dispose(this.disposable);
  }

  static final class Builder {

    private DisbursableProduct product;
    private Number.Creator term;
    private Api api;
    private StringMapper stringMapper;
    private AlertManager alertManager;
    private TakeoverLoader takeoverLoader;
    private PresentationDisburseProductTerm presentation;

    private Builder() {
    }

    final Builder product(DisbursableProduct product) {
      this.product = ObjectHelper.checkNotNull(product, "product");
      return this;
    }

    final Builder term(Number.Creator creator) {
      this.term = ObjectHelper.checkNotNull(creator, "amount");
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

    final Builder alertManager(AlertManager manager) {
      this.alertManager = ObjectHelper.checkNotNull(manager, "alertManager");
      return this;
    }

    final Builder takeoverLoader(TakeoverLoader loader) {
      this.takeoverLoader = ObjectHelper.checkNotNull(loader, "takeoverLoader");
      return this;
    }

    final Builder presentation(PresentationDisburseProductTerm presentation) {
      this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
      return this;
    }

    final PresenterDisburseProductTerm build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("product", ObjectHelper.isNull(this.product))
        .addPropertyNameIfMissing("term", ObjectHelper.isNull(this.term))
        .addPropertyNameIfMissing("api", ObjectHelper.isNull(this.api))
        .addPropertyNameIfMissing("stringMapper", ObjectHelper.isNull(this.stringMapper))
        .addPropertyNameIfMissing("alertManager", ObjectHelper.isNull(this.alertManager))
        .addPropertyNameIfMissing("takeoverLoader", ObjectHelper.isNull(this.takeoverLoader))
        .addPropertyNameIfMissing("presentation", ObjectHelper.isNull(this.presentation))
        .checkNoMissingProperties();
      return new PresenterDisburseProductTerm(this);
    }
  }
}
