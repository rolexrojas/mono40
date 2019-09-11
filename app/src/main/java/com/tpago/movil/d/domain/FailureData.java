package com.tpago.movil.d.domain;

import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;

/**
 * @author hecvasro
 */
@Deprecated
@AutoValue
public abstract class FailureData<C> {
  public static <C> FailureData<C> create(C code, @Nullable String description) {
    return new AutoValue_FailureData<>(code, description);
  }

  public static <C> FailureData<C> create(C code) {
    return create(code, null);
  }

  public abstract C getCode();
  @Nullable public abstract String getDescription();
}
