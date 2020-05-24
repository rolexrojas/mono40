package com.mono40.movil.session;

import com.mono40.movil.api.Api;
import com.mono40.movil.company.bank.BankStore;
import com.mono40.movil.company.partner.PartnerStore;
import com.mono40.movil.insurance.micro.MicroInsurancePartnerStore;
import com.mono40.movil.paypal.PayPalAccountStore;
import com.mono40.movil.product.ProductStore;
import com.mono40.movil.product.Products;
import com.mono40.movil.product.disbursable.DisbursableProductStore;
import com.mono40.movil.reactivex.CompletableBuilder;
import com.mono40.movil.util.BuilderChecker;
import com.mono40.movil.util.ObjectHelper;

import io.reactivex.Completable;

/**
 * @author hecvasro
 */
public final class SessionDataLoader {

  static Builder builder() {
    return new Builder();
  }

  private final BankStore bankStore;
  private final PartnerStore partnerStore;
  private final ProductStore productStore;
  private final MicroInsurancePartnerStore microInsurancePartnerStore;
  private final PayPalAccountStore payPalAccountStore;

  private final Api api;

  private SessionDataLoader(Builder builder) {
    this.bankStore = builder.bankStore;
    this.partnerStore = builder.partnerStore;
    this.productStore = builder.productStore;
    this.microInsurancePartnerStore = builder.microInsurancePartnerStore;
    this.payPalAccountStore = builder.payPalAccountStore;

    this.api = builder.api;
  }

  public final Completable load() {
    return this.api.fetchSessionData()
      .flatMapCompletable((data) -> {
        final Products products = data.products();

        final Completable productsSync = this.productStore.sync(products)
          .flatMapCompletable((diff) -> Completable.complete()); // TODO: Add and remove products to and from the purchase service.


        return CompletableBuilder.create()
          .add(this.bankStore.sync(data.banks()))
          .add(this.partnerStore.sync(data.partners()))
          .add(productsSync)
          .add(this.microInsurancePartnerStore.sync(data.microInsurancePartners()))
          .add(this.payPalAccountStore.sync(data.payPalAccounts()))
          .build();
      });
  }

  static final class Builder {

    private BankStore bankStore;
    private PartnerStore partnerStore;
    private ProductStore productStore;
    private DisbursableProductStore disbursableProductStore;
    private MicroInsurancePartnerStore microInsurancePartnerStore;
    private PayPalAccountStore payPalAccountStore;

    private Api api;

    private Builder() {
    }

    final Builder bankStore(BankStore bankStore) {
      this.bankStore = ObjectHelper.checkNotNull(bankStore, "bankStore");
      return this;
    }

    final Builder partnerStore(PartnerStore partnerStore) {
      this.partnerStore = ObjectHelper.checkNotNull(partnerStore, "partnerStore");
      return this;
    }

    final Builder productStore(ProductStore productStore) {
      this.productStore = ObjectHelper.checkNotNull(productStore, "productStore");
      return this;
    }

    final Builder disbursableProductStore(DisbursableProductStore disbursableProductStore) {
      this.disbursableProductStore = ObjectHelper
        .checkNotNull(disbursableProductStore, "disbursableProductStore");
      return this;
    }

    final Builder microInsurancePartnerStore(MicroInsurancePartnerStore store) {
      this.microInsurancePartnerStore = ObjectHelper.checkNotNull(
        store,
        "microInsurancePartnerStore"
      );
      return this;
    }

    final Builder payPalAccountStore(PayPalAccountStore store) {
      this.payPalAccountStore = ObjectHelper.checkNotNull(store, "payPalAccountStore");
      return this;
    }

    final Builder api(Api api) {
      this.api = ObjectHelper.checkNotNull(api, "api");
      return this;
    }

    final SessionDataLoader build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("bankStore", ObjectHelper.isNull(this.bankStore))
        .addPropertyNameIfMissing("partnerStore", ObjectHelper.isNull(this.partnerStore))
        .addPropertyNameIfMissing("productStore", ObjectHelper.isNull(this.productStore))
        .addPropertyNameIfMissing(
          "microInsurancePartnerStore",
          ObjectHelper.isNull(this.microInsurancePartnerStore)
        )
        .addPropertyNameIfMissing(
          "payPalAccountStore",
          ObjectHelper.isNull(this.payPalAccountStore)
        )
        .addPropertyNameIfMissing("api", ObjectHelper.isNull(this.api))
        .checkNoMissingProperties();
      return new SessionDataLoader(this);
    }
  }
}
