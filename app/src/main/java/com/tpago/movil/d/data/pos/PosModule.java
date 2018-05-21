package com.tpago.movil.d.data.pos;

import android.content.Context;

import com.tpago.movil.BuildConfig;
import com.tpago.movil.d.domain.pos.PosBridge;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Deprecated
@Module
public final class PosModule {

  @Deprecated
  @Provides
  @Singleton
  PosBridge providePosBridge(Context context) {
    final String flavor = BuildConfig.FLAVOR
      .toLowerCase();
    if (flavor.contains("mock")) {
      return MockPosBridge.create();
    }
    return new CubePosBridge(context);
  }
}
