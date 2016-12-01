package com.gbh.movil.data.api;

import com.gbh.movil.domain.Product;
import com.gbh.movil.domain.ProductCreator;
import com.gbh.movil.domain.ProductType;
import com.gbh.movil.domain.Bank;
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
class ProductTypeAdapter implements JsonDeserializer<Product>, JsonSerializer<Product> {
  private static final String PROPERTY_TYPE = "accountType";
  private static final String PROPERTY_ALIAS = "accountAlias";
  private static final String PROPERTY_NUMBER = "accountNumber";
  private static final String PROPERTY_BANK = "bank";
  private static final String PROPERTY_CURRENCY = "currency";
  private static final String PROPERTY_QUERY_FEE = "queryFee";
  private static final String PROPERTY_PAYMENT_OPTION = "payable";

  @Override
  public Product deserialize(JsonElement json, Type typeOfT,
    JsonDeserializationContext context) throws JsonParseException {
    final JsonObject jsonObject = json.getAsJsonObject();
    if (!jsonObject.has(PROPERTY_TYPE)) {
      throw new JsonParseException("Property '" + PROPERTY_TYPE + "' is missing");
    } else if (!jsonObject.has(PROPERTY_ALIAS)) {
      throw new JsonParseException("Property '" + PROPERTY_ALIAS + "' is missing");
    } else if (!jsonObject.has(PROPERTY_NUMBER)) {
      throw new JsonParseException("Property '" + PROPERTY_NUMBER + "' is missing");
    } else if (!jsonObject.has(PROPERTY_BANK)) {
      throw new JsonParseException("Property '" + PROPERTY_BANK + "' is missing");
    } else if (!jsonObject.has(PROPERTY_CURRENCY)) {
      throw new JsonParseException("Property '" + PROPERTY_CURRENCY + "' is missing");
    } else if (!jsonObject.has(PROPERTY_QUERY_FEE)) {
      throw new JsonParseException("Property '" + PROPERTY_QUERY_FEE + "' is missing");
    } else if (!jsonObject.has(PROPERTY_PAYMENT_OPTION)) {
      throw new JsonParseException("Property '" + PROPERTY_PAYMENT_OPTION + "' is missing");
    } else {
      return ProductCreator.create(
        ProductType.valueOf(jsonObject.get(PROPERTY_TYPE).getAsString()),
        jsonObject.get(PROPERTY_ALIAS).getAsString(),
        jsonObject.get(PROPERTY_NUMBER).getAsString(),
        (Bank) context.deserialize(jsonObject.get(PROPERTY_BANK), Bank.class),
        jsonObject.get(PROPERTY_CURRENCY).getAsString(),
        jsonObject.get(PROPERTY_QUERY_FEE).getAsBigDecimal(),
        jsonObject.get(PROPERTY_PAYMENT_OPTION).getAsBoolean());
    }
  }

  @Override
  public JsonElement serialize(Product src, Type typeOfSrc, JsonSerializationContext context) {
    final JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty(PROPERTY_TYPE, src.getType().name());
    jsonObject.addProperty(PROPERTY_ALIAS, src.getAlias());
    jsonObject.addProperty(PROPERTY_NUMBER, src.getNumber());
    jsonObject.add(PROPERTY_BANK, context.serialize(src.getBank(), Bank.class));
    jsonObject.addProperty(PROPERTY_CURRENCY, src.getCurrency());
    jsonObject.addProperty(PROPERTY_QUERY_FEE, src.getQueryFee());
    return jsonObject;
  }
}
