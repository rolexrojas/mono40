package com.tpago.movil.api;

import com.google.auto.value.AutoValue;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class ApiError {
  public static ApiError create(Code code, String description) {
    return new AutoValue_ApiError(code, description);
  }

  public abstract Code getCode();

  public abstract String getDescription();

  public enum Code {
  }
}
