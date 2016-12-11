package com.gbh.movil.ui.auth.signup.two;

import android.support.annotation.NonNull;

import com.gbh.movil.data.StringHelper;
import com.gbh.movil.domain.session.SessionManager;
import com.gbh.movil.ui.FragmentScope;
import com.gbh.movil.ui.MessageDispatcher;
import com.gbh.movil.ui.view.widget.LoadIndicator;

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
    LoadIndicator loadIndicator, SessionManager sessionManager) {
    return new StepTwoPresenter(stringHelper, messageDispatcher, loadIndicator, sessionManager,
      phoneNumber, email);
  }
}
