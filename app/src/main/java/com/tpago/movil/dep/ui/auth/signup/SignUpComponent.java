package com.tpago.movil.dep.ui.auth.signup;

import com.tpago.movil.app.AppComponent;
import com.tpago.movil.dep.data.StringHelper;
import com.tpago.movil.dep.domain.InitialDataLoader;
import com.tpago.movil.dep.domain.session.SessionManager;
import com.tpago.movil.dep.ui.ActivityComponent;
import com.tpago.movil.dep.ui.ActivityModule;
import com.tpago.movil.dep.ui.ActivityScope;
import com.tpago.movil.dep.ui.MessageDispatcher;
import com.tpago.movil.dep.ui.view.widget.LoadIndicator;

import dagger.Component;

/**
 * @author hecvasro
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface SignUpComponent extends ActivityComponent {
  void inject(SignUpActivity activity);

  LoadIndicator provideLoadIndicator();
  MessageDispatcher provideMessageDispatcher();
  SessionManager provideSessionManager();
  StringHelper provideStringHelper();
  InitialDataLoader provideInitialDataLoader();
}
