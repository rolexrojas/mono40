package com.tpago.movil.d.domain;

import android.content.Context;

import com.squareup.picasso.Picasso;
import com.tpago.movil.api.ApiService;
import com.tpago.movil.net.NetworkService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Deprecated
@Module
public final class DomainModule {
  @Provides
  @Singleton
  BankProvider provideBankProvider(
    BankRepo bankRepo,
    NetworkService networkService,
    ApiService apiService,
    Context context) {
    return new BankProvider(
      bankRepo,
      networkService,
      apiService,
      Picasso.with(context));
  }
}
