package com.gbh.movil.data.repo;

import android.content.Context;

import com.gbh.movil.domain.ProductRepo;
import com.gbh.movil.domain.RecipientRepo;
import com.gbh.movil.domain.TransactionRepo;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public class RepoModule {
  @Provides
  @Singleton
  ProductRepo provideProductRepo(Context context, Gson gson) {
    return new SharedPreferencesProductRepo(
      context.getSharedPreferences(ProductRepo.class.getCanonicalName(), Context.MODE_PRIVATE),
      gson);
  }

  @Provides
  @Singleton
  RecipientRepo provideRecipientRepository(Context context, Gson gson) {
    return new SharedPreferencesRecipientRepo(
      context.getSharedPreferences(RecipientRepo.class.getCanonicalName(), Context.MODE_PRIVATE),
      gson);
  }

  @Provides
  @Singleton
  TransactionRepo provideTransactionRepository(Context context, Gson gson) {
    return new SharedPreferencesTransactionRepo(
      context.getSharedPreferences(TransactionRepo.class.getCanonicalName(), Context.MODE_PRIVATE),
      gson);
  }
}
