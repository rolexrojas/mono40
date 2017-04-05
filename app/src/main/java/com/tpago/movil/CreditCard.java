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
public abstract class CreditCard extends Product {
  public static CreditCard create(
    Bank bank,
    Type type,
    String alias,
    String number,
    String currency,
    BigDecimal queryFee) {
    return new AutoValue_CreditCard(
      bank,
      type,
      alias,
      number,
      currency,
      queryFee);
  }

  public static TypeAdapter<CreditCard> typeAdapter(Gson gson) {
    return new AutoValue_CreditCard.GsonTypeAdapter(gson)
      .setDefaultQueryFee(BigDecimal.ZERO);
  }
}
