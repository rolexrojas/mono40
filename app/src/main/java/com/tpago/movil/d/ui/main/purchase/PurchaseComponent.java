package com.tpago.movil.d.ui.main.purchase;

import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.api.ApiBridge;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.ui.FragmentScope;
import com.tpago.movil.d.ui.main.MainComponent;

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
