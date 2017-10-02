package com.tpago.movil.d.ui.main.recipient.addition;

import android.content.Context;

import com.tpago.movil.app.ui.ActivityModule;
import com.tpago.movil.app.ui.ActivityScope;
import com.tpago.movil.dep.AppComponent;
import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.ui.ActivityComponent;
import com.tpago.movil.d.ui.DepActivityModule;

import dagger.Component;

/**
 * @author hecvasro
 */
@ActivityScope
@Component(
  dependencies = AppComponent.class,
  modules = {
    ActivityModule.class,
    com.tpago.movil.dep.ActivityModule.class,
    DepActivityModule.class,
    AddRecipientModule.class
  }
)
public interface AddRecipientComponent extends ActivityComponent {

  void inject(AddRecipientActivity activity);

  void inject(SearchOrChooseRecipientFragment fragment);

  void inject(RecipientBuilderFragment fragment);

  Context provideContext();

  DepApiBridge provideApiBridge();

  SchedulerProvider provideSchedulerProvider();

  SessionManager provideSessionManager();
}
