package com.tpago.movil;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class Bank implements Parcelable {
  public static Bank create(int code, String id, String name, String imageUriTemplate) {
    return new AutoValue_Bank(code, id, name, imageUriTemplate);
  }

  public static TypeAdapter<Bank> typeAdapter(Gson gson) {
    return new AutoValue_Bank.GsonTypeAdapter(gson);
  }

  @SerializedName("bank-code") public abstract int getCode();
  @SerializedName("bank-id") public abstract String getId();
  @SerializedName("bank-name") public abstract String getName();
  @SerializedName("bank-logo-uri") public abstract String getImageUriTemplate();
}
