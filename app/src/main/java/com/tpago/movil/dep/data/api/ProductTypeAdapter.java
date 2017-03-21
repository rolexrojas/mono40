package com.tpago.movil.dep.data.api;

import com.tpago.movil.api.Currencies;
import com.tpago.movil.dep.domain.Product;
import com.tpago.movil.dep.domain.ProductCreator;
import com.tpago.movil.dep.domain.ProductType;
import com.tpago.movil.Bank;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.tpago.movil.text.Texts;

import java.lang.reflect.Type;
import java.math.BigDecimal;

/**
 * @author hecvasro
 */
class ProductTypeAdapter implements JsonDeserializer<Product>, JsonSerializer<Product> {
  private static final String PROPERTY_TYPE = "account-type";
  private static final String PROPERTY_ALIAS = "account-alias";
  private static final String PROPERTY_NUMBER = "account-number";
  private static final String PROPERTY_BANK = "bank";
  private static final String PROPERTY_CURRENCY = "currency";
  private static final String PROPERTY_QUERY_FEE = "query-fee";
  private static final String PROPERTY_PAYMENT_OPTION = "payable";
  private static final String PROPERTY_IS_DEFAULT = "default-account";
  private static final String PROPERTY_IMAGE_URL = "image-url";

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
    } else {
      JsonElement je;
      je = jsonObject.get(PROPERTY_QUERY_FEE);
      final BigDecimal queryFee = je.isJsonNull() ? BigDecimal.ZERO : BigDecimal.valueOf(je.getAsDouble());
      je = jsonObject.get(PROPERTY_PAYMENT_OPTION);
      final boolean paymentOption = !json.isJsonNull() && je.getAsBoolean();
      je = jsonObject.get(PROPERTY_IS_DEFAULT);
      final boolean isDefault = !je.isJsonNull() && je.getAsBoolean();
      String imageUrl = null;
      if (jsonObject.has(PROPERTY_IMAGE_URL) && !jsonObject.get(PROPERTY_IMAGE_URL).isJsonNull()) {
        imageUrl = jsonObject.get(PROPERTY_IMAGE_URL).getAsString();
      }
      return ProductCreator.create(
        ProductType.valueOf(jsonObject.get(PROPERTY_TYPE).getAsString()),
        jsonObject.get(PROPERTY_ALIAS).getAsString(),
        jsonObject.get(PROPERTY_NUMBER).getAsString().replaceAll("[\\D]", ""),
        (Bank) context.deserialize(jsonObject.get(PROPERTY_BANK), Bank.class),
        Currencies.map(jsonObject.get(PROPERTY_CURRENCY).getAsString()),
        queryFee,
        paymentOption,
        isDefault,
        imageUrl);
    }
  }

  @Override
  public JsonElement serialize(Product src, Type typeOfSrc, JsonSerializationContext context) {
    final JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty(PROPERTY_TYPE, src.getType().name());
    jsonObject.addProperty(PROPERTY_ALIAS, src.getAlias());
    jsonObject.addProperty(PROPERTY_NUMBER, src.getNumber());
    jsonObject.add(PROPERTY_BANK, context.serialize(src.getBank(), Bank.class));
    jsonObject.addProperty(PROPERTY_CURRENCY, Currencies.map(src.getCurrency()));
    jsonObject.addProperty(PROPERTY_QUERY_FEE, src.getQueryFee());
    jsonObject.addProperty(PROPERTY_PAYMENT_OPTION, Product.isPaymentOption(src));
    jsonObject.addProperty(PROPERTY_IS_DEFAULT, Product.isDefaultPaymentOption(src));
    String imageUrl = src.getImageUriTemplate();
    if (Texts.isNotEmpty(imageUrl)) {
      jsonObject.addProperty(PROPERTY_IMAGE_URL, imageUrl);
    }
    return jsonObject;
  }
}
