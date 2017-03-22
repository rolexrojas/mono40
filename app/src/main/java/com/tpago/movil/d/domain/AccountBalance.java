package com.tpago.movil.d.domain;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

/**
 * @author hecvasro
 */
public class AccountBalance extends Balance {
  private final BigDecimal available;

  public AccountBalance(@NonNull BigDecimal total, @NonNull BigDecimal available) {
    super(total);
    this.available = available;
  }

  @NonNull
  public final BigDecimal getAvailable() {
    return available;
  }
}
