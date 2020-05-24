package com.mono40.movil.d.data.pos;

import android.content.Context;

import com.mono40.movil.BuildConfig;
import com.mono40.movil.d.domain.pos.PosBridge;
import com.mono40.movil.util.StringHelper;

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
