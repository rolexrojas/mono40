package com.tpago.movil.dep.ui.main.recipients;

import android.content.Context;

import com.tpago.movil.app.ActivityModule;
import com.tpago.movil.app.ActivityScope;
import com.tpago.movil.app.AppComponent;
import com.tpago.movil.dep.data.SchedulerProvider;
import com.tpago.movil.dep.domain.api.DepApiBridge;
import com.tpago.movil.dep.domain.session.SessionManager;
import com.tpago.movil.dep.ui.ActivityComponent;
import com.tpago.movil.dep.ui.DepActivityModule;

import dagger.Component;

/**
 * @author hecvasro
 */
@ActivityScope
@Component(
  dependencies = AppComponent.class,
  modules = {
    ActivityModule.class,
    DepActivityModule.class,
    AddRecipientModule.class
  })
public interface AddRecipientComponent extends ActivityComponent {
  void inject(AddRecipientActivity activity);

  void inject(RecipientBuilderFragment fragment);

  Context provideContext();
  DepApiBridge provideApiBridge();
  SchedulerProvider provideSchedulerProvider();
  SessionManager provideSessionManager();
}
