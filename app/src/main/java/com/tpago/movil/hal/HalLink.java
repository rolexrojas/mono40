package com.tpago.movil.hal;

import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.util.ComparisonChain;

@AutoValue
public abstract class HalLink implements Parcelable, Comparable<HalLink> {

  public static TypeAdapter<HalLink> typeAdapter(Gson gson) {
    return new AutoValue_HalLink.GsonTypeAdapter(gson);
  }

  HalLink() {
  }

  @SerializedName("rel")
  public abstract String relationship();

  @SerializedName("href")
  public abstract String value();

  @Override
  public int compareTo(@NonNull HalLink that) {
    return ComparisonChain.create()
      .compare(this.relationship(), that.relationship())
      .compare(this.value(), that.value())
      .result();
  }
}
