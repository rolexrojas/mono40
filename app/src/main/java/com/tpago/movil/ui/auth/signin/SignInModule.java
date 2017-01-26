package com.tpago.movil.ui.auth.signin;

import com.tpago.movil.data.StringHelper;
import com.tpago.movil.domain.InitialDataLoader;
import com.tpago.movil.domain.session.SessionManager;
import com.tpago.movil.ui.ActivityScope;
import com.tpago.movil.ui.MessageDispatcher;
import com.tpago.movil.ui.view.widget.LoadIndicator;

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
