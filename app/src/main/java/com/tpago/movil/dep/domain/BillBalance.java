package com.tpago.movil.dep.domain;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class BillBalance implements Serializable {
  public static TypeAdapter<BillBalance> typeAdapter(Gson gson) {
    return new AutoValue_BillBalance.GsonTypeAdapter(gson);
  }

  @SerializedName("due-date") public abstract Date getDate();
  @SerializedName("total-amount") public abstract BigDecimal getTotal();
  @SerializedName("minimum-amount") public abstract BigDecimal getMinimum();
}
