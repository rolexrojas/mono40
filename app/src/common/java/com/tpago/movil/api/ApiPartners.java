package com.tpago.movil.api;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.company.partner.Partner;

import java.util.List;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class ApiPartners {

  public static TypeAdapter<ApiPartners> typeAdapter(Gson gson) {
    return new AutoValue_ApiPartners.GsonTypeAdapter(gson);
  }

  ApiPartners() {
  }

  @SerializedName("_embedded")
  abstract Wrapper wrapper();

  @Memoized
  List<Partner> value() {
    return this.wrapper()
      .value();
  }

  @AutoValue
  public static abstract class Wrapper {

    public static TypeAdapter<Wrapper> typeAdapter(Gson gson) {
      return new AutoValue_ApiPartners_Wrapper.GsonTypeAdapter(gson);
    }

    Wrapper() {
    }

    @SerializedName("partners")
    abstract List<Partner> value();
  }
}
