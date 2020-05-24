package com.mono40.movil.d.domain;

import android.os.Parcelable;
import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * @author hecvasro
 */
@Deprecated
@AutoValue
public abstract class BillBalance implements Parcelable {

  public static TypeAdapter<BillBalance> typeAdapter(Gson gson) {
    return new AutoValue_BillBalance.GsonTypeAdapter(gson);
  }

  public static Builder builder() {
    return new AutoValue_BillBalance.Builder();
  }

  @SerializedName("due-date")
  @Nullable
  public abstract String getDate();

  @SerializedName("total-amount")
  public abstract BigDecimal getTotal();

  @SerializedName("minimum-amount")
  public abstract BigDecimal getMinimum();

  @AutoValue.Builder
  public static abstract class Builder {

    public abstract Builder setDate(String date);

    public abstract Builder setTotal(BigDecimal total);

    public abstract Builder setMinimum(BigDecimal minimum);

    public abstract BillBalance build();
  }
}
