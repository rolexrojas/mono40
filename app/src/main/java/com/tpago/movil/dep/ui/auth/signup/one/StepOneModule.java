package com.tpago.movil.dep.ui.auth.signup.one;

import com.tpago.movil.dep.data.StringHelper;
import com.tpago.movil.dep.ui.FragmentScope;

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
