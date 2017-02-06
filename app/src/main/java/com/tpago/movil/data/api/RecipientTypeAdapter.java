package com.tpago.movil.data.api;

import android.text.TextUtils;

import com.tpago.movil.domain.ContactRecipient;
import com.tpago.movil.domain.PhoneNumberRecipient;
import com.tpago.movil.domain.Recipient;
import com.tpago.movil.domain.RecipientType;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * @author hecvasro
 */
class RecipientTypeAdapter implements JsonDeserializer<Recipient>, JsonSerializer<Recipient> {
  private static final String PROPERTY_TYPE = "type";
  private static final String PROPERTY_LABEL = "label";
  private static final String PROPERTY_PHONE_NUMBER = "phoneNumber";

  @Override
  public Recipient deserialize(
    JsonElement json,
    Type typeOfT,
    JsonDeserializationContext context) throws JsonParseException {
    final JsonObject jo = json.getAsJsonObject();
    if (!jo.has(PROPERTY_TYPE)) {
      throw new JsonParseException("Property '" + PROPERTY_TYPE + "' is missing");
    } else if (!jo.has(PROPERTY_PHONE_NUMBER)) {
      throw new JsonParseException("Property '" + PROPERTY_PHONE_NUMBER + "' is missing");
    } else {
      final RecipientType type = RecipientType.valueOf(jo.get(PROPERTY_TYPE).getAsString());
      final String phoneNumber = jo.get(PROPERTY_PHONE_NUMBER).getAsString();
      final String label = jo.has(PROPERTY_LABEL) ? jo.get(PROPERTY_LABEL).getAsString() : null;
      if (type.equals(RecipientType.PHONE_NUMBER)) {
        return new PhoneNumberRecipient(phoneNumber, label);
      } else {
        return new ContactRecipient(phoneNumber, label);
      }
    }
  }

  @Override
  public JsonElement serialize(Recipient src, Type typeOfSrc, JsonSerializationContext context) {
    final JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty(PROPERTY_TYPE, src.getType().toString());
    if (!TextUtils.isEmpty(src.getLabel())) {
      jsonObject.addProperty(PROPERTY_LABEL, src.getLabel());
    }
    jsonObject.addProperty(PROPERTY_PHONE_NUMBER, src.getIdentifier());
    return jsonObject;
  }
}
