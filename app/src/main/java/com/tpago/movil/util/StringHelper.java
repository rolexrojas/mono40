package com.tpago.movil.util;

import java.util.List;

import static com.tpago.movil.util.ObjectHelper.checkNotNull;
import static com.tpago.movil.util.ObjectHelper.isNull;

/**
 * Collection of helpers for {@link String strings}.
 *
 * @author hecvasro
 */
public final class StringHelper {

  /**
   * Checks if the given {@link String string} is null or empty.
   *
   * @param s
   *   {@link String} that will be checked.
   *
   * @return True if the given {@link String string} is null or empty, false otherwise.
   */
  public static boolean isNullOrEmpty(CharSequence s) {
    return isNull(s) || s.length() == 0;
  }

  /**
   * Creates a {@link String string} that contains each given {@link
   * Object object} joined by the given delimiter.
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
    checkNotNull(delimiter, "delimiter");
    checkNotNull(tokenList, "tokenList");

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

  private StringHelper() {
  }
}
