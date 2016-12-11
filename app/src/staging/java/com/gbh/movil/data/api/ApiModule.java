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

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author hecvasro
 */
@Module
public class ApiModule {
  /**
   * TODO
   */
  private static final String URL = "http://172.19.1.103:8081/api/neo/";

  @Provides
  @Singleton
  OkHttpClient provideOkHttpClient() {
    final OkHttpClient.Builder builder = new OkHttpClient.Builder()
      .addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
          final Request.Builder builder = chain.request().newBuilder()
            .addHeader(Api.Header.USER_AGENT, System.getProperty("http.agent"));
          return chain.proceed(builder.build());
        }
      });
    if (BuildConfig.DEBUG) {
      final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
      interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
      builder.addInterceptor(interceptor);
    }
    return builder.build();
  }

  @Provides
  @Singleton
  Retrofit provideRetrofit(OkHttpClient okHttpClient) {
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
    return new Retrofit.Builder()
      .addConverterFactory(GsonConverterFactory.create(gson))
      .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
      .baseUrl(URL)
      .client(okHttpClient)
      .build();
  }

  @Provides
  @Singleton
  ApiBridge provideApiBridge(Retrofit retrofit) {
    return new DecoratedApiBridge(new RetrofitApiBridge(retrofit.create(ApiService.class)));
  }
}
