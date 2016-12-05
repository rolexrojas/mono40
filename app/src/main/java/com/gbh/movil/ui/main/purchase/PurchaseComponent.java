package com.gbh.movil.ui.main.purchase;

import com.gbh.movil.ui.FragmentScope;
import com.gbh.movil.ui.main.MainComponent;

import dagger.Component;

/**
 * TODO
 *
 * @author hecvasro
 */
@FragmentScope
@Component(dependencies = MainComponent.class, modules = PurchaseModule.class)
interface PurchaseComponent {
  /**
   * TODO
   *
   * @param screen
   *   TODO
   */
  void inject(PurchaseFragment screen);

  /**
   * TODO
   *
   * @param fragment
   *   TODO
   */
  void inject(CommercePaymentDialogFragment fragment);
}
