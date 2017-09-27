package com.tpago.movil.company;

import com.tpago.movil.app.DisplayDensity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * {@link Module} that contributes to the graph all object related to {@link Company companies}.
 *
 * @author hecvasro
 */
@Module
public final class CompanyModule {

  @Provides
  @Singleton
  final LogoUriMapper createLogoUriMapper(DisplayDensity displayDensity) {
    throw new UnsupportedOperationException("not implemented");
  }
}
