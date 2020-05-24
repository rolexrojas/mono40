package com.mono40.movil.dep.content;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Deprecated
@Module
public final class ContentModule {
  @Provides
  @Singleton
  SharedPreferencesCreator provideSharedPreferencesCreator(Context context) {
    return new SharedPreferencesCreator(context);
  }
}
