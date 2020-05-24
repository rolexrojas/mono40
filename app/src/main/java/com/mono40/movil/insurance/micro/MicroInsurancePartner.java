package com.mono40.movil.insurance.micro;

import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mono40.movil.hal.HalLink;
import com.mono40.movil.util.ComparisonChain;

import java.util.List;

@AutoValue
public abstract class MicroInsurancePartner
  implements Parcelable, Comparable<MicroInsurancePartner> {

  public static TypeAdapter<MicroInsurancePartner> typeAdapter(Gson gson) {
    return new AutoValue_MicroInsurancePartner.GsonTypeAdapter(gson);
  }

  MicroInsurancePartner() {
  }

  @SerializedName("category")
  public abstract String category();

  @SerializedName("partnerId")
  public abstract String id();

  @SerializedName("name")
  public abstract String name();

  @SerializedName("description")
  public abstract String description();

  @SerializedName("active")
  public abstract boolean isActive();

  @SerializedName("links")
  public abstract List<HalLink> links();

  @Override
  public int compareTo(@NonNull MicroInsurancePartner that) {
    return ComparisonChain.create()
      .compare(this.category(), that.category())
      .compare(this.name(), that.name())
      .result();
  }
}
