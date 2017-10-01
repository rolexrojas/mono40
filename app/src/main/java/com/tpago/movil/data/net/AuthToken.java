package com.tpago.movil.data.net;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.tpago.movil.util.StringHelper;

/**
 * Authorization token representation
 *
 * @author hecvasro
 */
@AutoValue
public abstract class AuthToken {

  public static AuthToken create(String value) {
    if (StringHelper.isNullOrEmpty(value)) {
      throw new IllegalArgumentException("isNullOrEmpty(value)");
    }
    return new AutoValue_AuthToken(value);
  }

  AuthToken() {
  }

  public abstract String value();

  @Memoized
  @Override
  public abstract String toString();

  @Memoized
  @Override
  public abstract int hashCode();
}
