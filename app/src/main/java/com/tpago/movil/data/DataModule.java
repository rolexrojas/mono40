package com.tpago.movil.data;

import android.content.Context;

import com.tpago.movil.data.bus.BusModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * {@link Module} that contains providers for objects that belong to the data layer.
 *
 * @author hecvasro
 */
@Module(
  includes = {
    DataFlavorModule.class,
    BusModule.class
  }
)
public final class DataModule {

  @Provides
  @Singleton
  StringMapper stringMapper(Context context) {
    return context::getString;
  }
}
