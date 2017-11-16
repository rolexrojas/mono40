package com.tpago.movil.currency;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;

/**
 * Currency representation
 *
 * @author hecvasro
 */
@AutoValue
public abstract class Currency {

  static Currency create(String value) {
    return new AutoValue_Currency(value);
  }

  Currency() {
  }

  public abstract String value();

  @Memoized
  @Override
  public abstract String toString();

  @Memoized
  @Override
  public abstract int hashCode();
}
