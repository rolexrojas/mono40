package com.tpago.movil.ui.main.recipients;

import android.content.Context;

import com.tpago.movil.AppComponent;
import com.tpago.movil.data.SchedulerProvider;
import com.tpago.movil.ui.ActivityComponent;
import com.tpago.movil.ui.ActivityModule;
import com.tpago.movil.ui.ActivityScope;

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
