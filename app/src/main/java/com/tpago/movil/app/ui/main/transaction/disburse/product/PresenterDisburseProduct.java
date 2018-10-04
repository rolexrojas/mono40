package com.tpago.movil.app.ui.main.transaction.disburse.product;

import com.tpago.movil.R;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.app.ui.Presenter;
import com.tpago.movil.product.disbursable.DisbursableProduct;
import com.tpago.movil.product.disbursable.DisbursableProductHelper;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

/**
 * @author hecvasro
 */
final class PresenterDisburseProduct extends Presenter<PresentationDisburseProduct> {

  static Builder builder() {
    return new Builder();
  }

  private final DisbursableProduct product;
  private final StringMapper stringMapper;
  private final DisbursableProductHelper productHelper;

  private PresenterDisburseProduct(Builder builder) {
    super(builder.presentation);

    this.product = builder.product;
    this.stringMapper = builder.stringMapper;
    this.productHelper = builder.productHelper;
  }

  @Override
  public void onPresentationResumed() {
    final String subtitle = StringHelper.builder()
      .append(this.stringMapper.apply(R.string.from))
      .append(' ')
      .append(this.productHelper.getTypeNameAndNumber(this.product))
      .toString();
    this.presentation.setProductTypeAndNumber(subtitle);
  }

  static final class Builder {

    private DisbursableProduct product;
    private StringMapper stringMapper;
    private DisbursableProductHelper productHelper;
    private PresentationDisburseProduct presentation;

    private Builder() {
    }

    final Builder product(DisbursableProduct product) {
      this.product = ObjectHelper.checkNotNull(product, "product");
      return this;
    }

    final Builder stringMapper(StringMapper mapper) {
      this.stringMapper = ObjectHelper.checkNotNull(mapper, "stringMapper");
      return this;
    }

    final Builder productHelper(DisbursableProductHelper helper) {
      this.productHelper = ObjectHelper.checkNotNull(helper, "productHelper");
      return this;
    }

    final Builder presentation(PresentationDisburseProduct presentation) {
      this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
      return this;
    }

    final PresenterDisburseProduct build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("product", ObjectHelper.isNull(this.product))
        .addPropertyNameIfMissing("stringMapper", ObjectHelper.isNull(this.stringMapper))
        .addPropertyNameIfMissing("productHelper", ObjectHelper.isNull(this.productHelper))
        .addPropertyNameIfMissing("presentation", ObjectHelper.isNull(this.presentation))
        .checkNoMissingProperties();
      return new PresenterDisburseProduct(this);
    }
  }
}
