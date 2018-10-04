package com.tpago.movil.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class GsonModuleFlavored {

  @Provides
  @Singleton
  Gson gson() {
    return new GsonBuilder()
      .setDateFormat(GsonConstant.FORMAT_DATE)
      .create();
  }
}
