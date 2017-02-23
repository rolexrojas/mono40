package com.tpago.movil.api;

import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
public final class ApiData<T> {
  private final T value;
  private final ApiError error;

  public static <T> ApiData<T> create(T value) {
    return new ApiData<>(value, null);
  }

  public static <T> ApiData<T> create(ApiError error) {
    return new ApiData<>(null, error);
  }

  private ApiData(T value, ApiError error) {
    if (Objects.isNull(error)) {
      this.value = Preconditions.checkNotNull(value, "value == null");
      this.error = null;
    } else {
      this.value = null;
      this.error = Preconditions.checkNotNull(error, "error == null");
    }
  }

  public final T getValue() {
    return value;
  }

  public final ApiError getError() {
    return error;
  }

  @Override
  public String toString() {
    return String.format("ApiData{value=%1$s,error=%2$s", value, error);
  }
}
