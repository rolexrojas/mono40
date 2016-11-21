package com.gbh.movil.ui.main.payments;

import com.gbh.movil.ui.FragmentScope;
import com.gbh.movil.ui.main.MainComponent;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(modules = PaymentsModule.class, dependencies = MainComponent.class)
interface PaymentsComponent {
  void inject(PaymentsFragment fragment);
}
