package com.tpago.movil.dep.data.api;

import com.tpago.movil.BuildConfig;
import com.tpago.movil.dep.domain.Account;
import com.tpago.movil.dep.domain.AccountBalance;
import com.tpago.movil.dep.domain.Balance;
import com.tpago.movil.dep.domain.Bank;
import com.tpago.movil.dep.domain.ContactRecipient;
import com.tpago.movil.dep.domain.CreditCard;
import com.tpago.movil.dep.domain.CreditCardBalance;
import com.tpago.movil.dep.domain.InitialData;
import com.tpago.movil.dep.domain.Loan;
import com.tpago.movil.dep.domain.LoanBalance;
import com.tpago.movil.dep.domain.PhoneNumberRecipient;
import com.tpago.movil.dep.domain.Product;
import com.tpago.movil.dep.domain.Recipient;
import com.tpago.movil.dep.domain.Transaction;
import com.tpago.movil.dep.domain.api.ApiBridge;
import com.tpago.movil.dep.domain.api.ApiError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;

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
  Gson provideGson() {
    return new GsonBuilder()
      .registerTypeAdapter(ApiError.class, new ApiErrorTypeAdapter())
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
      .registerTypeAdapter(Recipient.class, new RecipientTypeAdapter())
      .registerTypeAdapter(PhoneNumberRecipient.class, new RecipientTypeAdapter())
      .registerTypeAdapter(ContactRecipient.class, new RecipientTypeAdapter())
      .create();
  }

  @Provides
  @Singleton
  Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
    return new Retrofit.Builder()
      .addConverterFactory(GsonConverterFactory.create(gson))
      .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
      .baseUrl(Api.URL)
      .client(okHttpClient)
      .build();
  }

  @Provides
  @Singleton
  ApiBridge provideApiBridge(Retrofit retrofit) {
    return new RetrofitApiBridge(
      retrofit.create(ApiService.class),
      retrofit.<ApiError>responseBodyConverter(ApiError.class, new Annotation[0]));
  }
}
