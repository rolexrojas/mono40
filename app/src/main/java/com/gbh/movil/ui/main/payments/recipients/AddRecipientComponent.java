package com.gbh.movil.ui.main.payments.recipients;

import com.gbh.movil.AppComponent;
import com.gbh.movil.ui.ActivityScope;

import dagger.Component;

/**
 * TODO
 *
 * @author hecvasro
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = AddRecipientModule.class)
interface AddRecipientComponent {
  /**
   * TODO
   *
   * @param activity
   *   TODO
   */
  void inject(AddRecipientActivity activity);
}
