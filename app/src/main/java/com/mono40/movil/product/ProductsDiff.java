package com.mono40.movil.product;

import com.google.auto.value.AutoValue;
import com.mono40.movil.util.ObjectHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class ProductsDiff {

  static ProductsDiff create(Products oldValue, Products newValue) {
    ObjectHelper.checkNotNull(oldValue, "oldValue");
    ObjectHelper.checkNotNull(newValue, "newValue");

    final List<Product> oldProducts = oldValue.all();
    final List<Product> newProducts = newValue.all();

    final List<Product> added = new ArrayList<>();
    final List<Product> updated = new ArrayList<>();
    final List<Product> removed = new ArrayList<>();

    for (Product product : newProducts) {
      if (oldProducts.contains(product)) {
        updated.add(product);
      } else {
        added.add(product);
      }
    }

    for (Product product : oldProducts) {
      if (!newProducts.contains(product)) {
        removed.add(product);
      }
    }

    return new AutoValue_ProductsDiff.Builder()
      .value(newValue)
      .added(added)
      .updated(updated)
      .removed(removed)
      .build();
  }

  ProductsDiff() {
  }

  abstract Products value();

  public abstract List<Product> added();

  public abstract List<Product> updated();

  public abstract List<Product> removed();

  @AutoValue.Builder
  static abstract class Builder {

    Builder() {
    }

    abstract Builder value(Products value);

    abstract Builder added(List<Product> added);

    abstract Builder updated(List<Product> updated);

    abstract Builder removed(List<Product> removed);

    abstract ProductsDiff build();
  }
}
