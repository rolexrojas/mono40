package com.tpago.movil.app.ui.main.transaction.disburse.index;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.main.transaction.item.IndexItem;
import com.tpago.movil.company.Company;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.product.disbursable.Disbursable;
import com.tpago.movil.product.disbursable.DisbursableProduct;
import com.tpago.movil.product.disbursable.DisbursableProductHelper;
import com.tpago.movil.product.disbursable.DisbursableProductStore;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.function.Consumer;

import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * @author hecvasro
 */
final class DisburseItemsSupplierProducts implements DisburseItemsSupplier {

  static Builder builder() {
    return new Builder();
  }

  private final StringMapper stringMapper;
  private final CompanyHelper companyHelper;
  private final DisbursableProductHelper productHelper;

  private final DisbursableProductStore productStore;
  private final Consumer<DisbursableProduct> actionConsumer;

  private DisburseItemsSupplierProducts(Builder builder) {
    this.stringMapper = builder.stringMapper;
    this.companyHelper = builder.companyHelper;
    this.productHelper = builder.productHelper;

    this.productStore = builder.productStore;
    this.actionConsumer = builder.actionConsumer;
  }

  private IndexItem toItem(DisbursableProduct product) {
    final Bank bank = product.bank();
    final Disbursable disbursable = product.disbursable();
    return IndexItem.builder()
      .pictureUri(this.companyHelper.getLogoUri(bank, Company.LogoStyle.GRAY_36))
      .titleText(this.productHelper.getTypeName(disbursable))
      .subtitleText(bank.name())
      .actionText(this.stringMapper.apply(R.string.disburse))
      .actionRunner(() -> this.actionConsumer.accept(product))
      .build();
  }

  @Override
  public Observable<IndexItem> get() {
    return this.productStore.getAll()
      .defaultIfEmpty(new ArrayList<>())
      .flatMapObservable(Observable::fromIterable)
      .map(this::toItem);
  }

  static final class Builder {

    private StringMapper stringMapper;
    private CompanyHelper companyHelper;
    private DisbursableProductHelper productHelper;

    private DisbursableProductStore productStore;
    private Consumer<DisbursableProduct> actionConsumer;

    private Builder() {
    }

    final Builder stringMapper(StringMapper stringMapper) {
      this.stringMapper = ObjectHelper.checkNotNull(stringMapper, "stringMapper");
      return this;
    }

    final Builder companyHelper(CompanyHelper companyHelper) {
      this.companyHelper = ObjectHelper.checkNotNull(companyHelper, "companyHelper");
      return this;
    }

    final Builder productHelper(DisbursableProductHelper productHelper) {
      this.productHelper = ObjectHelper.checkNotNull(productHelper, "productHelper");
      return this;
    }

    final Builder productStore(DisbursableProductStore productStore) {
      this.productStore = ObjectHelper.checkNotNull(productStore, "productStore");
      return this;
    }

    final Builder actionConsumer(Consumer<DisbursableProduct> actionConsumer) {
      this.actionConsumer = ObjectHelper.checkNotNull(actionConsumer, "actionConsumer");
      return this;
    }

    final DisburseItemsSupplierProducts build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("stringMapper", ObjectHelper.isNull(this.stringMapper))
        .addPropertyNameIfMissing("companyHelper", ObjectHelper.isNull(this.companyHelper))
        .addPropertyNameIfMissing("productHelper", ObjectHelper.isNull(this.productHelper))
        .addPropertyNameIfMissing("productStore", ObjectHelper.isNull(this.productStore))
        .addPropertyNameIfMissing("actionConsumer", ObjectHelper.isNull(this.actionConsumer))
        .checkNoMissingProperties();
      return new DisburseItemsSupplierProducts(this);
    }
  }
}
