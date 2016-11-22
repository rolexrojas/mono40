package com.gbh.movil.ui.main.payments.recipients;

import com.gbh.movil.AppComponent;
import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.ui.ActivityComponent;
import com.gbh.movil.ui.ActivityModule;
import com.gbh.movil.ui.ActivityScope;

import dagger.Component;

/**
 * TODO
 *
 * @author hecvasro
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = { ActivityModule.class })
public interface AddRecipientComponent extends ActivityComponent {
  /**
   * TODO
   *
   * @param activity
   *   TODO
   */
  void inject(AddRecipientActivity activity);

  /**
   * TODO
   *
   * @return TODO
   */
  SchedulerProvider provideSchedulerProvider();
}
