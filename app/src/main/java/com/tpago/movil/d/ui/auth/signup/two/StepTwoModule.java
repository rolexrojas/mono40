package com.tpago.movil.d.ui.auth.signup.two;

import android.support.annotation.NonNull;

import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.InitialDataLoader;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.ui.FragmentScope;
import com.tpago.movil.d.ui.MessageDispatcher;

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
    SessionManager sessionManager, InitialDataLoader initialDataLoader) {
    return new StepTwoPresenter(
      stringHelper,
      messageDispatcher,
      sessionManager,
      phoneNumber,
      email,
      initialDataLoader);
  }
}
