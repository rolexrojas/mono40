package com.tpago.movil.ui.auth.signup.two;

import android.support.annotation.NonNull;

import com.tpago.movil.data.StringHelper;
import com.tpago.movil.domain.session.SessionManager;
import com.tpago.movil.ui.FragmentScope;
import com.tpago.movil.ui.MessageDispatcher;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
class StepTwoModule {
  private final String phoneNumber;
  private final String email;

  StepTwoModule(@NonNull String phoneNumber, @NonNull String email) {
    this.phoneNumber = phoneNumber;
    this.email = email;
  }

  @Provides
  @FragmentScope
  StepTwoPresenter providePresenter(StringHelper stringHelper, MessageDispatcher messageDispatcher,
    SessionManager sessionManager) {
    return new StepTwoPresenter(stringHelper, messageDispatcher, sessionManager, phoneNumber, email);
  }
}
