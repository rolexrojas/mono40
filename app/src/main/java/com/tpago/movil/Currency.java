package com.tpago.movil;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;

/**
 * Currency representation
 *
 * @author hecvasro
 */
@AutoValue
public abstract class Currency {

  public static Currency create(String value) {
    throw new UnsupportedOperationException("not implemented");
  }

  Currency() {
  }

  public abstract String value();

  @Memoized
  @Override
  public abstract int hashCode();

  @Memoized
  @Override
  public abstract String toString();
}
