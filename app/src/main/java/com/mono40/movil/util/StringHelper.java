package com.mono40.movil.util;

import java.util.List;

/**
 * Collection of helpers for {@link String strings}.
 *
 * @author hecvasro
 */
public final class StringHelper {

  public static StringBuilder builder() {
    return new StringBuilder();
  }

  /**
   * Checks whether the a {@link String string} is {@code null} or empty.
   *
   * @param s
   *   {@link String} that will be checked.
   *
   * @return True if the given {@link String string} is null or empty, or otherwise false.
   */
  public static boolean isNullOrEmpty(CharSequence s) {
    return ObjectHelper.isNull(s) || s.length() == 0;
  }

  public static String checkIsNotNullNorEmpty(String s, String argumentName) {
    if (StringHelper.isNullOrEmpty(s)) {
      throw new IllegalArgumentException(String.format("isNullOrEmpty(%1$s)", argumentName));
    }
    return s;
  }

  /**
   * Returns {@code null} if a given {@link String string} is empty, or otherwise the given one.
   *
   * @return {@code null} if a given {@link String string} is empty, or otherwise the given one.
   */
  public static String nullIfEmpty(String s) {
    return isNullOrEmpty(s) ? null : s;
  }

  /**
   * Returns an empty {@link String string} if a given {@link String string} is {@code null}, or
   * otherwise the given one.
   *
   * @return An empty {@link String string} if a given {@link String string} is {@code null}, or
   * otherwise the given one.
   */
  public static String emptyIfNull(String s) {
    return ObjectHelper.firstNonNull(s, "");
  }

  /**
   * Creates a {@link String string} that contains each given {@link Object object} joined by the
   * given delimiter.
   *
   * @param tokenList
   *   {@link List} of {@link Object tokens} that will be joined. Each one will be joined using its
   *   string representation, given by {@link Object#toString()}.
   *
   * @return A {@link String string} that contains each given {@link Object object} joined by the
   * given delimiter.
   *
   * @throws NullPointerException
   *   If the parameter {@code delimiter} is null.
   * @throws NullPointerException
   *   If the parameter {@code strings} is null.
   */
  public static <T> String join(CharSequence delimiter, List<T> tokenList) {
    ObjectHelper.checkNotNull(delimiter, "delimiter");
    ObjectHelper.checkNotNull(tokenList, "tokenList");

    final int size = tokenList.size();
    if (size == 0) {
      return "";
    } else {
      final StringBuilder builder = new StringBuilder();
      for (int i = 0; i < size; i++) {
        builder.append(
          tokenList.get(i)
            .toString()
        );
        if ((i + 1) < size) {
          builder.append(delimiter);
        }
      }
      return builder.toString();
    }
  }

  public static String repeat(String s, int length) {
    if (isNullOrEmpty(s)) {
      return "";
    } else {
      final StringBuilder builder = new StringBuilder();
      while (length > 0) {
        builder.append(s);
        length--;
      }
      return builder.toString();
    }
  }

  public static String extractOTPcode(String s) {
    String code = s.split(":")[1].replaceAll("[^0-9]+", "");
    return code;
  }

  private StringHelper() {
  }
}
