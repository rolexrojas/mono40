package com.mono40.movil.d.domain;

import androidx.annotation.NonNull;

import java.math.BigDecimal;

/**
 * @author hecvasro
 */
@Deprecated
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

  @Override
  public BigDecimal valueForWalletScreen() {
    return this.getAvailable();
  }
}
