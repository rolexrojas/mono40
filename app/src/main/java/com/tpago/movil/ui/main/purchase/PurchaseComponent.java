package com.tpago.movil.ui.main.purchase;

import com.tpago.movil.data.StringHelper;
import com.tpago.movil.domain.ProductManager;
import com.tpago.movil.domain.api.ApiBridge;
import com.tpago.movil.domain.pos.PosBridge;
import com.tpago.movil.domain.session.SessionManager;
import com.tpago.movil.ui.FragmentScope;
import com.tpago.movil.ui.main.MainComponent;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(dependencies = MainComponent.class, modules = PurchaseModule.class)
interface PurchaseComponent {
  void inject(PurchaseFragment screen);

  ApiBridge provideApiBridge();
  PosBridge providePosBridge();
  ProductManager provideProductManager();
  PurchasePaymentOptionBinder providePaymentOptionBinder();
  SessionManager provideSessionManager();
  StringHelper provideStringHelper();
}
