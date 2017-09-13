package com.tpago.movil;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.tpago.movil.util.BuilderChecker;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.tpago.movil.DigitHelper.removeNonDigits;
import static com.tpago.movil.DigitHelper.toDigitList;
import static com.tpago.movil.DigitHelper.toDigitString;
import static com.tpago.movil.util.ObjectHelper.isNull;

/**
 * Helper for the creation of objects composed by a {@link String string} of {@link Digit digits}.
 *
 * @param <T>
 *   Type of object that can be created with a {@link String string} of {@link Digit digits}.
 *
 * @author hecvasro
 */
public final class DigitValueCreator<T> {

  static <T> Builder<T> builder() {
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
  private final Optional<Function<String, String>> formatFunction;

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
    this.digitString = toDigitString(this.digitList);
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
    if (this.additionPredicate.apply(this.digitList.size())) {
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
  public final boolean canCreateValue() {
    return this.formatPredicate.apply(this.digitString);
  }

  /**
   * Creates a value with the added {@link #digitList digits}.
   *
   * @return A value created with the added {@link #digitList digits}.
   *
   * @throws IllegalStateException
   *   If a value with the added {@link #digitList digits} cannot be created.
   */
  public final T createValue() {
    checkState(this.canCreateValue(), "!canCreateValue()");
    return this.mapperFunction.apply(this.digitString);
  }

  @Override
  public String toString() {
    if (this.formatFunction.isPresent()) {
      return this.formatFunction.get()
        .apply(this.digitString);
    } else {
      return this.digitString;
    }
  }

  public static final class Builder<T> {

    private Predicate<Integer> additionPredicate;
    private Predicate<String> formatPredicate;
    private Optional<Function<String, String>> formatFunction;
    private Function<String, T> mapperFunction;

    private List<Integer> digitList;
    private String digitString;

    private Builder() {
      this.digitList = new ArrayList<>();
      this.digitString = "";
    }

    public final Builder<T> additionPredicate(Predicate<Integer> additionPredicate) {
      this.additionPredicate = checkNotNull(additionPredicate, "additionPredicate == null");
      return this;
    }

    public final Builder<T> formatPredicate(Predicate<String> formatPredicate) {
      this.formatPredicate = checkNotNull(formatPredicate, "formatPredicate == null");
      return this;
    }

    public final Builder<T> formatFunction(Function<String, String> formatFunction) {
      this.formatFunction = Optional.of(formatFunction);
      return this;
    }

    public final Builder<T> mapperFunction(Function<String, T> mapperFunction) {
      this.mapperFunction = checkNotNull(mapperFunction, "mapperFunction == null");
      return this;
    }

    public final Builder<T> value(String value) {
      this.digitString = removeNonDigits(value);
      this.digitList = toDigitList(this.digitString);
      return this;
    }

    public final DigitValueCreator<T> build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("additionPredicate", isNull(this.additionPredicate))
        .addPropertyNameIfMissing("formatPredicate", isNull(this.formatFunction))
        .addPropertyNameIfMissing("formatFunction", isNull(this.formatFunction))
        .addPropertyNameIfMissing("mapperFunction", isNull(this.mapperFunction))
        .checkNoMissingProperties();
      return new DigitValueCreator<>(this);
    }
  }
}
