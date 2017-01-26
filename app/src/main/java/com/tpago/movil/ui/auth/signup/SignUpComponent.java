package com.tpago.movil.ui.auth.signup;

import com.tpago.movil.AppComponent;
import com.tpago.movil.data.StringHelper;
import com.tpago.movil.domain.session.SessionManager;
import com.tpago.movil.ui.ActivityComponent;
import com.tpago.movil.ui.ActivityModule;
import com.tpago.movil.ui.ActivityScope;
import com.tpago.movil.ui.MessageDispatcher;
import com.tpago.movil.ui.view.widget.LoadIndicator;

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
}
