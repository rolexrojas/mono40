package com.mono40.movil.api;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mono40.movil.insurance.micro.MicroInsurancePlan;

import java.util.List;

import io.reactivex.Observable;

@AutoValue
public abstract class ApiMicroInsurancePlans {

  public static TypeAdapter<ApiMicroInsurancePlans> typeAdapter(Gson gson) {
    return new AutoValue_ApiMicroInsurancePlans.GsonTypeAdapter(gson);
  }

  ApiMicroInsurancePlans() {
  }

  @SerializedName("partnerCode")
  public abstract String partnerId();

  @SerializedName("productoPlanes")
  public abstract Wrapper wrapper();

  @Memoized
  public List<MicroInsurancePlan> plans() {
    return Observable.just(this.wrapper())
      .flatMap((wrapper) -> Observable.fromIterable(wrapper.plans()))
      .map((data) ->
        MicroInsurancePlan.builder()
          .partnerId(this.partnerId())
          .data(data)
          .build()
      )
      .toList()
      .blockingGet();
  }

  @AutoValue
  public static abstract class Wrapper {

    public static TypeAdapter<Wrapper> typeAdapter(Gson gson) {
      return new AutoValue_ApiMicroInsurancePlans_Wrapper.GsonTypeAdapter(gson);
    }

    Wrapper() {
    }

    @SerializedName("ProductoPlanes")
    public abstract List<MicroInsurancePlan.Data> plans();
  }
}
