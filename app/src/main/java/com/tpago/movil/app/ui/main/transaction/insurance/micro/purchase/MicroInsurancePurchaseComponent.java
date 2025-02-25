package com.tpago.movil.app.ui.main.transaction.insurance.micro.purchase;

import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.ui.activity.ActivityScope;
import com.tpago.movil.app.ui.activity.base.ActivityModule;
import com.tpago.movil.app.ui.activity.fragment.ActivityModuleFragment;
import com.tpago.movil.app.ui.main.transaction.insurance.micro.purchase.confirm.MicroInsurancePurchaseConfirmPresenter;
import com.tpago.movil.app.ui.main.transaction.insurance.micro.purchase.plan.MicroInsurancePurchasePlanFragment;
import com.tpago.movil.app.ui.main.transaction.insurance.micro.purchase.plan.MicroInsurancePurchasePlanPresenter;
import com.tpago.movil.app.ui.main.transaction.insurance.micro.purchase.term.MicroInsurancePurchaseTermFragment;
import com.tpago.movil.app.ui.main.transaction.insurance.micro.purchase.term.MicroInsurancePurchaseTermPresenter;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {
  ActivityModule.class,
  ActivityModuleFragment.class,
  MicroInsurancePurchaseModule.class
})
public interface MicroInsurancePurchaseComponent {

  void inject(MicroInsurancePurchaseActivity activity);

  void inject(MicroInsurancePurchasePlanFragment fragment);

  void inject(MicroInsurancePurchasePlanPresenter presenter);

  void inject(MicroInsurancePurchaseTermFragment fragment);

  void inject(MicroInsurancePurchaseTermPresenter presenter);

  void inject(MicroInsurancePurchaseConfirmPresenter presenter);

  @Subcomponent.Builder
  interface Builder extends ComponentBuilder<MicroInsurancePurchaseComponent> {

    Builder activityModule(ActivityModule module);

    Builder microInsurancePurchaseModule(MicroInsurancePurchaseModule module);
  }
}
