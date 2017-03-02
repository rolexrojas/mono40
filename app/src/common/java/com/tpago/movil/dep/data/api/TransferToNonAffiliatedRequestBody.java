package com.tpago.movil.dep.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.dep.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.dep.domain.Product;

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
      RecipientAccount.create(recipient),
      pin,
      amount);
  }

  public static TypeAdapter<TransferToNonAffiliatedRequestBody> typeAdapter(Gson gson) {
    return new AutoValue_TransferToNonAffiliatedRequestBody.GsonTypeAdapter(gson);
  }

  @SerializedName("funding-account") public abstract Product getFundingAccount();
  @SerializedName("recipient-account") public abstract RecipientAccount getRecipientAccount();
  public abstract String getPin();
  public abstract BigDecimal getAmount();
}
