package com.tpago.movil.d.ui.auth.signin;

import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.InitialDataLoader;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.ui.ActivityScope;
import com.tpago.movil.d.ui.MessageDispatcher;
import com.tpago.movil.d.ui.view.widget.LoadIndicator;

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
