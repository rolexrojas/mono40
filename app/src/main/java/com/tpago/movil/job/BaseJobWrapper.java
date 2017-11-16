package com.tpago.movil.job;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class BaseJobWrapper {

  public static TypeAdapter<BaseJobWrapper> typeAdapter(Gson gson) {
    return new AutoValue_BaseJobWrapper.GsonTypeAdapter(gson);
  }

  public static Builder builder() {
    return new AutoValue_BaseJobWrapper.Builder();
  }

  BaseJobWrapper() {
  }

  public abstract String type();

  public abstract String data();

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

    public abstract Builder type(String type);

    public abstract Builder data(String data);

    public abstract BaseJobWrapper build();
  }
}
