package com.tpago.movil.dep.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.Bank;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class RecipientAccountInfoRequestBody {
  public static RecipientAccountInfoRequestBody create(Bank bank, String accountNumber) {
    return new AutoValue_RecipientAccountInfoRequestBody(bank, accountNumber);
  }

  public static TypeAdapter<RecipientAccountInfoRequestBody> typeAdapter(Gson gson) {
    return new AutoValue_RecipientAccountInfoRequestBody.GsonTypeAdapter(gson);
  }

  public abstract Bank getBank();
  @SerializedName("recipient-account-number") public abstract String getAccountNumber();
}
