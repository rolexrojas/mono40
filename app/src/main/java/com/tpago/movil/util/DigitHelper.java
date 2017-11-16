package com.tpago.movil.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static java.lang.String.format;

/**
 * Collection of helpers for {@link Digit digits}.
 *
 * @author hecvasro
 */
public final class DigitHelper {

  private static final Pattern PATTERN_DIGITS = Pattern.compile("\\A\\d+\\z");

  public static boolean containsOnlyDigits(String s) {
    return PATTERN_DIGITS.matcher(StringHelper.emptyIfNull(s))
      .matches();
  }

  /**
   * Removes all non-{@link Digit digit} {@link Character characters} from the given {@link String
   * string}.
   *
   * @return A copy of the given {@link String string} with only {@link Digit digit} {@link
   * Character characters}.
   */
  public static String removeNonDigits(String s) {
    return StringHelper.emptyIfNull(s)
      .replaceAll("\\D", "");
  }

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
        return Digit.ZERO;
      case '1':
        return Digit.ONE;
      case '2':
        return Digit.TWO;
      case '3':
        return Digit.THREE;
      case '4':
        return Digit.FOUR;
      case '5':
        return Digit.FIVE;
      case '6':
        return Digit.SIX;
      case '7':
        return Digit.SEVEN;
      case '8':
        return Digit.EIGHT;
      case '9':
        return Digit.NINE;
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
    return StringHelper.join("", ObjectHelper.checkNotNull(digitList, "digitList"));
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
