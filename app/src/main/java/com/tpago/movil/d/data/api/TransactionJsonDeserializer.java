package com.tpago.movil.d.data.api;

import com.tpago.movil.api.DCurrencies;
import com.tpago.movil.d.domain.Transaction;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * @author hecvasro
 */
class TransactionJsonDeserializer implements JsonDeserializer<Transaction> {
  private static final String PROPERTY_TYPE = "type";
  private static final String PROPERTY_NAME = "name";
  private static final String PROPERTY_DATE = "date";
  private static final String PROPERTY_REQUEST_TYPE = "requestType";
  private static final String PROPERTY_CURRENCY = "currency";
  private static final String PROPERTY_VALUE = "value";

  @Override
  public Transaction deserialize(JsonElement json, Type typeOfT,
    JsonDeserializationContext context) throws JsonParseException {
    final JsonObject jsonObject = json.getAsJsonObject();
    if (!jsonObject.has(PROPERTY_TYPE)) {
      throw new JsonParseException("Property '" + PROPERTY_TYPE + "' is missing");
    } else if (!jsonObject.has(PROPERTY_NAME)) {
      throw new JsonParseException("Property '" + PROPERTY_NAME + "' is missing");
    } else if (!jsonObject.has(PROPERTY_DATE)) {
      throw new JsonParseException("Property '" + PROPERTY_DATE + "' is missing");
    } else if (!jsonObject.has(PROPERTY_REQUEST_TYPE)) {
      throw new JsonParseException("Property '" + PROPERTY_REQUEST_TYPE + "' is missing");
    } else if (!jsonObject.has(PROPERTY_CURRENCY)) {
      throw new JsonParseException("Property '" + PROPERTY_CURRENCY + "' is missing");
    } else if (!jsonObject.has(PROPERTY_VALUE)) {
      throw new JsonParseException("Property '" + PROPERTY_VALUE + "' is missing");
    } else {
      return new Transaction(
        jsonObject.get(PROPERTY_TYPE).getAsString(),
        jsonObject.get(PROPERTY_NAME).getAsString(),
        jsonObject.get(PROPERTY_DATE).getAsLong(),
        Transaction.RequestType.valueOf(jsonObject.get(PROPERTY_REQUEST_TYPE).getAsString()),
        DCurrencies.map(jsonObject.get(PROPERTY_CURRENCY).getAsString()),
        jsonObject.get(PROPERTY_VALUE).getAsBigDecimal());
    }
  }
}
