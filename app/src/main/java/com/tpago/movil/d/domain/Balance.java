package com.tpago.movil.d.domain;

import java.math.BigDecimal;

/**
 * @author hecvasro
 */
public abstract class Balance {
  private final BigDecimal value;

  Balance(BigDecimal value) {
    this.value = value;
  }

  public abstract BigDecimal valueForWalletScreen();

  public final BigDecimal getValue() {
    return value;
  }

  @Override
  public String toString() {
    return Balance.class.getSimpleName() + ":{value=" + value + "'}";
  }
}
