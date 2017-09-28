package com.tpago.movil.domain;

import com.tpago.movil.domain.auth.DomainAuthModule;

import dagger.Module;

/**
 * @author hecvasro
 */
@Module(
  includes = {
    DomainAuthModule.class
  }
)
public final class DomainModule {
}
