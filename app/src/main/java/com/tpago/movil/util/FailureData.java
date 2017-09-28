package com.tpago.movil.util;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;

/**
 * Failure data representation
 *
 * @author hecvasro
 */
@AutoValue
public abstract class FailureData {

  public static Builder builder() {
    return new AutoValue_FailureData.Builder();
  }

  FailureData() {
  }

  public abstract int code();

  public abstract String description();

  @Memoized
  @Override
  public abstract String toString();

  @Memoized
  @Override
  public abstract int hashCode();

  @AutoValue.Builder
  public static abstract class Builder {

    Builder() {
    }

    public abstract Builder code(int code);

    public abstract Builder description(String description);

    public abstract FailureData build();
  }
}
