package com.tpago.movil.payment;

import android.support.annotation.NonNull;

import com.tpago.movil.domain.company.Company;

/**
 * Partner representation
 *
 * @author hecvasro
 */
public abstract class Partner extends Company implements Comparable<Partner> {

  Partner() {
  }

  public abstract Type type();

  @Override
  public int compareTo(@NonNull Partner that) {
    throw new UnsupportedOperationException("not implemented");
  }

  /**
   * Partner type enumeration
   */
  public enum Type {
    CARRIER,
    PROVIDER
  }
}
