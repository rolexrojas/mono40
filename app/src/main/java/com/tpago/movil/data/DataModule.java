package com.tpago.movil.data;

import android.content.Context;

import com.tpago.movil.data.api.DataApiModule;
import com.tpago.movil.data.auth.DataAuthModule;
import com.tpago.movil.data.net.DataNetModule;
import com.tpago.movil.domain.KeyValueStore;

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
    DataApiModule.class,
    DataAuthModule.class,
    DataNetModule.class
  }
)
public final class DataModule {

  @Provides
  @Singleton
  StringMapper stringMapper(Context context) {
    return context::getString;
  }

  @Provides
  @Singleton
  KeyValueStore keyValueStore(Context context) {
    return SharedPreferencesKeyValueStore.create(context);
  }
}
