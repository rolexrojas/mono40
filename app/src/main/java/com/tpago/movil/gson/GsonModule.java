package com.tpago.movil.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.tpago.movil.company.LogoCatalogMapper;
import com.tpago.movil.d.data.api.ApiErrorTypeAdapter;
import com.tpago.movil.d.data.api.BalanceTypeAdapter;
import com.tpago.movil.d.data.api.InitialDataDeserializer;
import com.tpago.movil.d.data.api.ProductTypeAdapter;
import com.tpago.movil.d.data.api.RecipientTypeAdapter;
import com.tpago.movil.d.domain.AccountBalance;
import com.tpago.movil.d.domain.Balance;
import com.tpago.movil.d.domain.Bank;
import com.tpago.movil.d.domain.BillRecipient;
import com.tpago.movil.d.domain.CreditCardBalance;
import com.tpago.movil.d.domain.InitialData;
import com.tpago.movil.d.domain.LoanBalance;
import com.tpago.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.d.domain.PhoneNumberRecipient;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.api.ApiError;
import com.tpago.movil.dep.data.AssetUriBuilder;
import com.tpago.movil.util.Placeholder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class GsonModule {

  @Provides
  @Singleton
  Gson gson(LogoCatalogMapper logoCatalogMapper, AssetUriBuilder assetUriBuilder) {
    final TypeAdapter<Bank> bankTypeAdapter = new BankTypeAdapter(assetUriBuilder);

    return new GsonBuilder()
      .setDateFormat("dd/MM/yyyy")
      .registerTypeAdapter(Placeholder.class, PlaceholderTypeAdapter.create())
      .registerTypeAdapter(Bank.class, bankTypeAdapter)
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
      .registerTypeAdapterFactory(DecoratedAutoValueTypeAdapterFactory.create(logoCatalogMapper))
      .create();
  }
}
