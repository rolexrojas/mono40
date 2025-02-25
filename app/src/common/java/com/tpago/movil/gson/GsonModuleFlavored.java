package com.tpago.movil.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
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
import com.tpago.movil.util.Placeholder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class GsonModuleFlavored {

  @Provides
  @Singleton
  TypeAdapterFactory typeAdapterFactory() {
    return TypeAdapterFactoryFlavored.builder()
      .factory(TypeAdapterFactoryDeprecated.create())
      .build();
  }

  @Provides
  @Singleton
  Gson gson(TypeAdapterFactory typeAdapterFactory) {
    return new GsonBuilder()
      .setDateFormat("dd/MM/yyyy")
      .registerTypeAdapter(ApiError.class, new TypeAdapterApiErrorDeprecated())
      .registerTypeAdapter(InitialData.class, new TypeAdapterInitialDataDeprecated())
      .registerTypeAdapter(Product.class, new TypeAdapterProductDeprecated())
      .registerTypeAdapter(Balance.class, new TypeAdapterBalanceDeprecated())
      .registerTypeAdapter(AccountBalance.class, new TypeAdapterBalanceDeprecated())
      .registerTypeAdapter(CreditCardBalance.class, new TypeAdapterBalanceDeprecated())
      .registerTypeAdapter(LoanBalance.class, new TypeAdapterBalanceDeprecated())
      .registerTypeAdapter(Recipient.class, new TypeAdapterRecipientDeprecated())
      .registerTypeAdapter(PhoneNumberRecipient.class, new TypeAdapterRecipientDeprecated())
      .registerTypeAdapter(
        NonAffiliatedPhoneNumberRecipient.class,
        new TypeAdapterRecipientDeprecated()
      )
      .registerTypeAdapter(BillRecipient.class, new TypeAdapterRecipientDeprecated())
      .registerTypeAdapter(Placeholder.class, TypeAdapterPlaceholder.create())
      .registerTypeAdapterFactory(typeAdapterFactory)
      .create();
  }
}
