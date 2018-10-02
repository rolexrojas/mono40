package com.tpago.movil;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

/**
 * Currency representation
 *
 * @author hecvasro
 */
@AutoValue
public abstract class Currency implements Parcelable {

  public static final Currency USD = Currency.create("USD$");

  public static Currency create(String value) {
    return new AutoValue_Currency(value);
  }

  Currency() {
  }

  public abstract String value();
}
