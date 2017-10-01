package com.tpago.movil.domain;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.tpago.movil.util.DigitHelper;
import com.tpago.movil.util.DigitValueCreator;
import com.tpago.movil.util.ObjectHelper;

/**
 * FailureCode representation
 *
 * @author hecvasro
 */
@AutoValue
public abstract class Code {

  private static final int LENGTH = 4;

  public static Code create(String s) {
    final String ss = DigitHelper.removeNonDigits(s);
    if (s.length() != LENGTH) {
      throw new IllegalArgumentException(String.format("\"%1$s\".length() != %2$s", ss, LENGTH));
    }
    return new AutoValue_Code(ss);
  }

  private static DigitValueCreator<Code> creator(String value) {
    return DigitValueCreator.<Code>builder()
      .additionPredicate((i) -> i < LENGTH)
      .formatPredicate((s) -> s.length() == LENGTH)
      .mapperFunction(AutoValue_Code::new)
      .value(value)
      .build();
  }

  public static DigitValueCreator<Code> creator(Code code) {
    return creator(
      ObjectHelper.checkNotNull(code, "code")
        .value()
    );
  }

  public static DigitValueCreator<Code> creator() {
    return creator("");
  }

  Code() {
  }

  public abstract String value();

  @Memoized
  public char[] toCharArray() {
    final String value = this.value();
    final int length = value.length();
    final char[] array = new char[length];
    for (int i = 0; i < length; i++) {
      array[i] = value.charAt(i);
    }
    return array;
  }

  @Memoized
  @Override
  public abstract String toString();

  @Memoized
  @Override
  public abstract int hashCode();
}
