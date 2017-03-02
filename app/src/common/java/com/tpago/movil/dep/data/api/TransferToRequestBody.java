package com.tpago.movil.dep.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.Bank;
import com.tpago.movil.dep.domain.Product;
import com.tpago.movil.dep.domain.Recipient;

import java.math.BigDecimal;

/**
 * TODO
 *
 * @author hecvasro
 */
@AutoValue
public abstract class TransferToRequestBody {
  static TransferToRequestBody create(
    Product product,
    Recipient recipient,
    BigDecimal amount,
    String pin) {
    return new AutoValue_TransferToRequestBody(
      product.getAlias(),
      product.getNumber(),
      product.getType().name(),
      product.getBank(),
      product.getCurrency(),
      pin,
      amount,
      recipient.getIdentifier(),
      recipient.getLabel());
  }

  public static TypeAdapter<TransferToRequestBody> typeAdapter(Gson gson) {
    return new AutoValue_TransferToRequestBody.GsonTypeAdapter(gson);
  }

  @SerializedName("account-alias") abstract String getAccountAlias();
  @SerializedName("account-number") abstract String getAccountNumber();
  @SerializedName("account-type") abstract String getAccountType();
  @SerializedName("bank") abstract Bank getBank();
  @SerializedName("currency") abstract String getCurrency();
  @SerializedName("pin") abstract String getPin();
  @SerializedName("amount") abstract BigDecimal getAmount();
  @SerializedName("recipient-msisdn") abstract String getRecipientMsisdn();
  @SerializedName("recipient-name") abstract String getRecipientName();
}
