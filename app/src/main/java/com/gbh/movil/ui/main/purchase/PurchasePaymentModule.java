package com.gbh.movil.ui.main.purchase;

import android.support.annotation.NonNull;

import com.gbh.movil.data.StringHelper;
import com.gbh.movil.domain.Product;
import com.gbh.movil.domain.ProductManager;
import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.domain.pos.PosBridge;
import com.gbh.movil.ui.ChildFragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
class PurchasePaymentModule {
  private final Product paymentOption;

  PurchasePaymentModule(@NonNull Product paymentOption) {
    this.paymentOption = paymentOption;
  }

  @Provides
  @ChildFragmentScope
  PurchasePaymentPresenter providePresenter(StringHelper stringHelper,
    ProductManager productManager, PosBridge posBridge, ApiBridge apiBridge) {
    return new PurchasePaymentPresenter(stringHelper, paymentOption, productManager, posBridge,
      apiBridge);
  }
}
