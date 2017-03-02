package com.tpago.movil.dep.data.api;

import android.text.TextUtils;

import com.tpago.movil.Bank;
import com.tpago.movil.Partner;
import com.tpago.movil.dep.domain.BillRecipient;
import com.tpago.movil.dep.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.dep.domain.PhoneNumberRecipient;
import com.tpago.movil.dep.domain.Product;
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
  private static final String PROPERTY_PARTNER = "partner";
  private static final String PROPERTY_CONTRACT_NUMBER = "contractNumber";
  private static final String PROPERTY_PRODUCT = "product";

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
      Product product = null;
      if (jo.has(PROPERTY_PRODUCT)) {
        product = context.deserialize(jo.get(PROPERTY_PRODUCT), Product.class);
      }
      return new NonAffiliatedPhoneNumberRecipient(phoneNumber, label, bank, accountNumber, product);
    } else if (type.equals(RecipientType.BILL)) {
      if (!jo.has(PROPERTY_PARTNER)) {
        throw new JsonParseException("Property '" + PROPERTY_PARTNER + "' is missing");
      }
      final Partner partner = context.deserialize(jo.get(PROPERTY_PARTNER), Partner.class);
      if (!jo.has(PROPERTY_CONTRACT_NUMBER)) {
        throw new JsonParseException("Property '" + PROPERTY_CONTRACT_NUMBER + "' is missing");
      }
      final String contractNumber = jo.get(PROPERTY_CONTRACT_NUMBER).getAsString();
      return new BillRecipient(partner, contractNumber, label);
    } else {
      return null;
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
      if (Objects.isNotNull(r.getProduct())) {
        jsonObject.add(PROPERTY_PRODUCT, context.serialize(r.getProduct()));
      }
    } else if (type.equals(RecipientType.BILL)) {
      final BillRecipient r = (BillRecipient) src;
      jsonObject.add(PROPERTY_PARTNER, context.serialize(r.getPartner()));
      jsonObject.addProperty(PROPERTY_CONTRACT_NUMBER, r.getContractNumber());
    }
    return jsonObject;
  }
}
