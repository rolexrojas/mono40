package com.gbh.movil.data.api;

import com.gbh.movil.domain.Account;
import com.gbh.movil.domain.InitialData;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO
 *
 * @author hecvasro
 */
class InitialDataDeserializer implements JsonDeserializer<InitialData> {
  private static final String PROPERTY_QUERY = "query";
  private static final String PROPERTY_ACCOUNTS = "accounts";
  private static final String PROPERTY_CREDIT_CARDS = "creditCards";
  private static final String PROPERTY_LOANS = "loans";

  @Override
  public InitialData deserialize(JsonElement json, Type typeOfT,
    JsonDeserializationContext context) throws JsonParseException {
    final JsonObject jsonObject = json.getAsJsonObject();
    if (!jsonObject.has(PROPERTY_QUERY)) {
      throw new JsonParseException("Property '" + PROPERTY_QUERY + "' is missing");
    }
    final JsonObject queryJsonObject = jsonObject.get(PROPERTY_QUERY).getAsJsonObject();
    if (!queryJsonObject.isJsonObject()) {
      throw new JsonParseException(PROPERTY_QUERY + " must be an object");
    }
    if (!queryJsonObject.has(PROPERTY_ACCOUNTS)) {
      throw new JsonParseException("Property '" + PROPERTY_ACCOUNTS + "' is missing");
    }
    final JsonElement accountsJsonElement = queryJsonObject.get(PROPERTY_ACCOUNTS);
    if (!accountsJsonElement.isJsonArray()) {
      throw new JsonParseException(PROPERTY_ACCOUNTS + " must be an array");
    }
    if (!queryJsonObject.has(PROPERTY_CREDIT_CARDS)) {
      throw new JsonParseException("Property '" + PROPERTY_CREDIT_CARDS + "' is missing");
    }
    final JsonElement creditCardsJsonElement = queryJsonObject.get(PROPERTY_CREDIT_CARDS);
    if (!creditCardsJsonElement.isJsonArray()) {
      throw new JsonParseException(PROPERTY_CREDIT_CARDS + " must be an array");
    }
    if (!queryJsonObject.has(PROPERTY_LOANS)) {
      throw new JsonParseException("Property '" + PROPERTY_LOANS + "' is missing");
    }
    final JsonElement loansJsonElement = queryJsonObject.get(PROPERTY_LOANS);
    if (!loansJsonElement.isJsonArray()) {
      throw new JsonParseException(PROPERTY_CREDIT_CARDS + " must be an array");
    }
    final Set<Account> accounts = new HashSet<>();
    final JsonArray accountsArray = accountsJsonElement.getAsJsonArray();
    for (int i = 0; i < accountsArray.size(); i++) {
      accounts.add((Account) context.deserialize(accountsArray.get(i).getAsJsonObject(),
        Account.class));
    }
    final JsonArray creditCardsArray = creditCardsJsonElement.getAsJsonArray();
    for (int i = 0; i < creditCardsArray.size(); i++) {
      accounts.add((Account) context.deserialize(creditCardsArray.get(i).getAsJsonObject(),
        Account.class));
    }
    final JsonArray loansArray = loansJsonElement.getAsJsonArray();
    for (int i = 0; i < loansArray.size(); i++) {
      accounts.add((Account) context.deserialize(loansArray.get(i).getAsJsonObject(),
        Account.class));
    }
    return new InitialData(accounts);
  }
}
