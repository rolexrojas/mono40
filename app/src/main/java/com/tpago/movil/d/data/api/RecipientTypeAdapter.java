package com.tpago.movil.d.data.api;

import android.text.TextUtils;

import com.tpago.movil.d.domain.CreditCardBillBalance;
import com.tpago.movil.d.domain.LoanBillBalance;
import com.tpago.movil.d.domain.ProductBillBalance;
import com.tpago.movil.d.domain.ProductRecipient;
import com.tpago.movil.domain.Bank;
import com.tpago.movil.Partner;
import com.tpago.movil.d.domain.BillBalance;
import com.tpago.movil.d.domain.BillRecipient;
import com.tpago.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.d.domain.PhoneNumberRecipient;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.RecipientType;
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
@Deprecated
class RecipientTypeAdapter implements JsonDeserializer<Recipient>, JsonSerializer<Recipient> {
  private static final String PROPERTY_TYPE = "type";
  private static final String PROPERTY_LABEL = "label";
  private static final String PROPERTY_PHONE_NUMBER = "phoneNumber";
  private static final String PROPERTY_BANK = "bank";
  private static final String PROPERTY_ACCOUNT_NUMBER = "accountNumber";
  private static final String PROPERTY_PARTNER = "partner";
  private static final String PROPERTY_CONTRACT_NUMBER = "contractNumber";
  private static final String PROPERTY_PRODUCT = "creditCard";
  private static final String PROPERTY_BALANCE = "balance";

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
      final BillRecipient b = new BillRecipient(partner, contractNumber, label);
      if (jo.has(PROPERTY_BALANCE)) {
        b.setBalance((BillBalance) context.deserialize(jo.get(PROPERTY_BALANCE), BillBalance.class));
      }
      return b;
    } else if (type.equals(RecipientType.PRODUCT)) {
      if (!jo.has(PROPERTY_PRODUCT)) {
        throw new JsonParseException("Property '" + PROPERTY_PRODUCT + "' is missing");
      }
      final Product product = context.deserialize(jo.get(PROPERTY_PRODUCT), Product.class);
      final ProductRecipient p = new ProductRecipient(product, label);
      if (jo.has(PROPERTY_BALANCE)) {
        if (Product.checkIfCreditCard(product)) {
          p.setBalance((CreditCardBillBalance) context.deserialize(jo.get(PROPERTY_BALANCE), CreditCardBillBalance.class));
        } else {
          p.setBalance((LoanBillBalance) context.deserialize(jo.get(PROPERTY_BALANCE), LoanBillBalance.class));
        }
      }
      return p;
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
      if (Objects.checkIfNotNull(r.getBank())) {
        jsonObject.add(PROPERTY_BANK, context.serialize(r.getBank(), Bank.class));
      }
      if (Texts.checkIfNotEmpty(r.getAccountNumber())) {
        jsonObject.addProperty(PROPERTY_ACCOUNT_NUMBER, r.getAccountNumber());
      }
      if (Objects.checkIfNotNull(r.getProduct())) {
        jsonObject.add(PROPERTY_PRODUCT, context.serialize(r.getProduct(), Product.class));
      }
    } else if (type.equals(RecipientType.BILL)) {
      final BillRecipient r = (BillRecipient) src;
      jsonObject.add(PROPERTY_PARTNER, context.serialize(r.getPartner(), Partner.class));
      jsonObject.addProperty(PROPERTY_CONTRACT_NUMBER, r.getContractNumber());
      final BillBalance b = r.getBalance();
      if (Objects.checkIfNotNull(b)) {
        jsonObject.add(PROPERTY_BALANCE, context.serialize(b, BillBalance.class));
      }
    } else if (type.equals(RecipientType.PRODUCT)) {
      final ProductRecipient r = (ProductRecipient) src;
      jsonObject.add(PROPERTY_PRODUCT, context.serialize(r.getProduct()));
      final ProductBillBalance b = r.getBalance();
      if (Objects.checkIfNotNull(b)) {
        if (Product.checkIfCreditCard(r.getProduct())) {
          jsonObject.add(PROPERTY_BALANCE, context.serialize(b, CreditCardBillBalance.class));
        } else {
          jsonObject.add(PROPERTY_BALANCE, context.serialize(b, LoanBillBalance.class));
        }
      }
    }
    return jsonObject;
  }
}
