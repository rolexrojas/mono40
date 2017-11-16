package com.tpago.movil.dep.api;

import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
@Deprecated
public final class DApiData<T> {

  private final T value;
  private final DApiError error;

  public static <T> DApiData<T> create(T value) {
    return new DApiData<>(value, null);
  }

  public static <T> DApiData<T> create(DApiError error) {
    return new DApiData<>(null, error);
  }

  private DApiData(T value, DApiError error) {
    if (ObjectHelper.isNull(error)) {
      this.value = ObjectHelper.checkNotNull(value, "value");
      this.error = null;
    } else {
      this.value = null;
      this.error = ObjectHelper.checkNotNull(error, "error");
    }
  }

  public final T getValue() {
    return value;
  }

  public final DApiError getError() {
    return error;
  }

  @Override
  public String toString() {
    return String.format("ApiData{value=%1$s,error=%2$s", value, error);
  }
}
