package com.tpago.movil;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class Account extends Product {
  public static Account create(Bank bank, Type type, String alias, String number, String currency) {
    return new AutoValue_Account(bank, type, alias, number, currency);
  }

  public static TypeAdapter<Account> typeAdapter(Gson gson) {
    return new AutoValue_Account.GsonTypeAdapter(gson);
  }
}
