package com.gbh.movil.domain;

import android.support.annotation.NonNull;

/**
 * {@link Account} type enumeration.
 *
 * @author hecvasro
 */
public enum AccountType {
  SAVINGS ("SAV"),
  CREDIT_CARD ("CC");

  private final String value;

  AccountType(@NonNull String value) {
    this.value = value;
  }

  @NonNull
  public final String getValue() {
    return value;
  }
}
