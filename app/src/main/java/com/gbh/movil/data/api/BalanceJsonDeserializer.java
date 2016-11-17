package com.gbh.movil.data.api;

import com.gbh.movil.domain.Balance;
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
class BalanceJsonDeserializer implements JsonDeserializer<Balance> {
  private static final String PROPERTY_TOTAL = "total";
  private static final String PROPERTY_FEE = "fee";
  private static final String PROPERTY_AVAILABLE = "available";
  private static final String PROPERTY_DESCRIPTION = "balanceDesc";

  @Override
  public Balance deserialize(JsonElement json, Type typeOfT,
    JsonDeserializationContext context) throws JsonParseException {
    final JsonObject jsonObject = json.getAsJsonObject();
    if (!jsonObject.has(PROPERTY_TOTAL) && !jsonObject.has(PROPERTY_FEE)) {
      throw new JsonParseException("Property '" + PROPERTY_TOTAL + "' is missing");
    } else if (!jsonObject.has(PROPERTY_AVAILABLE)) {
      throw new JsonParseException("Property '" + PROPERTY_AVAILABLE + "' is missing");
    } else if (!jsonObject.has(PROPERTY_DESCRIPTION)) {
      throw new JsonParseException("Property '" + PROPERTY_DESCRIPTION + "' is missing");
    } else {
      return new Balance(jsonObject.get((jsonObject.has(PROPERTY_TOTAL) ? PROPERTY_TOTAL
        : PROPERTY_FEE)).getAsBigDecimal(), jsonObject.get(PROPERTY_AVAILABLE).getAsBigDecimal(),
        jsonObject.get(PROPERTY_DESCRIPTION).getAsString());
    }
  }
}
