package com.tpago.movil;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.nullToEmpty;
import static com.tpago.movil.Digit.EIGHT;
import static com.tpago.movil.Digit.FIVE;
import static com.tpago.movil.Digit.FOUR;
import static com.tpago.movil.Digit.NINE;
import static com.tpago.movil.Digit.ONE;
import static com.tpago.movil.Digit.SEVEN;
import static com.tpago.movil.Digit.SIX;
import static com.tpago.movil.Digit.THREE;
import static com.tpago.movil.Digit.TWO;
import static com.tpago.movil.Digit.ZERO;
import static java.lang.Character.forDigit;
import static java.lang.String.format;

/**
 * Collection of helpers for {@link Digit digits}.
 *
 * @author hecvasro
 */
public final class DigitHelper {

  private static final char CHAR_ZERO = toChar(ZERO);
  private static final char CHAR_NINE = toChar(NINE);

  /**
   * Transforms the given {@link Character charater} into its {@link Digit digit} representation.
   *
   * @return {@link Digit} representation of the given {@link Character character}.
   *
   * @throws IllegalArgumentException
   *   If {@code character} is not a {@link Digit digit}.
   */
  @Digit
  public static int toDigit(char character) {
    switch (character) {
      case '0':
        return ZERO;
      case '1':
        return ONE;
      case '2':
        return TWO;
      case '3':
        return THREE;
      case '4':
        return FOUR;
      case '5':
        return FIVE;
      case '6':
        return SIX;
      case '7':
        return SEVEN;
      case '8':
        return EIGHT;
      case '9':
        return NINE;
      default:
        throw new IllegalArgumentException(format("%1$s < '0' || %1$s > '9'", character));
    }
  }

  /**
   * Transforms the given {@link List list} of {@link Digit digits} into its {@link String string}
   * representation.
   *
   * @return {@link String} representation of the given {@link List list} of {@link Digit digits}.
   *
   * @throws NullPointerException
   *   If {@code digitList} is null.
   */
  public static String toDigitString(List<Integer> digitList) {
    return Joiner.on("")
      .join(checkNotNull(digitList, "isNull(digitList)"));
  }

  /**
   * Transforms the given {@link Digit digit} into its {@link Character character} representation.
   *
   * @return {@link Character} representation of the given {@link Digit digit}.
   */
  private static char toChar(@Digit int digit) {
    return forDigit(digit, 10);
  }

  /**
   * Removes all non-{@link Digit digit} {@link Character characters} from the given {@link String
   * string}.
   *
   * @return A copy of the given {@link String string} with only {@link Digit digit} {@link
   * Character characters}.
   */
  public static String removeNonDigits(String s) {
    return CharMatcher.inRange(CHAR_ZERO, CHAR_NINE)
      .retainFrom(nullToEmpty(s));
  }

  /**
   * Transforms the given {@link String string} to a {@link List list} of {@link Digit digits}.
   *
   * @return A {@link List list} of {@link Digit digits} created from the given {@link String
   * string}.
   */
  public static List<Integer> toDigitList(String s) {
    final String digitString = removeNonDigits(s);
    final List<Integer> digitList = new ArrayList<>();
    for (int i = 0; i < digitString.length(); i++) {
      digitList.add(toDigit(digitString.charAt(i)));
    }
    return digitList;
  }

  private DigitHelper() {
  }
}
