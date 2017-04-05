package com.tpago.movil;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.tpago.movil.domain.Bank;

import java.math.BigDecimal;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class Account extends Product {
  public static Account create(
    Bank bank,
    Type type,
    String alias,
    String number,
    String currency,
    BigDecimal queryFee) {
    return new AutoValue_Account(bank, type, alias, number, currency, queryFee);
  }

  public static TypeAdapter<Account> typeAdapter(Gson gson) {
    return new AutoValue_Account.GsonTypeAdapter(gson)
      .setDefaultQueryFee(BigDecimal.ZERO);
  }
}
