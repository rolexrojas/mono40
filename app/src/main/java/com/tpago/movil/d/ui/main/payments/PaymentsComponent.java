package com.tpago.movil.d.ui.main.payments;

import com.tpago.movil.d.ui.FragmentScope;
import com.tpago.movil.d.ui.main.MainComponent;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(modules = PaymentsModule.class, dependencies = MainComponent.class)
interface PaymentsComponent {
  void inject(PaymentsFragment fragment);
}
