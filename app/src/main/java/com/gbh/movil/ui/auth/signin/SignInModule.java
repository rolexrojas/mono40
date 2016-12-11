package com.gbh.movil.ui.auth.signin;

import com.gbh.movil.data.StringHelper;
import com.gbh.movil.domain.session.SessionManager;
import com.gbh.movil.ui.ActivityScope;
import com.gbh.movil.ui.MessageDispatcher;
import com.gbh.movil.ui.view.widget.LoadIndicator;

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
    LoadIndicator loadIndicator, SessionManager sessionManager) {
    return new SignInPresenter(stringHelper, messageDispatcher, loadIndicator, sessionManager);
  }
}
