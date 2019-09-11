package com.tpago.movil.app.ui.loader.takeover;

import androidx.fragment.app.FragmentManager;

import com.tpago.movil.app.ui.activity.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class ModuleLoaderTakeover {

  @Provides
  @ActivityScope
  TakeoverLoader takeoverLoader(FragmentManager fragmentManager) {
    return TakeoverLoader.create(fragmentManager);
  }
}
