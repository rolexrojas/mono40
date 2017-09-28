package com.tpago.movil.domain.auth;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.tpago.movil.util.StringHelper;

/**
 * Access token representation
 *
 * @author hecvasro
 */
@AutoValue
public abstract class AuthorizationToken {

  public static AuthorizationToken create(String value) {
    if (StringHelper.isNullOrEmpty(value)) {
      throw new IllegalArgumentException("isNullOrEmpty(value)");
    }
    return new AutoValue_AuthorizationToken(value);
  }

  AuthorizationToken() {
  }

  public abstract String value();

  @Memoized
  @Override
  public abstract int hashCode();

  @Memoized
  @Override
  public abstract String toString();
}
