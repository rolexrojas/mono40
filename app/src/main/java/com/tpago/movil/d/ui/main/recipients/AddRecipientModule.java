package com.tpago.movil.d.ui.main.recipients;

import com.tpago.movil.app.ActivityScope;
import com.tpago.movil.d.domain.RecipientManager;
import com.tpago.movil.d.domain.session.SessionManager;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
class AddRecipientModule {
  @Provides
  @ActivityScope
  AddRecipientPresenter providePresenter(
    SessionManager sessionManager,
    RecipientManager recipientManager) {
    return new AddRecipientPresenter(sessionManager.getSession().getAuthToken(), recipientManager);
  }
}
