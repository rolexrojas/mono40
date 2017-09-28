package com.tpago.movil.net;

import com.google.auto.value.AutoValue;

/**
 * @author hecvasro
 */
@Deprecated
@AutoValue
public abstract class HttpResult<T> extends Result<T> {
  public static <T> HttpResult<T> create(HttpCode code, T data) {
    return new AutoValue_HttpResult<>(data, code);
  }

  public abstract HttpCode getCode();

  @Override
  public boolean isSuccessful() {
    return HttpCode.isSuccessCode(getCode());
  }
}
