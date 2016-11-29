package com.gbh.movil.data.api;

import com.gbh.movil.BuildConfig;
import com.gbh.movil.domain.Account;
import com.gbh.movil.domain.AccountBalance;
import com.gbh.movil.domain.Balance;
import com.gbh.movil.domain.Bank;
import com.gbh.movil.domain.CreditCard;
import com.gbh.movil.domain.CreditCardBalance;
import com.gbh.movil.domain.InitialData;
import com.gbh.movil.domain.Loan;
import com.gbh.movil.domain.LoanBalance;
import com.gbh.movil.domain.Product;
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
      .registerTypeAdapter(Transaction.class, new TransactionJsonDeserializer())
      .registerTypeAdapter(InitialData.class, new InitialDataDeserializer())
      .registerTypeAdapter(Product.class, new ProductTypeAdapter())
      .registerTypeAdapter(Account.class, new ProductTypeAdapter())
      .registerTypeAdapter(CreditCard.class, new ProductTypeAdapter())
      .registerTypeAdapter(Loan.class, new ProductTypeAdapter())
      .registerTypeAdapter(Balance.class, new BalanceTypeAdapter())
      .registerTypeAdapter(AccountBalance.class, new BalanceTypeAdapter())
      .registerTypeAdapter(CreditCardBalance.class, new BalanceTypeAdapter())
      .registerTypeAdapter(LoanBalance.class, new BalanceTypeAdapter())
      .create();
    final Retrofit retrofit = new Retrofit.Builder()
      .addConverterFactory(GsonConverterFactory.create(gson))
      .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
      .baseUrl(BuildConfig.API_URL)
      .client(okHttpClient)
      .build();
    return new DecoratedApiBridge(new FakeApiBridge());//new RetrofitApiBridge(retrofit.create(ApiService.class)));
  }
}
