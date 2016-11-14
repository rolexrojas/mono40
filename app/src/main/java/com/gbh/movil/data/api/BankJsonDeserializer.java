package com.gbh.movil.data.api;

import com.gbh.movil.domain.Bank;
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
class BankJsonDeserializer implements JsonDeserializer<Bank> {
  private static final String PROPERTY_BANK_PARTNER_CODE = "bankPartnerCode";
  private static final String PROPERTY_BANK_PARTNER_ID = "bankPartnerId";
  private static final String PROPERTY_BANK_PARTNER_NAME = "bankPartnerName";
  private static final String PROPERTY_BANK_PARTNER_STATE = "active";

  @Override
  public Bank deserialize(JsonElement json, Type typeOfT,
    JsonDeserializationContext context) throws JsonParseException {
    final JsonObject jsonObject = json.getAsJsonObject();
    if (!jsonObject.has(PROPERTY_BANK_PARTNER_CODE)) {
      throw new JsonParseException("Property '" + PROPERTY_BANK_PARTNER_CODE + "' is missing");
    } else if (!jsonObject.has(PROPERTY_BANK_PARTNER_ID)) {
      throw new JsonParseException("Property '" + PROPERTY_BANK_PARTNER_ID + "' is missing");
    } else if (!jsonObject.has(PROPERTY_BANK_PARTNER_NAME)) {
      throw new JsonParseException("Property '" + PROPERTY_BANK_PARTNER_NAME + "' is missing");
    } else {
      final Bank bank = new Bank(
        jsonObject.get(PROPERTY_BANK_PARTNER_CODE).getAsInt(),
        jsonObject.get(PROPERTY_BANK_PARTNER_ID).getAsString(),
        jsonObject.get(PROPERTY_BANK_PARTNER_NAME).getAsString()
      );
      if (jsonObject.has(PROPERTY_BANK_PARTNER_STATE)) {
        bank.setState(jsonObject.get(PROPERTY_BANK_PARTNER_STATE).getAsBoolean());
      }
      return bank;
    }
  }
}
