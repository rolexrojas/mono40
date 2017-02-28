package com.tpago.movil.dep.ui.main.purchase;

import android.support.annotation.NonNull;

import com.tpago.movil.dep.data.StringHelper;
import com.tpago.movil.dep.domain.Product;
import com.tpago.movil.dep.domain.ProductManager;
import com.tpago.movil.dep.domain.api.ApiBridge;
import com.tpago.movil.dep.domain.pos.PosBridge;
import com.tpago.movil.dep.domain.session.SessionManager;
import com.tpago.movil.dep.ui.ChildFragmentScope;

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
  PurchasePaymentPresenter providePresenter(StringHelper stringHelper,
    ProductManager productManager, Lazy<PosBridge> posBridge, ApiBridge apiBridge,
    SessionManager sessionManager) {
    return new PurchasePaymentPresenter(stringHelper, paymentOption, productManager, posBridge,
      apiBridge, sessionManager);
  }
}
