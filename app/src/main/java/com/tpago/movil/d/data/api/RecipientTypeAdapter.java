package com.tpago.movil.d.data.api;

import static com.tpago.movil.d.domain.RecipientType.ACCOUNT;
import static com.tpago.movil.d.domain.RecipientType.BILL;
import static com.tpago.movil.d.domain.RecipientType.NON_AFFILIATED_PHONE_NUMBER;
import static com.tpago.movil.d.domain.RecipientType.PHONE_NUMBER;
import static com.tpago.movil.d.domain.RecipientType.PRODUCT;

import android.text.TextUtils;

import com.tpago.movil.PhoneNumber;
import com.tpago.movil.d.domain.AccountRecipient;
import com.tpago.movil.d.domain.ProductRecipient;
import com.tpago.movil.d.domain.Bank;
import com.tpago.movil.dep.Partner;
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
import com.tpago.movil.dep.text.Texts;
import com.tpago.movil.util.ObjectHelper;

import java.lang.reflect.Type;

/**
 * @author hecvasro
 */
@Deprecated
public class RecipientTypeAdapter
  implements JsonDeserializer<Recipient>, JsonSerializer<Recipient> {

  private static final String PROPERTY_TYPE = "type";
  private static final String PROPERTY_LABEL = "label";
  private static final String PROPERTY_PHONE_NUMBER = "phoneNumber";
  private static final String PROPERTY_BANK = "bank";
  private static final String PROPERTY_ACCOUNT_NUMBER = "accountNumber";
  private static final String PROPERTY_PARTNER = "partner";
  private static final String PROPERTY_CONTRACT_NUMBER = "contractNumber";
  private static final String PROPERTY_PRODUCT = "product";
  //  private static final String PROPERTY_BALANCE = "balance";
  private static final String PROPERTY_CARRIER = "carrier";

  @Override
  public Recipient deserialize(
    JsonElement json,
    Type typeOfT,
    JsonDeserializationContext context
  ) throws JsonParseException {
    final JsonObject jo = json.getAsJsonObject();
    if (!jo.has(PROPERTY_TYPE)) {
      throw new JsonParseException("Property '" + PROPERTY_TYPE + "' is missing");
    }
    final RecipientType type = RecipientType.valueOf(jo.get(PROPERTY_TYPE)
      .getAsString());
    String label = null;
    if (jo.has(PROPERTY_LABEL)) {
      label = jo.get(PROPERTY_LABEL)
        .getAsString();
    }
    if (type == PHONE_NUMBER) {
      if (!jo.has(PROPERTY_PHONE_NUMBER)) {
        throw new JsonParseException("Property '" + PROPERTY_PHONE_NUMBER + "' is missing");
      }
      final String phoneNumber = jo.get(PROPERTY_PHONE_NUMBER)
        .getAsString();
      Partner carrier = null;
      if (jo.has(PROPERTY_CARRIER)) {
        carrier = context.deserialize(jo.get(PROPERTY_CARRIER), Partner.class);
      }
      return new PhoneNumberRecipient(PhoneNumber.create(phoneNumber), carrier, label);
    } else if (type == NON_AFFILIATED_PHONE_NUMBER) {
      if (!jo.has(PROPERTY_PHONE_NUMBER)) {
        throw new JsonParseException("Property '" + PROPERTY_PHONE_NUMBER + "' is missing");
      }
      final String phoneNumber = jo.get(PROPERTY_PHONE_NUMBER)
        .getAsString();
      Bank bank = null;
      if (jo.has(PROPERTY_BANK)) {
        bank = context.deserialize(jo.get(PROPERTY_BANK), Bank.class);
      }
      String accountNumber = null;
      if (jo.has(PROPERTY_ACCOUNT_NUMBER)) {
        accountNumber = jo.get(PROPERTY_ACCOUNT_NUMBER)
          .getAsString();
      }
      Product product = null;
      if (jo.has(PROPERTY_PRODUCT)) {
        product = context.deserialize(jo.get(PROPERTY_PRODUCT), Product.class);
      }
      Partner carrier = null;
      if (jo.has(PROPERTY_CARRIER)) {
        carrier = context.deserialize(jo.get(PROPERTY_CARRIER), Partner.class);
      }
      return new NonAffiliatedPhoneNumberRecipient(
        PhoneNumber.create(phoneNumber),
        label,
        bank,
        accountNumber,
        product,
        carrier
      );
    } else if (type == BILL) {
      if (!jo.has(PROPERTY_PARTNER)) {
        throw new JsonParseException("Property '" + PROPERTY_PARTNER + "' is missing");
      }
      final Partner partner = context.deserialize(jo.get(PROPERTY_PARTNER), Partner.class);
      if (!jo.has(PROPERTY_CONTRACT_NUMBER)) {
        throw new JsonParseException("Property '" + PROPERTY_CONTRACT_NUMBER + "' is missing");
      }
      final String contractNumber = jo.get(PROPERTY_CONTRACT_NUMBER)
        .getAsString();
      final BillRecipient b = new BillRecipient(partner, contractNumber, label);
//      if (jo.has(PROPERTY_BALANCE)) {
//        b.setBalance((BillBalance) context.deserialize(jo.generate(PROPERTY_BALANCE), BillBalance.class));
//      }
      return b;
    } else if (type == PRODUCT) {
      if (!jo.has(PROPERTY_PRODUCT)) {
        throw new JsonParseException("Property '" + PROPERTY_PRODUCT + "' is missing");
      }
      final Product product = context.deserialize(jo.get(PROPERTY_PRODUCT), Product.class);
      final ProductRecipient p = new ProductRecipient(product, label);
//      if (jo.has(PROPERTY_BALANCE)) {
//        if (Product.checkIfCreditCard(product)) {
//          p.setBalance((CreditCardBillBalance) context.deserialize(jo.generate(PROPERTY_BALANCE), CreditCardBillBalance.class));
//        } else {
//          p.setBalance((LoanBillBalance) context.deserialize(jo.generate(PROPERTY_BALANCE), LoanBillBalance.class));
//        }
//      }
      return p;
    } else if (type == ACCOUNT) {
      if (!jo.has(PROPERTY_ACCOUNT_NUMBER)) {
        throw new JsonParseException("Property '" + PROPERTY_ACCOUNT_NUMBER + "' is missing");
      }
      final AccountRecipient.Builder builder = AccountRecipient.builder()
        .number(
          jo.get(PROPERTY_ACCOUNT_NUMBER)
            .getAsString()
        )
        .label(label);
      final Bank bank;
      if (jo.has(PROPERTY_BANK)) {
        bank = context.deserialize(jo.get(PROPERTY_BANK), Bank.class);
      } else {
        bank = null;
      }
      builder.bank(bank);
      final Product product;
      if (jo.has(PROPERTY_PRODUCT)) {
        product = context.deserialize(jo.get(PROPERTY_PRODUCT), Product.class);
      } else {
        product = null;
      }
      builder.product(product);
      return builder.build();
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
    if (type == PHONE_NUMBER) {
      final PhoneNumberRecipient r = (PhoneNumberRecipient) src;
      jsonObject.addProperty(
        PROPERTY_PHONE_NUMBER,
        r.getPhoneNumber()
          .value()
      );
      final Partner carrier = r.getCarrier();
      if (ObjectHelper.isNotNull(carrier)) {
        jsonObject.add(PROPERTY_CARRIER, context.serialize(r.getCarrier(), Partner.class));
      }
    } else if (type == NON_AFFILIATED_PHONE_NUMBER) {
      final NonAffiliatedPhoneNumberRecipient r = (NonAffiliatedPhoneNumberRecipient) src;
      jsonObject.addProperty(
        PROPERTY_PHONE_NUMBER,
        r.getPhoneNumber()
          .value()
      );
      if (ObjectHelper.isNotNull(r.getBank())) {
        jsonObject.add(PROPERTY_BANK, context.serialize(r.getBank(), Bank.class));
      }
      if (Texts.checkIfNotEmpty(r.getAccountNumber())) {
        jsonObject.addProperty(PROPERTY_ACCOUNT_NUMBER, r.getAccountNumber());
      }
      if (ObjectHelper.isNotNull(r.getProduct())) {
        jsonObject.add(PROPERTY_PRODUCT, context.serialize(r.getProduct(), Product.class));
      }
      final Partner carrier = r.getCarrier();
      if (ObjectHelper.isNotNull(carrier)) {
        jsonObject.add(PROPERTY_CARRIER, context.serialize(r.getCarrier(), Partner.class));
      }
    } else if (type == BILL) {
      final BillRecipient r = (BillRecipient) src;
      jsonObject.add(PROPERTY_PARTNER, context.serialize(r.getPartner(), Partner.class));
      jsonObject.addProperty(PROPERTY_CONTRACT_NUMBER, r.getContractNumber());
//      final BillBalance b = r.getBalance();
//      if (Objects.checkIfNotNull(b)) {
//        jsonObject.add(PROPERTY_BALANCE, context.serialize(b, BillBalance.class));
//      }
    } else if (type == PRODUCT) {
      final ProductRecipient r = (ProductRecipient) src;
      jsonObject.add(PROPERTY_PRODUCT, context.serialize(r.getProduct(), Product.class));
//      final ProductBillBalance b = r.getBalance();
//      if (Objects.checkIfNotNull(b)) {
//        if (Product.checkIfCreditCard(r.getProduct())) {
//          jsonObject.add(PROPERTY_BALANCE, context.serialize(b, CreditCardBillBalance.class));
//        } else {
//          jsonObject.add(PROPERTY_BALANCE, context.serialize(b, LoanBillBalance.class));
//        }
//      }
    } else if (type == ACCOUNT) {
      final AccountRecipient r = (AccountRecipient) src;
      jsonObject.addProperty(PROPERTY_ACCOUNT_NUMBER, r.number());
      final Bank bank = r.bank();
      if (ObjectHelper.isNotNull(bank)) {
        jsonObject.add(PROPERTY_BANK, context.serialize(bank, Bank.class));
      }
      final Product product = r.product();
      if (ObjectHelper.isNotNull(product)) {
        jsonObject.add(PROPERTY_PRODUCT, context.serialize(product, Product.class));
      }
    }
    return jsonObject;
  }
}
