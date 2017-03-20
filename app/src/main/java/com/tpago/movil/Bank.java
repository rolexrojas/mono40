package com.tpago.movil;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class Bank implements Serializable, Parcelable {
  public static Bank create(int code, String id, String name, String logoUri) {
    return new AutoValue_Bank(code, id, name, logoUri);
  }

  /**
   * Temporary fix for: <a href="https://trello.com/c/09CG3iLy">https://trello.com/c/09CG3iLy</a>
   */
  public static String getName(Bank bank) {
    if (bank.getCode() == 5) {
      return "Popular";
    } else {
      return bank.getName();
    }
  }

  public static TypeAdapter<Bank> typeAdapter(Gson gson) {
    return new AutoValue_Bank.GsonTypeAdapter(gson);
  }

  @SerializedName("bank-code") public abstract int getCode();
  @SerializedName("bank-id") public abstract String getId();
  @SerializedName("bank-name") public abstract String getName();
  @SerializedName("bank-logo-uri") public abstract String getLogoUri();
}
