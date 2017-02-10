package com.tpago.movil.d.data.api;

import com.tpago.movil.d.domain.AccountBalance;
import com.tpago.movil.d.domain.Balance;
import com.tpago.movil.d.domain.CreditCardBalance;
import com.tpago.movil.d.domain.LoanBalance;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * TODO
 *
 * @author hecvasro
 */
class BalanceTypeAdapter implements JsonDeserializer<Balance> {
  private static final String PROPERTY_AVAILABLE = "available";
  private static final String PROPERTY_BALANCE = "balance";
  private static final String PROPERTY_FEE = "fee";
  private static final String PROPERTY_TOTAL = "total";

  @Override
  public Balance deserialize(JsonElement json, Type typeOfT,
    JsonDeserializationContext context) throws JsonParseException {
    final JsonObject jsonObject = json.getAsJsonObject();
    if (AccountBalance.class.isAssignableFrom((Class<?>) typeOfT)) {
      if (!jsonObject.has(PROPERTY_AVAILABLE)) {
        throw new JsonParseException("Property '" + PROPERTY_AVAILABLE + "' is missing");
      } else if (!jsonObject.has(PROPERTY_TOTAL)) {
        throw new JsonParseException("Property '" + PROPERTY_TOTAL + "' is missing");
      } else {
        return new AccountBalance(jsonObject.get(PROPERTY_TOTAL).getAsBigDecimal(),
          jsonObject.get(PROPERTY_AVAILABLE).getAsBigDecimal());
      }
    } else if (CreditCardBalance.class.isAssignableFrom((Class<?>) typeOfT)) {
      if (!jsonObject.has(PROPERTY_AVAILABLE)) {
        throw new JsonParseException("Property '" + PROPERTY_AVAILABLE + "' is missing");
      } else if (!jsonObject.has(PROPERTY_TOTAL)) {
        throw new JsonParseException("Property '" + PROPERTY_TOTAL + "' is missing");
      } else {
        return new CreditCardBalance(jsonObject.get(PROPERTY_TOTAL).getAsBigDecimal(),
          jsonObject.get(PROPERTY_AVAILABLE).getAsBigDecimal());
      }
    } else {
      if (!jsonObject.has(PROPERTY_BALANCE)) {
        throw new JsonParseException("Property '" + PROPERTY_BALANCE + "' is missing");
      } else if (!jsonObject.has(PROPERTY_FEE)) {
        throw new JsonParseException("Property '" + PROPERTY_FEE + "' is missing");
      } else {
        return new LoanBalance(jsonObject.get(PROPERTY_BALANCE).getAsBigDecimal(),
          jsonObject.get(PROPERTY_FEE).getAsBigDecimal());
      }
    }
  }
}
