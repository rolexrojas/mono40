package com.gbh.movil.domain;

import android.support.annotation.NonNull;

public class Transaction {
  private final String type;
  private final String name;
  private final long date;
  private final double value;

  public Transaction(@NonNull String type, @NonNull String name, long date, double value) {
    this.type = type;
    this.name = name;
    this.date = date;
    this.value = value;
  }

  @NonNull
  public final String getType() {
    return type;
  }

  @NonNull
  public final String getName() {
    return name;
  }

  public final long getDate() {
    return date;
  }

  public final double getValue() {
    return value;
  }

  @Override
  public boolean equals(Object object) {
    return object != null && (super.equals(object) || (object instanceof Transaction &&
      ((Transaction) object).type.equals(type) && ((Transaction) object).name.equals(name)));
  }

  @Override
  public int hashCode() {
    return (type + name).hashCode();
  }

  @Override
  public String toString() {
    return "Transaction:{type='" + type + "',name='" + name + "',date='" + date + "',value="
      + value + "}";
  }
}
