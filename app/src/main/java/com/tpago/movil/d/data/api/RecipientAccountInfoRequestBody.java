package com.tpago.movil.d.data.api;

import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.company.bank.Bank;

/**
 * @author hecvasro
 */
@Deprecated
@AutoValue
public abstract class RecipientAccountInfoRequestBody {

  public static RecipientAccountInfoRequestBody create(Bank bank, String accountNumber, String accounType) {
    return new AutoValue_RecipientAccountInfoRequestBody(bank, accountNumber, accounType);
  }

  public static TypeAdapter<RecipientAccountInfoRequestBody> typeAdapter(Gson gson) {
    return new AutoValue_RecipientAccountInfoRequestBody.GsonTypeAdapter(gson);
  }

  public abstract Bank getBank();

  @SerializedName("recipient-account-number")
  public abstract String getAccountNumber();

  @Nullable
  @SerializedName("account-type")
  public abstract String getAccountType();
}
