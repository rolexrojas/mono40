package com.tpago.movil.company;

import com.tpago.movil.DisplayDensity;

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
  final TemplateToLogoCatalogMapper logoCatalogMapper(DisplayDensity displayDensity) {
    return TemplateToLogoCatalogMapper.create(displayDensity);
  }
}
