package com.tpago.movil;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class Loan extends Product {
  public static Loan create(Bank bank, Type type, String alias, String number, String currency) {
    return new AutoValue_Loan(bank, type, alias, number, currency);
  }

  public static TypeAdapter<Loan> typeAdapter(Gson gson) {
    return new AutoValue_Loan.GsonTypeAdapter(gson);
  }
}
