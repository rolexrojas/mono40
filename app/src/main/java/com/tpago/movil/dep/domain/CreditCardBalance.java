package com.tpago.movil.dep.domain;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

/**
 * @author hecvasro
 */
public class CreditCardBalance extends Balance {
  private final BigDecimal available;

  public CreditCardBalance(@NonNull BigDecimal value, @NonNull BigDecimal available) {
    super(value);
    this.available = available;
  }

  @NonNull
  public final BigDecimal getAvailable() {
    return available;
  }
}
