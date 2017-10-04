package com.tpago.movil.data;

import android.content.Context;

import com.tpago.movil.BuildConfig;
import com.tpago.movil.data.api.DataApiModule;
import com.tpago.movil.data.auth.DataAuthModule;
import com.tpago.movil.net.NetModule;
import com.tpago.movil.KeyValueStore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * {@link Module} that contains providers for objects that belong to the value layer.
 *
 * @author hecvasro
 */
@Module(
  includes = {
    DataFlavorModule.class,
    DataApiModule.class,
    DataAuthModule.class,
    NetModule.class
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
    final KeyValueStore keyValueStore = SharedPreferencesKeyValueStore.create(context);
    if (BuildConfig.DEBUG) {
      return DebugKeyValueStore.create(keyValueStore);
    } else {
      return keyValueStore;
    }
  }
}
