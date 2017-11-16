package com.tpago.movil;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.tpago.movil.util.StringHelper;

import java.util.Arrays;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class Name {

  public static Builder builder() {
    return new AutoValue_Name.Builder();
  }

  public static Name create(String first, String last) {
    return builder()
      .first(StringHelper.checkIsNotNullNorEmpty(first, "first"))
      .last(StringHelper.checkIsNotNullNorEmpty(last, "last"))
      .build();
  }

  Name() {
  }

  public abstract String first();

  public abstract String last();

  @Memoized
  @Override
  public String toString() {
    return StringHelper.join(" ", Arrays.asList(this.first(), this.last()));
  }

  @Memoized
  @Override
  public abstract int hashCode();

  @AutoValue.Builder
  public static abstract class Builder {

    Builder() {
    }

    public abstract Builder first(String first);

    public abstract Builder last(String last);

    public abstract Name build();
  }
}
