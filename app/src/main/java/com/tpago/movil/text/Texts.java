package com.tpago.movil.text;

import com.tpago.movil.util.Objects;

/**
 * @author hecvasro
 */
public final class Texts {
  public static boolean isEmpty(CharSequence s) {
    return Objects.isNull(s) || s.length() == 0;
  }

  public static boolean isNotEmpty(CharSequence s) {
    return !isEmpty(s);
  }

  public static String nullIfEmpty(String s) {
    return isNotEmpty(s) ? s : null;
  }

  public static String join(String delimiter, Object... tokens) {
    boolean firstTime = true;
    final StringBuilder builder = new StringBuilder();
    for (Object token : tokens) {
      if (firstTime) {
        firstTime = false;
      } else {
        builder.append(delimiter);
      }
      builder.append(token.toString());
    }
    return builder.toString();
  }

  private Texts() {
    throw new AssertionError("Cannot be instantiated");
  }
}
