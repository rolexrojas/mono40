package com.tpago.movil.d.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.d.domain.ProductInfo;

import java.math.BigDecimal;

/**
 * @author hecvasro
 */
@Deprecated
@AutoValue
public abstract class TransferToNonAffiliatedRequestBody {
  public static TransferToNonAffiliatedRequestBody create(
    ProductInfo fundingAccount,
    ProductInfo recipientAccount,
    String pin,
    BigDecimal amount) {
    return new AutoValue_TransferToNonAffiliatedRequestBody(
      fundingAccount,
      recipientAccount,
      pin,
      amount);
  }

  public static TypeAdapter<TransferToNonAffiliatedRequestBody> typeAdapter(Gson gson) {
    return new AutoValue_TransferToNonAffiliatedRequestBody.GsonTypeAdapter(gson);
  }

  @SerializedName("funding-account") public abstract ProductInfo getFundingAccount();
  @SerializedName("recipient-account") public abstract ProductInfo getRecipientAccount();
  public abstract String getPin();
  public abstract BigDecimal getAmount();
}
