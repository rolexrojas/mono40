package com.tpago.movil.app.ui;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class ToolbarActivityModule {

  @Provides
  @ActivityScope
  @HomeButton
  NavButtonClickHandler navButtonClickHandler() {
    return NavButtonClickHandler.create();
  }
}
