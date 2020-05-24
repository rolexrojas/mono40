package com.mono40.movil.d.ui.main.purchase;

import com.mono40.movil.app.ui.activity.ActivityScope;
import com.mono40.movil.app.ui.activity.base.ActivityModule;
import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.company.CompanyHelper;
import com.mono40.movil.d.data.StringHelper;
import com.mono40.movil.d.domain.ProductManager;
import com.mono40.movil.d.domain.api.DepApiBridge;
import com.mono40.movil.d.domain.pos.PosBridge;
import com.mono40.movil.d.ui.ChildFragment;
import com.mono40.movil.d.ui.main.DepMainComponent;
import com.mono40.movil.d.ui.main.MainContainer;

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
