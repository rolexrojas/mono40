package com.tpago.movil.d.data.pos;

import android.content.Context;

import com.tpago.movil.d.domain.pos.PosBridge;

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
    return new CubePosBridge(context);
  }
}
