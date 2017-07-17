package com.tpago.movil.d.ui.main.payments;

import com.tpago.movil.app.FragmentScope;
import com.tpago.movil.d.ui.main.DepMainComponent;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(modules = PaymentsModule.class, dependencies = DepMainComponent.class)
interface PaymentsComponent {
  void inject(PaymentsFragment fragment);
}
