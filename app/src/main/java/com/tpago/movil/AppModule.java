package com.tpago.movil;

import android.content.Context;

import com.tpago.movil.util.Preconditions;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
final class AppModule {
  private final Context context;

  AppModule(Context context) {
    this.context = Preconditions.checkNotNull(context, "context == null");
  }

  @Provides
  @Singleton
  Context provideContext() {
    return context;
  }

  @Provides
  @Singleton
  SharedPreferencesCreator provideSharedPreferencesCreator(Context context) {
    return new SharedPreferencesCreator(context);
  }

  @Provides
  @Singleton
  UserStore provideUserStore(SharedPreferencesCreator sharedPreferencesCreator) {
    return new UserStore(sharedPreferencesCreator);
  }
}
