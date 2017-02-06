package com.tpago.movil.ui.main.purchase;

import android.support.annotation.NonNull;

import com.tpago.movil.data.StringHelper;
import com.tpago.movil.domain.Product;
import com.tpago.movil.domain.ProductManager;
import com.tpago.movil.domain.api.ApiBridge;
import com.tpago.movil.domain.pos.PosBridge;
import com.tpago.movil.domain.session.SessionManager;
import com.tpago.movil.ui.ChildFragmentScope;

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
