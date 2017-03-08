package com.tpago.movil;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class CreditCard extends Product {
  public static CreditCard create(
    Bank bank,
    Type type,
    String alias,
    String number,
    String currency) {
    return new AutoValue_CreditCard(bank, type, alias, number, currency);
  }

  public static TypeAdapter<CreditCard> typeAdapter(Gson gson) {
    return new AutoValue_CreditCard.GsonTypeAdapter(gson);
  }
}
