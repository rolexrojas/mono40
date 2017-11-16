package com.tpago.movil.company;

import android.net.Uri;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class LogoCatalog {

  static Builder builder() {
    return new AutoValue_LogoCatalog.Builder();
  }

  LogoCatalog() {
  }

  public abstract Uri colored24();

  public abstract Uri gray20();

  public abstract Uri gray36();

  public abstract Uri white36();

  @Memoized
  @Override
  public abstract String toString();

  @Memoized
  @Override
  public abstract int hashCode();

  @AutoValue.Builder
  static abstract class Builder {

    Builder() {
    }

    abstract Builder colored24(Uri uri);

    abstract Builder gray20(Uri uri);

    abstract Builder gray36(Uri uri);

    abstract Builder white36(Uri uri);

    abstract LogoCatalog build();
  }
}
