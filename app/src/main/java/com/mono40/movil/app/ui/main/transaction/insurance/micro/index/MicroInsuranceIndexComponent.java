package com.mono40.movil.app.ui.main.transaction.insurance.micro.index;

import com.mono40.movil.app.di.ComponentBuilder;
import com.mono40.movil.app.ui.fragment.FragmentScope;

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
