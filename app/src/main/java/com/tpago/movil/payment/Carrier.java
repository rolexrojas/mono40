package com.tpago.movil.payment;

import android.net.Uri;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;

import java.util.Map;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class Carrier extends Partner {

  public static Builder builder() {
    return new AutoValue_Carrier.Builder();
  }

  Carrier() {
  }

  @Override
  public Type type() {
    return Type.CARRIER;
  }

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

    public abstract Builder id(String id);

    public abstract Builder code(int code);

    public abstract Builder name(String name);

    public abstract Builder logoTemplate(String logoTemplate);

    public abstract Builder styledLogos(Map<String, Uri> styledLogos);

    public abstract Carrier build();
  }
}
