package com.mono40.movil.app.ui.main.transaction.paypal.confirm;

import com.mono40.movil.api.Api;
import com.mono40.movil.app.StringMapper;
import com.mono40.movil.app.ui.alert.AlertManager;
import com.mono40.movil.app.ui.fragment.FragmentScopeChild;
import com.mono40.movil.app.ui.loader.takeover.TakeoverLoader;
import com.mono40.movil.d.domain.ProductManager;
import com.mono40.movil.paypal.PayPalAccount;
import com.mono40.movil.util.ObjectHelper;

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
