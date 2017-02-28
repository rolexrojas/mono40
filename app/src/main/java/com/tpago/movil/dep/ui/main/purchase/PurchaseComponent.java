package com.tpago.movil.dep.ui.main.purchase;

import com.tpago.movil.dep.data.StringHelper;
import com.tpago.movil.dep.domain.ProductManager;
import com.tpago.movil.dep.domain.api.ApiBridge;
import com.tpago.movil.dep.domain.pos.PosBridge;
import com.tpago.movil.dep.domain.session.SessionManager;
import com.tpago.movil.dep.ui.FragmentScope;
import com.tpago.movil.dep.ui.main.MainComponent;

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
