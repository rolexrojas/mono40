package com.mono40.movil.api;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mono40.movil.company.bank.Bank;

import java.util.List;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class ApiBanks {

  public static TypeAdapter<ApiBanks> typeAdapter(Gson gson) {
    return new AutoValue_ApiBanks.GsonTypeAdapter(gson);
  }

  ApiBanks() {
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
      return new AutoValue_ApiBanks_Wrapper.GsonTypeAdapter(gson);
    }

    Wrapper() {
    }

    @SerializedName("bankDTOList")
    abstract List<Bank> value();
  }
}
