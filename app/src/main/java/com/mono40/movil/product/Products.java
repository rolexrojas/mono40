package com.mono40.movil.product;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class Products {

  public static TypeAdapter<Products> typeAdapter(Gson gson) {
    return new AutoValue_Products.GsonTypeAdapter(gson);
  }

  public static Builder builder() {
    return new AutoValue_Products.Builder();
  }

  static Products create() {
    return builder()
      .accounts(new ArrayList<>())
      .creditCards(new ArrayList<>())
      .loans(new ArrayList<>())
      .build();
  }

  Products() {
  }

  abstract List<Product> accounts();

  @Memoized
  List<Product> accountsSorted() {
    return Observable.fromIterable(this.accounts())
      .toSortedList(Product::compareTo)
      .blockingGet();
  }

  abstract List<Product> creditCards();

  @Memoized
  List<Product> creditCardsSorted() {
    return Observable.fromIterable(this.creditCards())
      .toSortedList(Product::compareTo)
      .blockingGet();
  }

  abstract List<Product> loans();

  @Memoized
  List<Product> loansSorted() {
    return Observable.fromIterable(this.loans())
      .toSortedList(Product::compareTo)
      .blockingGet();
  }

  @Memoized
  List<Product> all() {
    return Observable.fromIterable(this.accounts())
      .concatWith(Observable.fromIterable(this.creditCards()))
      .concatWith(Observable.fromIterable(this.loans()))
      .toList()
      .blockingGet();
  }

  @Memoized
  List<Product> allSorted() {
    return Observable.fromIterable(this.all())
      .toSortedList(Product::compareTo)
      .blockingGet();
  }

  @AutoValue.Builder
  public static abstract class Builder {

    Builder() {
    }

    public abstract Builder accounts(List<Product> accounts);

    public abstract Builder creditCards(List<Product> creditCards);

    public abstract Builder loans(List<Product> loans);

    public abstract Products build();
  }
}
