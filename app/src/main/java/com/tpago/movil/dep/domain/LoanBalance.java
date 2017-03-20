package com.tpago.movil.dep.domain;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

/**
 * @author hecvasro
 */
public class LoanBalance extends Balance {
  private final BigDecimal fee;

  public LoanBalance(@NonNull BigDecimal value, @NonNull BigDecimal fee) {
    super(value);
    this.fee = fee;
  }

  @NonNull
  public final BigDecimal getFee() {
    return fee;
  }
}
