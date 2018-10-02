package com.tpago.movil.api;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.Code;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.insurance.micro.MicroInsurancePartner;
import com.tpago.movil.insurance.micro.MicroInsurancePlan;
import com.tpago.movil.util.ObjectHelper;

@AutoValue
public abstract class ApiMicroInsuranceBody {

  public static TypeAdapter<ApiMicroInsuranceBody> typeAdapter(Gson gson) {
    return new AutoValue_ApiMicroInsuranceBody.GsonTypeAdapter(gson);
  }

  public static Builder builder() {
    return new AutoValue_ApiMicroInsuranceBody.Builder();
  }

  ApiMicroInsuranceBody() {
  }

  @SerializedName("partner")
  public abstract MicroInsurancePartner partner();

  @SerializedName("partnerCode")
  @Nullable
  public abstract String partnerId();

  @SerializedName("productoPlan")
  @Nullable
  public abstract MicroInsurancePlan.Data planData();

  @SerializedName("codformapago")
  @Nullable
  public abstract String term();

  @SerializedName("generateReqResponse")
  @Nullable
  public abstract MicroInsurancePlan.Request request();

  @SerializedName("funding-account")
  @Nullable
  public abstract Product paymentMethod();

  @SerializedName("pin")
  @Nullable
  public abstract String pin();

  @AutoValue.Builder
  public static abstract class Builder {

    Builder() {
    }

    public abstract Builder partner(MicroInsurancePartner partner);

    abstract Builder partnerId(@Nullable String partnerId);

    abstract Builder planData(@Nullable MicroInsurancePlan.Data planData);

    public final Builder plan(@Nullable MicroInsurancePlan plan) {
      if (ObjectHelper.isNull(plan)) {
        this.partnerId(null);
        this.planData(null);
      } else {
        this.partnerId(plan.partnerId());
        this.planData(plan.data());
      }
      return this;
    }

    abstract Builder term(@Nullable String term);

    public final Builder term(@Nullable MicroInsurancePlan.Term term) {
      if (ObjectHelper.isNull(term)) {
        this.term((String) null);
      } else {
        this.term(Integer.toString(term.id()));
      }
      return this;
    }

    public abstract Builder request(@Nullable MicroInsurancePlan.Request request);

    public abstract Builder paymentMethod(@Nullable Product paymentMethod);

    abstract Builder pin(@Nullable String pin);

    public final Builder pin(@Nullable Code pin) {
      if (ObjectHelper.isNull(pin)) {
        this.pin((String) null);
      } else {
        this.pin(pin.value());
      }
      return this;
    }

    public abstract ApiMicroInsuranceBody build();
  }
}
