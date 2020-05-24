package com.mono40.movil.util;

import androidx.annotation.Nullable;

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

  @Nullable
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

    public abstract Builder description(@Nullable String description);

    public abstract FailureData build();
  }
}
