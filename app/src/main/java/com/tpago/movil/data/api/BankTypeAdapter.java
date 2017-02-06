package com.tpago.movil.data.api;

import com.tpago.movil.domain.Bank;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * TODO
 *
 * @author hecvasro
 */
class BankTypeAdapter implements JsonDeserializer<Bank>, JsonSerializer<Bank> {
  private static final String PROPERTY_CODE = "bankPartnerCode";
  private static final String PROPERTY_ID = "bankPartnerId";
  private static final String PROPERTY_NAME = "bankPartnerName";
  private static final String PROPERTY_STATE = "active";

  @Override
  public Bank deserialize(JsonElement json, Type typeOfT,
    JsonDeserializationContext context) throws JsonParseException {
    final JsonObject jsonObject = json.getAsJsonObject();
    if (!jsonObject.has(PROPERTY_CODE)) {
      throw new JsonParseException("Property '" + PROPERTY_CODE + "' is missing");
    } else if (!jsonObject.has(PROPERTY_ID)) {
      throw new JsonParseException("Property '" + PROPERTY_ID + "' is missing");
    } else if (!jsonObject.has(PROPERTY_NAME)) {
      throw new JsonParseException("Property '" + PROPERTY_NAME + "' is missing");
    } else {
      final Bank bank = new Bank(
        jsonObject.get(PROPERTY_CODE).getAsInt(),
        jsonObject.get(PROPERTY_ID).getAsString(),
        jsonObject.get(PROPERTY_NAME).getAsString());
      bank.setState(jsonObject.has(PROPERTY_STATE)
        && jsonObject.get(PROPERTY_STATE).getAsBoolean());
      return bank;
    }
  }

  @Override
  public JsonElement serialize(Bank src, Type typeOfSrc, JsonSerializationContext context) {
    final JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty(PROPERTY_CODE, src.getCode());
    jsonObject.addProperty(PROPERTY_ID, src.getId());
    jsonObject.addProperty(PROPERTY_NAME, src.getName());
    jsonObject.addProperty(PROPERTY_STATE, src.getState());
    return jsonObject;
  }
}
