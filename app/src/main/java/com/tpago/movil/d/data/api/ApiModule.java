package com.tpago.movil.d.data.api;

import com.tpago.movil.d.DepQualifier;
import com.tpago.movil.d.domain.AccountBalance;
import com.tpago.movil.d.domain.Balance;
import com.tpago.movil.d.domain.BillRecipient;
import com.tpago.movil.d.domain.CreditCardBalance;
import com.tpago.movil.d.domain.InitialData;
import com.tpago.movil.d.domain.LoanBalance;
import com.tpago.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.d.domain.PhoneNumberRecipient;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.api.ApiError;
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
  Gson provideGson(Gson gson) {
    return new GsonBuilder()
      .setDateFormat("dd/MM/yyyy")
      .registerTypeAdapterFactory(new GsonTypeAdapterFactory(gson))
      .registerTypeAdapter(ApiError.class, new ApiErrorTypeAdapter())
      .registerTypeAdapter(InitialData.class, new InitialDataDeserializer())
      .registerTypeAdapter(Product.class, new ProductTypeAdapter())
      .registerTypeAdapter(Balance.class, new BalanceTypeAdapter())
      .registerTypeAdapter(AccountBalance.class, new BalanceTypeAdapter())
      .registerTypeAdapter(CreditCardBalance.class, new BalanceTypeAdapter())
      .registerTypeAdapter(LoanBalance.class, new BalanceTypeAdapter())
      .registerTypeAdapter(Recipient.class, new RecipientTypeAdapter())
      .registerTypeAdapter(PhoneNumberRecipient.class, new RecipientTypeAdapter())
      .registerTypeAdapter(NonAffiliatedPhoneNumberRecipient.class, new RecipientTypeAdapter())
      .registerTypeAdapter(BillRecipient.class, new RecipientTypeAdapter())
      .create();
  }
}
