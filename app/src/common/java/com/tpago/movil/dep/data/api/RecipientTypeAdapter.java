package com.tpago.movil.dep.data.api;

import android.text.TextUtils;

import com.tpago.movil.Bank;
import com.tpago.movil.dep.domain.ContactRecipient;
import com.tpago.movil.dep.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.dep.domain.PhoneNumberRecipient;
import com.tpago.movil.dep.domain.Recipient;
import com.tpago.movil.dep.domain.RecipientType;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.tpago.movil.text.Texts;
import com.tpago.movil.util.Objects;

import java.lang.reflect.Type;

/**
 * @author hecvasro
 */
class RecipientTypeAdapter implements JsonDeserializer<Recipient>, JsonSerializer<Recipient> {
  private static final String PROPERTY_TYPE = "type";
  private static final String PROPERTY_LABEL = "label";
  private static final String PROPERTY_PHONE_NUMBER = "phoneNumber";
  private static final String PROPERTY_BANK = "bank";
  private static final String PROPERTY_ACCOUNT_NUMBER = "accountNumber";

  @Override
  public Recipient deserialize(
    JsonElement json,
    Type typeOfT,
    JsonDeserializationContext context) throws JsonParseException {
    final JsonObject jo = json.getAsJsonObject();
    if (!jo.has(PROPERTY_TYPE)) {
      throw new JsonParseException("Property '" + PROPERTY_TYPE + "' is missing");
    }
    final RecipientType type = RecipientType.valueOf(jo.get(PROPERTY_TYPE).getAsString());
    String label = null;
    if (jo.has(PROPERTY_LABEL)) {
      label = jo.get(PROPERTY_LABEL).getAsString();
    }
    if (type.equals(RecipientType.PHONE_NUMBER)) {
      if (!jo.has(PROPERTY_PHONE_NUMBER)) {
        throw new JsonParseException("Property '" + PROPERTY_PHONE_NUMBER + "' is missing");
      }
      final String phoneNumber = jo.get(PROPERTY_PHONE_NUMBER).getAsString();
      return new PhoneNumberRecipient(phoneNumber, label);
    } else if (type.equals(RecipientType.NON_AFFILIATED_PHONE_NUMBER)) {
      if (!jo.has(PROPERTY_PHONE_NUMBER)) {
        throw new JsonParseException("Property '" + PROPERTY_PHONE_NUMBER + "' is missing");
      }
      final String phoneNumber = jo.get(PROPERTY_PHONE_NUMBER).getAsString();
      Bank bank = null;
      if (jo.has(PROPERTY_BANK)) {
        bank = context.deserialize(jo.get(PROPERTY_BANK), Bank.class);
      }
      String accountNumber = null;
      if (jo.has(PROPERTY_ACCOUNT_NUMBER)) {
        accountNumber = jo.get(PROPERTY_ACCOUNT_NUMBER).getAsString();
      }
      return new NonAffiliatedPhoneNumberRecipient(phoneNumber, label, bank, accountNumber);
    } else {
      if (!jo.has(PROPERTY_PHONE_NUMBER)) {
        throw new JsonParseException("Property '" + PROPERTY_PHONE_NUMBER + "' is missing");
      }
      final String phoneNumber = jo.get(PROPERTY_PHONE_NUMBER).getAsString();
      return new ContactRecipient(phoneNumber, label);
    }
  }

  @Override
  public JsonElement serialize(Recipient src, Type typeOfSrc, JsonSerializationContext context) {
    final JsonObject jsonObject = new JsonObject();
    final RecipientType type = src.getType();
    jsonObject.addProperty(PROPERTY_TYPE, type.toString());
    if (!TextUtils.isEmpty(src.getLabel())) {
      jsonObject.addProperty(PROPERTY_LABEL, src.getLabel());
    }
    if (type.equals(RecipientType.PHONE_NUMBER)) {
      final PhoneNumberRecipient r = (PhoneNumberRecipient) src;
      jsonObject.addProperty(PROPERTY_PHONE_NUMBER, r.getPhoneNumber());
    } else if (type.equals(RecipientType.NON_AFFILIATED_PHONE_NUMBER)) {
      final NonAffiliatedPhoneNumberRecipient r = (NonAffiliatedPhoneNumberRecipient) src;
      jsonObject.addProperty(PROPERTY_PHONE_NUMBER, r.getPhoneNumber());
      if (Objects.isNotNull(r.getBank())) {
        jsonObject.add(PROPERTY_BANK, context.serialize(r.getBank()));
      }
      if (Texts.isNotEmpty(r.getAccountNumber())) {
        jsonObject.addProperty(PROPERTY_ACCOUNT_NUMBER, r.getAccountNumber());
      }
    } else {
      final ContactRecipient r = (ContactRecipient) src;
      jsonObject.addProperty(PROPERTY_PHONE_NUMBER, r.getIdentifier());
    }
    return jsonObject;
  }
}
