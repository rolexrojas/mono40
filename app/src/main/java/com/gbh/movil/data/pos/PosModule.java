package com.gbh.movil.data.pos;

import android.content.Context;

import com.gbh.movil.domain.pos.PosBridge;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * TODO
 *
 * @author hecvasro
 */
@Module
public class PosModule {
  @Provides
  @Singleton
  PosBridge providePosBridge(Context context) {
    return new CubePosBridge(context);
  }
}
