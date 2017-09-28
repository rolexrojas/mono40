package com.tpago.movil.d.ui.main.purchase;

import com.tpago.movil.dep.Session;
import com.tpago.movil.app.ui.FragmentScope;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.ui.main.DepMainComponent;

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
  Session provideSession();
  SessionManager provideSessionManager();
  StringHelper provideStringHelper();
}
