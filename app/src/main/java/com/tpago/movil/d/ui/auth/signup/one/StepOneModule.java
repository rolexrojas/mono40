package com.tpago.movil.d.ui.auth.signup.one;

import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.ui.FragmentScope;

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
