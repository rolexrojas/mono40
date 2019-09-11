package com.tpago.movil.d.data.api;

import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.d.domain.PhoneNumberRecipient;
import com.tpago.movil.d.domain.Product;

import java.math.BigDecimal;

/**
 * @author hecvasro
 */
@Deprecated
@AutoValue
public abstract class TransferToAffiliatedRequestBody {

  static TransferToAffiliatedRequestBody create(
    Product product,
    PhoneNumberRecipient recipient,
    String name,
    BigDecimal amount,
    String pin
  ) {
    return new AutoValue_TransferToAffiliatedRequestBody(
      product.getAlias(),
      product.getNumber(),
      product.getType()
        .name(),
      product.getBank(),
      product.getCurrency(),
      pin,
      amount,
      recipient.getPhoneNumber()
        .value(),
      name
    );
  }

  public static TypeAdapter<TransferToAffiliatedRequestBody> typeAdapter(Gson gson) {
    return new AutoValue_TransferToAffiliatedRequestBody.GsonTypeAdapter(gson);
  }

  @SerializedName("account-alias")
  abstract String getAccountAlias();

  @SerializedName("account-number")
  abstract String getAccountNumber();

  @SerializedName("account-type")
  abstract String getAccountType();

  @SerializedName("bank")
  abstract Bank getBank();

  @SerializedName("currency")
  abstract String getCurrency();

  @SerializedName("pin")
  abstract String getPin();

  @SerializedName("amount")
  abstract BigDecimal getAmount();

  @SerializedName("recipient-msisdn")
  abstract String getRecipientMsisdn();

  @Nullable
  @SerializedName("recipient-name")
  abstract String getRecipientName();
}
