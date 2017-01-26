package com.tpago.movil.ui.main.payments;

import com.tpago.movil.ui.FragmentScope;
import com.tpago.movil.ui.main.MainComponent;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(modules = PaymentsModule.class, dependencies = MainComponent.class)
interface PaymentsComponent {
  void inject(PaymentsFragment fragment);
}
