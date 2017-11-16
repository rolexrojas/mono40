package com.tpago.movil.d.data.pos;

import android.content.Context;

import com.tpago.movil.BuildConfig;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.util.StringHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * TODO
 *
 * @author hecvasro
 */
@Deprecated
@Module
public class PosModule {

  @Provides
  @Singleton
  PosBridge providePosBridge(Context context) {
    if (StringHelper.isNullOrEmpty(BuildConfig.API_URL)) {
      return MockPosBridge.create();
    } else {
      return new CubePosBridge(context);
    }
  }
}
