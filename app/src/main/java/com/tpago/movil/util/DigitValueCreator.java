package com.tpago.movil.util;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Helper for the creation of objects composed by a {@link String string} of {@link Digit digits}.
 *
 * @param <T>
 *   Type of object that can be created with a {@link String string} of {@link Digit digits}.
 *
 * @author hecvasro
 */
public final class DigitValueCreator<T> {

  public static <T> Builder<T> builder() {
    return new Builder<>();
  }

  /**
   * {@link Predicate} that evaluates whether more {@link Digit digits} can be added or not.
   */
  private final Predicate<Integer> additionPredicate;

  /**
   * {@link Predicate} that evaluates whether the current {@link String string} of {@link Digit
   * digits} is valid or not.
   */
  private final Predicate<String> formatPredicate;

  /**
   * {@link Function} that formats the current {@link String string} of {@link Digit digits}.
   */
  @Nullable
  private final Function<String, String> formatFunction;

  /**
   * {@link Function} that transforms the current {@link String string} of {@link Digit digits} to
   * the given object type.
   */
  private final Function<String, T> mapperFunction;

  private List<Integer> digitList;
  private String digitString;

  private DigitValueCreator(Builder<T> builder) {
    this.additionPredicate = builder.additionPredicate;
    this.formatPredicate = builder.formatPredicate;
    this.formatFunction = builder.formatFunction;
    this.mapperFunction = builder.mapperFunction;

    this.digitList = builder.digitList;
    this.digitString = builder.digitString;
  }

  private void updateValue() {
    this.digitString = DigitHelper.toDigitString(this.digitList);
  }

  /**
   * Removes all the {@link Digit digits} that were added.
   */
  public final void clear() {
    if (!this.digitList.isEmpty()) {
      this.digitList.clear();
      this.updateValue();
    }
  }

  /**
   * Adds the given {@link Digit digit} to the {@link #digitList list}.
   *
   * @param digit
   *   {@link Digit} that will be added.
   */
  public final void addDigit(@Digit int digit) {
    if (this.additionPredicate.test(this.digitList.size())) {
      this.digitList.add(digit);
      this.updateValue();
    }
  }

  /**
   * Removes the last {@link Digit digit} that was added to the {@link #digitList list}.
   */
  public final void removeLastDigit() {
    final int size = this.digitList.size();
    if (size > 0) {
      this.digitList.remove(size - 1);
      this.updateValue();
    }
  }

  /**
   * Checks whether a value can be created or not with the added {@link #digitList digits}.
   *
   * @return True if it can be created with the added {@link #digitList digits}, false otherwise.
   */
  public final boolean canCreate() {
    return this.formatPredicate.test(this.digitString);
  }

  /**
   * Creates a value with the added {@link #digitList digits}.
   *
   * @return A value created with the added {@link #digitList digits}.
   *
   * @throws IllegalStateException
   *   If a value with the added {@link #digitList digits} cannot be created.
   */
  public final T create() {
    if (!this.canCreate()) {
      throw new IllegalStateException("!canCreateValue()");
    }
    return this.mapperFunction.apply(this.digitString);
  }

  @Override
  public String toString() {
    if (ObjectHelper.isNull(this.formatFunction)) {
      return this.digitString;
    } else {
      return this.formatFunction.apply(this.digitString);
    }
  }

  public static final class Builder<T> {

    private Predicate<Integer> additionPredicate;
    private Predicate<String> formatPredicate;
    private Function<String, String> formatFunction;
    private Function<String, T> mapperFunction;

    private List<Integer> digitList;
    private String digitString;

    private Builder() {
      this.digitList = new ArrayList<>();
      this.digitString = "";
    }

    public final Builder<T> additionPredicate(Predicate<Integer> additionPredicate) {
      this.additionPredicate = ObjectHelper.checkNotNull(additionPredicate, "additionPredicate");
      return this;
    }

    public final Builder<T> formatPredicate(Predicate<String> formatPredicate) {
      this.formatPredicate = ObjectHelper.checkNotNull(formatPredicate, "formatPredicate");
      return this;
    }

    public final Builder<T> formatFunction(Function<String, String> formatFunction) {
      this.formatFunction = ObjectHelper.checkNotNull(formatFunction, "formatFunction");
      return this;
    }

    public final Builder<T> mapperFunction(Function<String, T> mapperFunction) {
      this.mapperFunction = ObjectHelper.checkNotNull(mapperFunction, "mapperFunction == null");
      return this;
    }

    public final Builder<T> value(String value) {
      this.digitString = DigitHelper.removeNonDigits(value);
      this.digitList = DigitHelper.toDigitList(this.digitString);
      return this;
    }

    public final DigitValueCreator<T> build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("additionPredicate", ObjectHelper.isNull(this.additionPredicate))
        .addPropertyNameIfMissing("formatPredicate", ObjectHelper.isNull(this.formatPredicate))
        .addPropertyNameIfMissing("mapperFunction", ObjectHelper.isNull(this.mapperFunction))
        .checkNoMissingProperties();

      return new DigitValueCreator<>(this);
    }
  }
}
