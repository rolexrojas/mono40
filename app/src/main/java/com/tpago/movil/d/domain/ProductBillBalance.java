package com.tpago.movil.d.domain;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * @author Hector Vasquez
 */
public abstract class ProductBillBalance implements Parcelable {
  @SerializedName("due-date") public abstract String dueDate();

  @SerializedName("current-amount") public abstract BigDecimal currentAmount();
  @SerializedName("period-amount") public abstract BigDecimal periodAmount();
}
