package com.tpago.movil.d.ui.auth.signup;

import com.tpago.movil.d.AppComponent;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.InitialDataLoader;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.ui.ActivityComponent;
import com.tpago.movil.d.ui.ActivityModule;
import com.tpago.movil.d.ui.ActivityScope;
import com.tpago.movil.d.ui.MessageDispatcher;
import com.tpago.movil.d.ui.view.widget.LoadIndicator;

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
