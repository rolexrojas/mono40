package com.mono40.movil.d.domain;

import androidx.annotation.NonNull;

import java.math.BigDecimal;

/**
 * @author hecvasro
 */
@Deprecated
public class LoanBalance extends Balance {
  private final BigDecimal fee;

  public LoanBalance(BigDecimal value, BigDecimal fee) {
    super(value);
    this.fee = fee;
  }

  @NonNull
  public final BigDecimal getFee() {
    return fee;
  }

  @Override
  public BigDecimal valueForWalletScreen() {
    return this.getValue();
  }
}
