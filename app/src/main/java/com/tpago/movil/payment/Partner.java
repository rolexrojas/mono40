package com.tpago.movil.payment;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.tpago.movil.company.Company;

import java.util.Map;

/**
 * Partner representation
 *
 * @author hecvasro
 */
@AutoValue
public abstract class Partner extends Company implements Comparable<Partner> {

  public static Builder builder() {
    throw new UnsupportedOperationException("not implemented");
  }

  Partner() {
  }

  public abstract Type type();

  @Memoized
  @Override
  public abstract int hashCode();

  @Memoized
  @Override
  public abstract String toString();

  @Override
  public int compareTo(@NonNull Partner that) {
    throw new UnsupportedOperationException("not implemented");
  }

  /**
   * Partner type enumeration
   */
  public enum Type {
    CARRIER,
    PROVIDER
  }

  @AutoValue.Builder
  public static abstract class Builder {

    Builder() {
    }

    public abstract Builder type(Type type);

    public abstract Builder id(String id);

    public abstract Builder code(int code);

    public abstract Builder name(String name);

    public abstract Builder logoUriTemplate(String logoUriTemplate);

    public abstract Builder logoUriMap(Map<String, Uri> logoUriMap);

    public abstract Partner build();
  }
}
