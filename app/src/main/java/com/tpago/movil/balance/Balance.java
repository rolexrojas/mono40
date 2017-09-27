package com.tpago.movil.balance;

/**
 * Balance representation
 *
 * @author hecvasro
 */
abstract class Balance {

  private final long queryTime;

  Balance() {
    throw new UnsupportedOperationException("not implemented");
  }

  /**
   * Time (milliseconds) when it was queried.
   */
  public final long queryTime() {
    throw new UnsupportedOperationException("not implemented");
  }
}
