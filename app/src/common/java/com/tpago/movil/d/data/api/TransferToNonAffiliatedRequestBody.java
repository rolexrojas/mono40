package com.tpago.movil.d.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.d.domain.Product;

import java.math.BigDecimal;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class TransferToNonAffiliatedRequestBody {
  public static TransferToNonAffiliatedRequestBody create(
    Product fundingAccount,
    NonAffiliatedPhoneNumberRecipient recipient,
    String pin,
    BigDecimal amount) {
    return new AutoValue_TransferToNonAffiliatedRequestBody(
      fundingAccount,
      recipient.getProduct(),
      pin,
      amount);
  }

  public static TypeAdapter<TransferToNonAffiliatedRequestBody> typeAdapter(Gson gson) {
    return new AutoValue_TransferToNonAffiliatedRequestBody.GsonTypeAdapter(gson);
  }

  @SerializedName("funding-account") public abstract Product getFundingAccount();
  @SerializedName("recipient-account") public abstract Product getRecipientAccount();
  public abstract String getPin();
  public abstract BigDecimal getAmount();
}
