package com.tpago.movil.d.data.repo;

import android.content.Context;

import com.tpago.movil.d.domain.TransactionRepo;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Deprecated
@Module
public class RepoModule {
  @Provides
  @Singleton
  TransactionRepo provideTransactionRepository(Context context, Gson gson) {
    return new SharedPreferencesTransactionRepo(
      context.getSharedPreferences(TransactionRepo.class.getCanonicalName(), Context.MODE_PRIVATE),
      gson);
  }
}
