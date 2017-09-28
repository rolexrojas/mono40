package com.tpago.movil.domain;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.tpago.movil.util.DigitValueCreator;
import com.tpago.movil.util.ObjectHelper;

/**
 * Code representation
 *
 * @author hecvasro
 */
@AutoValue
public abstract class Code {

  private static final int LENGTH = 4;

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
  @Override
  public abstract String toString();

  @Memoized
  @Override
  public abstract int hashCode();
}
