package com.tpago.movil.partner;

import android.support.annotation.NonNull;

import com.tpago.movil.company.Company;
import com.tpago.movil.util.ComparisonChain;

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
    return ComparisonChain.create()
      .compare(this.type(), that.type())
      .compare(this.name(), that.name())
      .result();
  }

  /**
   * Partner type enumeration
   */
  public enum Type {
    CARRIER,
    PROVIDER
  }
}
