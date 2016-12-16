package com.gbh.movil.ui.auth.signup;

import com.gbh.movil.AppComponent;
import com.gbh.movil.data.StringHelper;
import com.gbh.movil.domain.session.SessionManager;
import com.gbh.movil.ui.ActivityComponent;
import com.gbh.movil.ui.ActivityModule;
import com.gbh.movil.ui.ActivityScope;
import com.gbh.movil.ui.MessageDispatcher;
import com.gbh.movil.ui.view.widget.LoadIndicator;

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
