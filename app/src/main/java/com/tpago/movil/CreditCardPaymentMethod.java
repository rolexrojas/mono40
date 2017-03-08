package com.tpago.movil;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class CreditCardPaymentMethod extends PaymentMethod<CreditCard> {
  public static CreditCardPaymentMethod create(CreditCard product) {
    return new AutoValue_CreditCardPaymentMethod(product);
  }

  public static TypeAdapter<CreditCardPaymentMethod> typeAdapter(Gson gson) {
    return new AutoValue_CreditCardPaymentMethod.GsonTypeAdapter(gson);
  }

  public abstract CreditCard getProduct();
}
