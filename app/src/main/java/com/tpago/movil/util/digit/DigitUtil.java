package com.tpago.movil.util.digit;

import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static java.lang.String.format;

/**
 * Collection of helpers for {@link Digit digits}.
 *
 * @author hecvasro
 */
public final class DigitUtil {

  private static final Pattern PATTERN = Pattern.compile("\\A\\d+\\z");

  public static boolean containsOnlyDigits(String s) {
    return PATTERN.matcher(StringHelper.emptyIfNull(s))
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

  @Digit
  public static int toDigit(CharSequence cs) {
    final String s = ObjectHelper.checkNotNull(cs, "cs")
      .toString();
    final int d = Integer.parseInt(s);
    if (d < 0 || d > 9) {
      throw new IllegalArgumentException(format("%1$s < '0' || %1$s > '9'", d));
    }
    return d;
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

  public static String getLast4Digits(String s) {
    StringHelper.checkIsNotNullNorEmpty(s, "s");
    final String digitString = removeNonDigits(s);
    final int digitStringLength = digitString.length();
    if (digitStringLength < 4) {
      throw new IllegalArgumentException("digitStringLength < 4");
    }
    return digitString.substring(digitStringLength - 4, digitStringLength);
  }

  private DigitUtil() {
  }
}
