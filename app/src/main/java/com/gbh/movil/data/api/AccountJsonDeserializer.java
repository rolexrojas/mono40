package com.gbh.movil.data.api;

import com.gbh.movil.domain.Account;
import com.gbh.movil.domain.AccountType;
import com.gbh.movil.domain.Bank;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * @author hecvasro
 */
class AccountJsonDeserializer implements JsonDeserializer<Account> {
  private static final String PROPERTY_ACCOUNT_TYPE = "accountType";
  private static final String PROPERTY_ACCOUNT_ALIAS = "accountAlias";
  private static final String PROPERTY_ACCOUNT_NUMBER = "accountNumber";
  private static final String PROPERTY_ACCOUNT_BANK = "bank";
  private static final String PROPERTY_ACCOUNT_CURRENCY = "currency";
  private static final String PROPERTY_ACCOUNT_QUERY_FEE = "queryFee";

  @Override
  public Account deserialize(JsonElement json, Type typeOfT,
    JsonDeserializationContext context) throws JsonParseException {
    final JsonObject jsonObject = json.getAsJsonObject();
    if (!jsonObject.has(PROPERTY_ACCOUNT_TYPE)) {
      throw new JsonParseException("Property '" + PROPERTY_ACCOUNT_TYPE + "' is missing");
    } else if (!jsonObject.has(PROPERTY_ACCOUNT_ALIAS)) {
      throw new JsonParseException("Property '" + PROPERTY_ACCOUNT_ALIAS + "' is missing");
    } else if (!jsonObject.has(PROPERTY_ACCOUNT_NUMBER)) {
      throw new JsonParseException("Property '" + PROPERTY_ACCOUNT_NUMBER + "' is missing");
    } else if (!jsonObject.has(PROPERTY_ACCOUNT_BANK)) {
      throw new JsonParseException("Property '" + PROPERTY_ACCOUNT_BANK + "' is missing");
    } else if (!jsonObject.has(PROPERTY_ACCOUNT_CURRENCY)) {
      throw new JsonParseException("Property '" + PROPERTY_ACCOUNT_CURRENCY + "' is missing");
    } else if (!jsonObject.has(PROPERTY_ACCOUNT_QUERY_FEE)) {
      throw new JsonParseException("Property '" + PROPERTY_ACCOUNT_QUERY_FEE + "' is missing");
    } else {
      return new Account(AccountType.valueOf(jsonObject.get(PROPERTY_ACCOUNT_TYPE).getAsString()),
        jsonObject.get(PROPERTY_ACCOUNT_ALIAS).getAsString(),
        jsonObject.get(PROPERTY_ACCOUNT_NUMBER).getAsString(),
        (Bank) context.deserialize(jsonObject.get(PROPERTY_ACCOUNT_BANK), Bank.class),
        jsonObject.get(PROPERTY_ACCOUNT_CURRENCY).getAsString(),
        jsonObject.get(PROPERTY_ACCOUNT_QUERY_FEE).getAsDouble());
    }
  }
}
