package com.gbh.movil.ui.main.payments.commerce;

import com.gbh.movil.ui.FragmentScope;
import com.gbh.movil.ui.main.MainComponent;

import dagger.Component;

/**
 * TODO
 *
 * @author hecvasro
 */
@FragmentScope
@Component(dependencies = MainComponent.class, modules = CommercePaymentsModule.class)
interface CommercePaymentsComponent {
  /**
   * TODO
   *
   * @param screen
   *   TODO
   */
  void inject(CommercePaymentsFragment screen);
}
