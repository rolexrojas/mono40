package com.tpago.movil.app.ui.main.transaction.paypal.confirm;

import com.tpago.movil.api.Api;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.app.ui.fragment.FragmentScopeChild;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.paypal.PayPalAccount;
import com.tpago.movil.util.ObjectHelper;

import dagger.Module;
import dagger.Provides;

@Module
public final class PayPalTransactionConfirmModule {

  static PayPalTransactionConfirmModule create(PayPalTransactionConfirmPresentation presentation) {
    return new PayPalTransactionConfirmModule(presentation);
  }

  private final PayPalTransactionConfirmPresentation presentation;

  private PayPalTransactionConfirmModule(PayPalTransactionConfirmPresentation presentation) {
    this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
  }

  @Provides
  @FragmentScopeChild
  PayPalTransactionConfirmPresenter presenter(
    PayPalAccount recipient,
    ProductManager productManager,
    Api api,
    StringMapper stringMapper,
    AlertManager alertManager,
    TakeoverLoader takeoverLoader
  ) {
    return PayPalTransactionConfirmPresenter.builder()
      .recipient(recipient)
      .productManager(productManager)
      .api(api)
      .stringMapper(stringMapper)
      .alertManager(alertManager)
      .takeoverLoader(takeoverLoader)
      .presentation(this.presentation)
      .build();
  }
}
