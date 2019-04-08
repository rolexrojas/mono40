package com.tpago.movil.d.ui.main.purchase;

import com.tpago.movil.app.ui.activity.ActivityScope;
import com.tpago.movil.app.ui.activity.base.ActivityModule;
import com.tpago.movil.app.ui.fragment.FragmentScope;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.ui.ChildFragment;
import com.tpago.movil.d.ui.main.DepMainComponent;
import com.tpago.movil.d.ui.main.MainContainer;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(
  dependencies = {DepMainComponent.class},
  modules = {PurchaseModule.class}
)
public interface NonNfcPurchaseComponent {

  void inject(NonNfcPurchaseFragment screen);

  CompanyHelper logoFactory();

  DepApiBridge provideApiBridge();

  PosBridge providePosBridge();

  ProductManager provideProductManager();

  PurchasePaymentOptionBinder providePaymentOptionBinder();

  StringHelper provideStringHelper();
}
