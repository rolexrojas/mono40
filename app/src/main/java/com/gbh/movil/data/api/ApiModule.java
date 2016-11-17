package com.gbh.movil.data.api;

import com.gbh.movil.BuildConfig;
import com.gbh.movil.domain.Account;
import com.gbh.movil.domain.Balance;
import com.gbh.movil.domain.Bank;
import com.gbh.movil.domain.CreditCardAccount;
import com.gbh.movil.domain.ElectronicAccount;
import com.gbh.movil.domain.InitialData;
import com.gbh.movil.domain.LoanAccount;
import com.gbh.movil.domain.Transaction;
import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.domain.api.DecoratedApiBridge;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author hecvasro
 */
@Module
public class ApiModule {
  @Provides
  @Singleton
  ApiBridge provideApiBridge(OkHttpClient okHttpClient) {
    final Gson gson = new GsonBuilder()
      .registerTypeAdapter(Bank.class, new BankTypeAdapter())
      .registerTypeAdapter(Account.class, new AccountTypeAdapter())
      .registerTypeAdapter(ElectronicAccount.class, new AccountTypeAdapter())
      .registerTypeAdapter(CreditCardAccount.class, new AccountTypeAdapter())
      .registerTypeAdapter(LoanAccount.class, new AccountTypeAdapter())
      .registerTypeAdapter(Balance.class, new BalanceJsonDeserializer())
      .registerTypeAdapter(Transaction.class, new TransactionJsonDeserializer())
      .registerTypeAdapter(InitialData.class, new InitialDataDeserializer())
      .create();
    final Retrofit retrofit = new Retrofit.Builder()
      .addConverterFactory(GsonConverterFactory.create(gson))
      .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
      .baseUrl(BuildConfig.API_URL)
      .client(okHttpClient)
      .build();
    return new DecoratedApiBridge(new RetrofitApiBridge(retrofit.create(ApiService.class)));
  }
}
