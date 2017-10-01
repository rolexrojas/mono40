package com.tpago.movil.app.ui;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class BaseActivityModule {

  @Provides
  @ActivityScope
  @BackButton
  NavButtonClickHandler navButtonClickHandler() {
    return NavButtonClickHandler.create();
  }
}
