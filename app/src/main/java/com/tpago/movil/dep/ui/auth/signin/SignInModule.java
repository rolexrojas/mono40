package com.tpago.movil.dep.ui.auth.signin;

import com.tpago.movil.dep.data.StringHelper;
import com.tpago.movil.dep.domain.InitialDataLoader;
import com.tpago.movil.dep.domain.session.SessionManager;
import com.tpago.movil.dep.ui.ActivityScope;
import com.tpago.movil.dep.ui.MessageDispatcher;
import com.tpago.movil.dep.ui.view.widget.LoadIndicator;

import dagger.Module;
import dagger.Provides;

/**
 * TODO
 *
 * @author hecvasro
 */
@Module
class SignInModule {
  /**
   * TODO
   *
   * @param sessionManager
   *   TODO
   *
   * @return TODO
   */
  @Provides
  @ActivityScope
  SignInPresenter providePresenter(StringHelper stringHelper, MessageDispatcher messageDispatcher,
    LoadIndicator loadIndicator, SessionManager sessionManager,
    InitialDataLoader initialDataLoader) {
    return new SignInPresenter(stringHelper, messageDispatcher, loadIndicator, sessionManager,
      initialDataLoader);
  }
}
