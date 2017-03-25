package com.tpago.movil.d.ui.main.purchase;

import android.support.annotation.NonNull;

import com.tpago.movil.Session;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.ui.ChildFragmentScope;

import dagger.Lazy;
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
  PurchasePaymentPresenter providePresenter(
    StringHelper stringHelper,
    ProductManager productManager,
    Lazy<PosBridge> posBridge,
    Session session) {
    return new PurchasePaymentPresenter(
      stringHelper,
      paymentOption,
      productManager,
      posBridge,
      session);
  }
}
