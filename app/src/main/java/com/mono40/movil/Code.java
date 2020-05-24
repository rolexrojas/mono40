package com.mono40.movil;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.mono40.movil.util.digit.DigitUtil;
import com.mono40.movil.util.digit.DigitValueCreator;
import com.mono40.movil.util.ObjectHelper;
import com.mono40.movil.util.StringHelper;
import com.mono40.movil.util.digit.DigitValueCreatorImpl;

/**
 * FailureCode representation
 *
 * @author hecvasro
 */
@AutoValue
public abstract class Code {

  private static final int LENGTH = 4;

  public static Code create(String s) {
    final String ss = DigitUtil.removeNonDigits(s);
    if (s.length() != LENGTH) {
      throw new IllegalArgumentException(String.format("\"%1$s\".length() != %2$s", ss, LENGTH));
    }
    return new AutoValue_Code(ss);
  }

  private static DigitValueCreator<Code> creator(String value) {
    return DigitValueCreatorImpl.<Code>builder()
      .canAdd((i) -> i < LENGTH)
      .isValid((s) -> s.length() == LENGTH)
      .formatter((s) -> StringHelper.repeat("•", s.length()))
      .mapper(AutoValue_Code::new)
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
