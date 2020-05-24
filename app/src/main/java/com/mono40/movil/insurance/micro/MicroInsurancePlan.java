package com.mono40.movil.insurance.micro;

import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mono40.movil.R;
import com.mono40.movil.util.ComparisonChain;

import java.math.BigDecimal;
import java.util.List;

@AutoValue
public abstract class MicroInsurancePlan implements Parcelable, Comparable<MicroInsurancePlan> {

  public static Builder builder() {
    return new AutoValue_MicroInsurancePlan.Builder();
  }

  MicroInsurancePlan() {
  }

  public abstract String partnerId();

  public abstract Data data();

  @Override
  public int compareTo(@NonNull MicroInsurancePlan that) {
    return ComparisonChain.create()
      .compare(this.data(), that.data())
      .result();
  }

  @AutoValue
  public static abstract class Data implements Parcelable, Comparable<Data> {

    public static TypeAdapter<Data> typeAdapter(Gson gson) {
      return new AutoValue_MicroInsurancePlan_Data.GsonTypeAdapter(gson);
    }

    Data() {
    }

    @SerializedName("idPlan")
    public abstract String planId();

    @SerializedName("idProducto")
    public abstract String productId();

    @SerializedName("descplan")
    public abstract String name();

    @SerializedName("stsPlan")
    public abstract String status();

    @SerializedName("codmoneda")
    public abstract String currency();

    @SerializedName("formaPago")
    public abstract List<Term> terms();

    @Override
    public int compareTo(@NonNull Data that) {
      return ComparisonChain.create()
        .compare(this.name(), that.name())
        .result();
    }
  }

  @AutoValue
  public static abstract class Term implements Parcelable, Comparable<Term> {

    public static TypeAdapter<Term> typeAdapter(Gson gson) {
      return new AutoValue_MicroInsurancePlan_Term.GsonTypeAdapter(gson);
    }

    Term() {
    }

    @SerializedName("codformapago")
    public abstract int id();

    @SerializedName("descripcion")
    public abstract String name();

    @Memoized
    @StringRes
    public int stringId() {
      switch (this.id()) {
        case 1:
          return R.string.monthly;
        case 3:
          return R.string.quarterly;
        case 6:
          return R.string.biannual;
        default:
          return R.string.annual;
      }
    }

    @Override
    public int compareTo(@NonNull Term that) {
      return ComparisonChain.create()
        .compare(this.id(), that.id())
        .result();
    }
  }

  @AutoValue
  public abstract static class Request {

    public static TypeAdapter<Request> typeAdapter(Gson gson) {
      return new AutoValue_MicroInsurancePlan_Request.GsonTypeAdapter(gson);
    }

    Request() {
    }

    @SerializedName("outIdSolicitud")
    public abstract int id();

    @SerializedName("outSuma")
    public abstract BigDecimal coverage();

    @SerializedName("outTasa")
    public abstract BigDecimal rate();

    @SerializedName("outPrima")
    public abstract BigDecimal premium();

    @SerializedName("outcodresultadoOutField")
    abstract String resultCode();

    @SerializedName("outdescresultadoOutField")
    abstract String resultDescription();
  }

  @AutoValue.Builder
  public static abstract class Builder {

    Builder() {
    }

    public abstract Builder partnerId(String partnerId);

    public abstract Builder data(Data data);

    public abstract MicroInsurancePlan build();
  }
}
