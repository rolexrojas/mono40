package com.tpago.movil.d.ui.main.purchase;

import androidx.annotation.NonNull;

import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.app.ui.fragment.FragmentScopeChild;

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
  @FragmentScopeChild
  PurchasePaymentPresenter providePresenter(
    StringHelper stringHelper,
    ProductManager productManager,
    Lazy<PosBridge> posBridge
  ) {
    return new PurchasePaymentPresenter(
      stringHelper,
      paymentOption,
      productManager,
      posBridge
    );
  }
}
