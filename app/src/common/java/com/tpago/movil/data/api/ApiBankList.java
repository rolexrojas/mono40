package com.tpago.movil.data.api;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.util.Bank;

import java.util.List;

@AutoValue
public abstract class ApiBankList {

  public static TypeAdapter<ApiBankList> typeAdapter(Gson gson) {
    return new AutoValue_ApiBankList.GsonTypeAdapter(gson);
  }

  ApiBankList() {
  }

  @SerializedName("_embedded")
  abstract Wrapper wrapper();

  @Memoized
  List<Bank> value() {
    return this.wrapper()
      .value();
  }

  @AutoValue
  public static abstract class Wrapper {

    public static TypeAdapter<Wrapper> typeAdapter(Gson gson) {
      return new AutoValue_ApiBankList_Wrapper.GsonTypeAdapter(gson);
    }

    Wrapper() {
    }

    @SerializedName("banks")
    abstract List<Bank> value();
  }
}
