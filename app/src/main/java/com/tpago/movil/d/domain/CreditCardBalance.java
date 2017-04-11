package com.tpago.movil.d.domain;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

/**
 * @author hecvasro
 */
public class CreditCardBalance extends Balance {
  private final BigDecimal available;

  public CreditCardBalance(BigDecimal value, BigDecimal available) {
    super(value);
    this.available = available;
  }

  @NonNull
  public final BigDecimal getAvailable() {
    return available;
  }
}
