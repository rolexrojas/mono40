package com.tpago.movil.dep.ui.main.recipients;

import android.content.Context;

import com.tpago.movil.app.AppComponent;
import com.tpago.movil.dep.data.SchedulerProvider;
import com.tpago.movil.dep.ui.ActivityComponent;
import com.tpago.movil.dep.ui.ActivityModule;
import com.tpago.movil.dep.ui.ActivityScope;

import dagger.Component;

/**
 * TODO
 *
 * @author hecvasro
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = { ActivityModule.class,
  AddRecipientModule.class })
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
  Context provideContext();

  /**
   * TODO
   *
   * @return TODO
   */
  SchedulerProvider provideSchedulerProvider();
}
