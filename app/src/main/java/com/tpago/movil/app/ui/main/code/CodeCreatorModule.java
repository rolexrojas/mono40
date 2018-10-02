package com.tpago.movil.app.ui.main.code;

import android.support.v4.app.FragmentManager;

import com.tpago.movil.app.ui.activity.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class CodeCreatorModule {

  @Provides
  @ActivityScope
  CodeCreator codeCreator(FragmentManager fragmentManager) {
    return CodeCreator.create(fragmentManager);
  }
}
