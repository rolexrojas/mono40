package com.tpago.movil.app.ui.main.transaction.insurance.micro.index;

import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.ui.fragment.FragmentScope;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = MicroInsuranceIndexModule.class)
public interface MicroInsuranceIndexComponent {

  void inject(MicroInsuranceIndexFragment fragment);

  @Subcomponent.Builder
  interface Builder extends ComponentBuilder<MicroInsuranceIndexComponent> {

    Builder indexModule(MicroInsuranceIndexModule module);
  }
}
