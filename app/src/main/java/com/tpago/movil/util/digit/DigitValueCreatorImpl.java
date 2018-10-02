package com.tpago.movil.util.digit;

import android.support.annotation.Nullable;

import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.function.Function;
import com.tpago.movil.util.function.Predicate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hecvasro
 */
public final class DigitValueCreatorImpl<T> implements DigitValueCreator<T> {

  public static <T> Builder<T> builder() {
    return new Builder<>();
  }

  /**
   * {@link Predicate} that evaluates whether more {@link Digit digits} can be added or not.
   */
  private final Predicate<Integer> canAdd;

  /**
   * {@link Predicate} that evaluates whether the current {@link String string} of {@link Digit
   * digits} is valid or not.
   */
  private final Predicate<String> isValid;

  /**
   * {@link Function} that formats the current {@link String string} of {@link Digit digits}.
   */
  @Nullable
  private final Function<String, String> formatter;

  /**
   * {@link Function} that transforms the current {@link String string} of {@link Digit digits} to
   * the given object type.
   */
  private final Function<String, T> mapper;

  private List<Integer> ds;
  private String s;

  private DigitValueCreatorImpl(Builder<T> builder) {
    this.canAdd = builder.canAdd;
    this.isValid = builder.isValid;
    this.formatter = builder.formatter;
    this.mapper = builder.mapper;

    this.ds = builder.digitList;
    this.s = builder.digitString;
  }

  private void updateValue() {
    this.s = DigitUtil.toDigitString(this.ds);
  }

  @Override
  public void clear() {
    if (!this.ds.isEmpty()) {
      this.ds.clear();
      this.updateValue();
    }
  }

  @Override
  public void addDigit(@Digit int digit) {
    if (this.canAdd.test(this.ds.size())) {
      this.ds.add(digit);
      this.updateValue();
    }
  }

  @Override
  public void removeLastDigit() {
    final int size = this.ds.size();
    if (size > 0) {
      this.ds.remove(size - 1);
      this.updateValue();
    }
  }

  @Override
  public boolean canCreate() {
    return this.isValid.test(this.s);
  }

  @Override
  public T create() {
    if (!this.canCreate()) {
      throw new IllegalStateException("!canCreate()");
    }
    return this.mapper.apply(this.s);
  }

  @Override
  public String toString() {
    if (ObjectHelper.isNull(this.formatter)) {
      return this.s;
    } else {
      return this.formatter.apply(this.s);
    }
  }

  public static final class Builder<T> {

    private Predicate<Integer> canAdd;
    private Predicate<String> isValid;
    private Function<String, String> formatter;
    private Function<String, T> mapper;

    private List<Integer> digitList;
    private String digitString;

    private Builder() {
      this.digitList = new ArrayList<>();
      this.digitString = "";
    }

    public final Builder<T> canAdd(Predicate<Integer> predicate) {
      this.canAdd = ObjectHelper.checkNotNull(predicate, "canAdd");
      return this;
    }

    public final Builder<T> isValid(Predicate<String> predicate) {
      this.isValid = ObjectHelper.checkNotNull(predicate, "isValid");
      return this;
    }

    public final Builder<T> formatter(Function<String, String> formatter) {
      this.formatter = ObjectHelper.checkNotNull(formatter, "formatter");
      return this;
    }

    public final Builder<T> mapper(Function<String, T> mapper) {
      this.mapper = ObjectHelper.checkNotNull(mapper, "mapper");
      return this;
    }

    public final Builder<T> value(String value) {
      this.digitString = DigitUtil.removeNonDigits(value);
      this.digitList = DigitUtil.toDigitList(this.digitString);
      return this;
    }

    public final DigitValueCreator<T> build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("canAdd", ObjectHelper.isNull(this.canAdd))
        .addPropertyNameIfMissing("isValid", ObjectHelper.isNull(this.isValid))
        .addPropertyNameIfMissing("mapper", ObjectHelper.isNull(this.mapper))
        .checkNoMissingProperties();
      return new DigitValueCreatorImpl<>(this);
    }
  }
}
