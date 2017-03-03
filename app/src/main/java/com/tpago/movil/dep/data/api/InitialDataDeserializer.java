package com.tpago.movil.dep.data.api;

import com.tpago.movil.dep.domain.Product;
import com.tpago.movil.dep.domain.InitialData;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.tpago.movil.dep.domain.Recipient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author hecvasro
 */
class InitialDataDeserializer implements JsonDeserializer<InitialData> {
  private static final String PROPERTY_QUERY = "query";
  private static final String PROPERTY_ACCOUNTS = "accounts";
  private static final String PROPERTY_CREDIT_CARDS = "credit-cards";
  private static final String PROPERTY_LOANS = "loans";
  private static final String PROPERTY_PAYMENT = "payment";
  private static final String PROPERTY_INVOICES = "invoices";

  @Override
  public InitialData deserialize(
    JsonElement json,
    Type typeOfT,
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
    final List<Product> products = new ArrayList<>();
    final JsonArray accountsArray = accountsJsonElement.getAsJsonArray();
    for (int i = 0; i < accountsArray.size(); i++) {
      products.add((Product) context.deserialize(accountsArray.get(i).getAsJsonObject(),
        Product.class));
    }
    final JsonArray creditCardsArray = creditCardsJsonElement.getAsJsonArray();
    for (int i = 0; i < creditCardsArray.size(); i++) {
      products.add((Product) context.deserialize(creditCardsArray.get(i).getAsJsonObject(),
        Product.class));
    }
    final JsonArray loansArray = loansJsonElement.getAsJsonArray();
    for (int i = 0; i < loansArray.size(); i++) {
      products.add((Product) context.deserialize(loansArray.get(i).getAsJsonObject(),
        Product.class));
    }
    final List<Recipient> recipientList = new ArrayList<>();
    if (jsonObject.has(PROPERTY_PAYMENT)) {
      final JsonObject paymentJsonElement = jsonObject.getAsJsonObject(PROPERTY_PAYMENT);
      if (paymentJsonElement.has(PROPERTY_INVOICES)) {
        final JsonArray ia = paymentJsonElement.getAsJsonArray(PROPERTY_INVOICES);
        for (int i = 0; i < ia.size(); i++) {
          final BillResponseBody brp = context.deserialize(
            ia.get(i)
              .getAsJsonObject(),
            BillResponseBody.class);
          recipientList.add(BillResponseBody.map(brp));
        }
      }
    }
    return new InitialData(products, recipientList);
  }
}
