package com.tpago.movil.data.auth;

import com.tpago.movil.data.api.Api;
import com.tpago.movil.data.auth.alt.DataAltAuthModule;
import com.tpago.movil.domain.auth.AuthService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module(includes = DataAltAuthModule.class)
public final class DataAuthModule {

  @Provides
  @Singleton
  AuthService authService(Api api) {
    return ApiAuthService.create(api);
  }
}
