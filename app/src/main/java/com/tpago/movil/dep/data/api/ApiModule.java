package com.tpago.movil.dep.data.api;

import com.tpago.movil.app.AppTypeAdapterFactory;
import com.tpago.movil.dep.DepQualifier;
import com.tpago.movil.dep.domain.AccountBalance;
import com.tpago.movil.dep.domain.Balance;
import com.tpago.movil.dep.domain.CreditCardBalance;
import com.tpago.movil.dep.domain.InitialData;
import com.tpago.movil.dep.domain.LoanBalance;
import com.tpago.movil.dep.domain.PhoneNumberRecipient;
import com.tpago.movil.dep.domain.Product;
import com.tpago.movil.dep.domain.Recipient;
import com.tpago.movil.dep.domain.Transaction;
import com.tpago.movil.dep.domain.api.ApiError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Deprecated
@Module(includes = ApiFlavorModule.class)
public class ApiModule {
  @Provides
  @Singleton
  @DepQualifier
  Gson provideGson() {
    return new GsonBuilder()
      .setDateFormat("dd/MM/yyyy")
      .registerTypeAdapterFactory(AppTypeAdapterFactory.create())
      .registerTypeAdapter(ApiError.class, new ApiErrorTypeAdapter())
      .registerTypeAdapter(Transaction.class, new TransactionJsonDeserializer())
      .registerTypeAdapter(InitialData.class, new InitialDataDeserializer())
      .registerTypeAdapter(Product.class, new ProductTypeAdapter())
      .registerTypeAdapter(Balance.class, new BalanceTypeAdapter())
      .registerTypeAdapter(AccountBalance.class, new BalanceTypeAdapter())
      .registerTypeAdapter(CreditCardBalance.class, new BalanceTypeAdapter())
      .registerTypeAdapter(LoanBalance.class, new BalanceTypeAdapter())
      .registerTypeAdapter(Recipient.class, new RecipientTypeAdapter())
      .registerTypeAdapter(PhoneNumberRecipient.class, new RecipientTypeAdapter())
      .create();
  }
}
