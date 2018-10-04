package com.tpago.movil.app.ui.main.transaction.disburse.product;

import com.tpago.movil.app.StringMapper;
import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.di.ComponentBuilderSupplier;
import com.tpago.movil.app.ui.fragment.FragmentQualifier;
import com.tpago.movil.app.ui.fragment.FragmentScope;
import com.tpago.movil.product.disbursable.DisbursableProduct;
import com.tpago.movil.product.disbursable.DisbursableProductHelper;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.Money;
import com.tpago.movil.util.Number;
import com.tpago.movil.util.ObjectHelper;

import java.util.Map;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class FragmentModuleDisburseProduct {

  static Builder builder() {
    return new Builder();
  }

  private final DisbursableProduct product;
  private final Money.Creator amount = Money.creator();
  private final Number.Creator term = Number.creator(0);
  private final PresentationDisburseProduct presentation;

  private FragmentModuleDisburseProduct(Builder builder) {
    this.product = builder.product;
    this.presentation = builder.presentation;
  }

  @Provides
  @FragmentScope
  DisbursableProduct product() {
    return this.product;
  }

  @Provides
  @FragmentScope
  Money.Creator amount() {
    return this.amount;
  }

  @Provides
  @FragmentScope
  Number.Creator term() {
    return this.term;
  }

  @Provides
  @FragmentScope
  @FragmentQualifier
  ComponentBuilderSupplier componentBuilderSupplier(@FragmentQualifier Map<Class<?>, ComponentBuilder> map) {
    return ComponentBuilderSupplier.create(map);
  }

  @Provides
  @FragmentScope
  PresenterDisburseProduct presenter(
    StringMapper stringMapper,
    DisbursableProductHelper productHelper
  ) {
    return PresenterDisburseProduct.builder()
      .product(this.product)
      .stringMapper(stringMapper)
      .productHelper(productHelper)
      .presentation(this.presentation)
      .build();
  }

  static final class Builder {

    private DisbursableProduct product;
    private PresentationDisburseProduct presentation;

    private Builder() {
    }

    final Builder product(DisbursableProduct product) {
      this.product = ObjectHelper.checkNotNull(product, "product");
      return this;
    }

    final Builder presentation(PresentationDisburseProduct presentation) {
      this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
      return this;
    }

    final FragmentModuleDisburseProduct build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("product", ObjectHelper.isNull(this.product))
        .addPropertyNameIfMissing("presentation", ObjectHelper.isNull(this.presentation))
        .checkNoMissingProperties();
      return new FragmentModuleDisburseProduct(this);
    }
  }
}
