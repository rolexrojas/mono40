package com.tpago.movil.dep.ui.main.purchase;

import com.tpago.movil.app.FragmentScope;
import com.tpago.movil.dep.data.StringHelper;
import com.tpago.movil.dep.domain.ProductManager;
import com.tpago.movil.dep.domain.api.DepApiBridge;
import com.tpago.movil.dep.domain.pos.PosBridge;
import com.tpago.movil.dep.domain.session.SessionManager;
import com.tpago.movil.dep.ui.main.DepMainComponent;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(dependencies = DepMainComponent.class, modules = PurchaseModule.class)
interface PurchaseComponent {
  void inject(PurchaseFragment screen);

  DepApiBridge provideApiBridge();
  PosBridge providePosBridge();
  ProductManager provideProductManager();
  PurchasePaymentOptionBinder providePaymentOptionBinder();
  SessionManager provideSessionManager();
  StringHelper provideStringHelper();
}
