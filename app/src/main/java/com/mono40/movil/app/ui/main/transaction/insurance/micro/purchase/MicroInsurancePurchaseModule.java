package com.mono40.movil.app.ui.main.transaction.insurance.micro.purchase;

import com.mono40.movil.app.ui.activity.ActivityScope;
import com.mono40.movil.util.ObjectHelper;

import dagger.Module;
import dagger.Provides;

@Module
public final class MicroInsurancePurchaseModule {

  static MicroInsurancePurchaseModule create(MicroInsurancePurchase purchase) {
    return new MicroInsurancePurchaseModule(purchase);
  }

  private final MicroInsurancePurchase purchase;

  private MicroInsurancePurchaseModule(MicroInsurancePurchase purchase) {
    this.purchase = ObjectHelper.checkNotNull(purchase, "purchase");
  }

  @Provides
  @ActivityScope
  MicroInsurancePurchase microInsurancePurchase() {
    return this.purchase;
  }
}
