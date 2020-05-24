package com.mono40.movil.d.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mono40.movil.company.bank.Bank;
import com.mono40.movil.d.domain.NonAffiliatedPhoneNumberRecipient;

/**
 * @author hecvasro
 */
@Deprecated
@AutoValue
public abstract class RecipientAccount {

  public static RecipientAccount create(NonAffiliatedPhoneNumberRecipient recipient) {
    return new AutoValue_RecipientAccount(recipient.getBank(), recipient.getAccountNumber());
  }

  public static TypeAdapter<RecipientAccount> typeAdapter(Gson gson) {
    return new AutoValue_RecipientAccount.GsonTypeAdapter(gson);
  }

  @SerializedName("bank")
  public abstract Bank getBank();

  @SerializedName("account-number")
  public abstract String getAccountNumber();
}
