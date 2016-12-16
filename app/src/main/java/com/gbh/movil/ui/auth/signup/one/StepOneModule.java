package com.gbh.movil.ui.auth.signup.one;

import com.gbh.movil.data.StringHelper;
import com.gbh.movil.ui.FragmentScope;

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
