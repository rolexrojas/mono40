package com.gbh.movil.ui.main.purchase;

import com.gbh.movil.data.StringHelper;
import com.gbh.movil.domain.pos.PosBridge;
import com.gbh.movil.ui.FragmentScope;
import com.gbh.movil.ui.main.MainComponent;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(dependencies = MainComponent.class, modules = PurchaseModule.class)
interface PurchaseComponent {
  void inject(PurchaseFragment screen);

  PosBridge providePosBridge();
  PurchasePaymentOptionBinder providePaymentOptionBinder();
  StringHelper provideStringHelper();
}
