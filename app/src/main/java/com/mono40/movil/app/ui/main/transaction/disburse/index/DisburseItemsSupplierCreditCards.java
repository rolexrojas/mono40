package com.mono40.movil.app.ui.main.transaction.disburse.index;

import com.mono40.movil.R;
import com.mono40.movil.app.ui.alert.AlertManager;
import com.mono40.movil.app.ui.main.transaction.item.IndexItem;
import com.mono40.movil.company.CompanyHelper;
import com.mono40.movil.company.bank.Bank;
import com.mono40.movil.app.StringMapper;
import com.mono40.movil.util.function.Consumer;
import com.mono40.movil.product.Product;
import com.mono40.movil.product.ProductHelper;
import com.mono40.movil.product.ProductStore;
import com.mono40.movil.util.BuilderChecker;
import com.mono40.movil.util.ObjectHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * @author hecvasro
 */
final class DisburseItemsSupplierCreditCards implements DisburseItemsSupplier {

  static Builder builder() {
    return new Builder();
  }

  private final StringMapper stringMapper;
  private final AlertManager alertManager;
  private final CompanyHelper companyHelper;
  private final ProductHelper productHelper;

  private final ProductStore productStore;
  private final Consumer<Product> actionConsumer;

  private DisburseItemsSupplierCreditCards(Builder builder) {
    this.stringMapper = builder.stringMapper;
    this.alertManager = builder.alertManager;
    this.companyHelper = builder.companyHelper;
    this.productHelper = builder.productHelper;

    this.productStore = builder.productStore;
    this.actionConsumer = builder.actionConsumer;
  }

  private IndexItem createItem(Product product, List<Product> accounts) {
    final Bank bank = product.bank();
    return IndexItem.builder()
      .pictureUri(this.companyHelper.getLogoUri(bank, Bank.LogoStyle.GRAY_36))
      .titleText(this.productHelper.getBankNameAndTypeName(product))
      .subtitleText(this.productHelper.getMaskedNumber(product))
      .actionText(this.stringMapper.apply(R.string.advance))
      .actionRunner(() -> {
        if (accounts.isEmpty()) {
          this.alertManager.builder()
            .message(R.string.thereIsNoAccountsAvailableForCashAdvance)
            .show();
        } else {
          this.actionConsumer.accept(product);
        }
      })
      .build();
  }

  @Override
  public Observable<IndexItem> get() {
    return this.productStore.getCreditCards().flatMapObservable(Observable::fromIterable)
      .withLatestFrom(this.productStore.getAccounts().defaultIfEmpty(new ArrayList<>())
        .toObservable(), this::createItem);
  }

  static final class Builder {

    private StringMapper stringMapper;
    private AlertManager alertManager;
    private CompanyHelper companyHelper;
    private ProductHelper productHelper;

    private ProductStore productStore;
    private Consumer<Product> actionConsumer;

    private Builder() {
    }

    final Builder stringMapper(StringMapper stringMapper) {
      this.stringMapper = ObjectHelper.checkNotNull(stringMapper, "stringMapper");
      return this;
    }

    final Builder alertManager(AlertManager alertManager) {
      this.alertManager = ObjectHelper.checkNotNull(alertManager, "alertManager");
      return this;
    }

    final Builder companyHelper(CompanyHelper companyHelper) {
      this.companyHelper = ObjectHelper.checkNotNull(companyHelper, "companyHelper");
      return this;
    }

    final Builder productHelper(ProductHelper productHelper) {
      this.productHelper = ObjectHelper.checkNotNull(productHelper, "productHelper");
      return this;
    }

    final Builder productStore(ProductStore productStore) {
      this.productStore = ObjectHelper.checkNotNull(productStore, "productStore");
      return this;
    }

    final Builder actionConsumer(Consumer<Product> actionConsumer) {
      this.actionConsumer = ObjectHelper.checkNotNull(actionConsumer, "actionConsumer");
      return this;
    }

    final DisburseItemsSupplierCreditCards build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("stringMapper", ObjectHelper.isNull(this.stringMapper))
        .addPropertyNameIfMissing("alertManager", ObjectHelper.isNull(this.alertManager))
        .addPropertyNameIfMissing("companyHelper", ObjectHelper.isNull(this.companyHelper))
        .addPropertyNameIfMissing("productHelper", ObjectHelper.isNull(this.productHelper))
        .addPropertyNameIfMissing("productStore", ObjectHelper.isNull(this.productStore))
        .addPropertyNameIfMissing("actionConsumer", ObjectHelper.isNull(this.actionConsumer))
        .checkNoMissingProperties();
      return new DisburseItemsSupplierCreditCards(this);
    }
  }
}
