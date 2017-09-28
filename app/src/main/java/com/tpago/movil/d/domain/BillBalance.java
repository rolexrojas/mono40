package com.tpago.movil.d.domain;

import android.os.Parcelable;
import android.support.annotation.Nullable;

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

  @SerializedName("due-date") @Nullable public abstract String getDate();
  @SerializedName("total-amount") public abstract BigDecimal getTotal();
  @SerializedName("minimum-amount") public abstract BigDecimal getMinimum();
}
