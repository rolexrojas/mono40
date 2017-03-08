package com.tpago.movil;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class AccountPaymentMethod extends PaymentMethod<Account> {
  public static AccountPaymentMethod create(Account account) {
    return new AutoValue_AccountPaymentMethod(account);
  }

  public static TypeAdapter<AccountPaymentMethod> typeAdapter(Gson gson) {
    return new AutoValue_AccountPaymentMethod.GsonTypeAdapter(gson);
  }

  public abstract Account getProduct();
}
