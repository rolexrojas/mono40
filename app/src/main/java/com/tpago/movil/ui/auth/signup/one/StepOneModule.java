package com.tpago.movil.ui.auth.signup.one;

import com.tpago.movil.data.StringHelper;
import com.tpago.movil.ui.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
class StepOneModule {
  @Provides
  @FragmentScope
  StepOnePresenter providePresenter(StringHelper stringHelper) {
    return new StepOnePresenter(stringHelper);
  }
}
