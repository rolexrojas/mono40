package com.tpago.movil.lib;

import com.google.auto.value.AutoValue;
import com.tpago.movil.util.StringHelper;

import java.util.regex.Pattern;

/**
 * @author Hector Vasquez
 */
@AutoValue
public abstract class Password {

  private static final Pattern PATTERN = Pattern.compile("\\A[A-Za-z\\d]{8,}\\z");

  /**
   * Checks whether the given {@link String string} is a valid password or not.
   *
   * @param shouldSanitize
   *   {@link Boolean Flag} that indicates whether the given {@link String string} should be
   *   sanitized or not.
   *
   * @return True if the given {@link String string} is a valid password, or otherwise false.
   */
  private static boolean isValid(String s, boolean shouldSanitize) {
    final String sanitizedString;
    if (shouldSanitize) {
      sanitizedString = StringHelper.emptyIfNull(s);
    } else {
      sanitizedString = s;
    }
    if (StringHelper.isNullOrEmpty(sanitizedString)) {
      return false;
    }
    return PATTERN.matcher(sanitizedString)
      .matches();
  }

  /**
   * Checks whether the given {@link String string} is a valid password or not.
   *
   * @return True if the given {@link String string} is a valid pssword, or otherwise false.
   */
  public static boolean isValid(String s) {
    return isValid(s, true);
  }


  /**
   * Creates an instance from the given {@link String string}.
   *
   * @return An instance created from the given {@link String string}.
   *
   * @throws IllegalArgumentException
   *   If {@code s} is not a {@link #isValid(String) valid} password.
   */
  public static Password create(String s) {
    return new AutoValue_Password(StringHelper.emptyIfNull(s));
//    final String sanitizedString = StringHelper.emptyIfNull(s);
//    if (!isValid(sanitizedString, false)) {
//      throw new IllegalArgumentException(String.format("!isValid(%1$s, false)", sanitizedString));
//    }
//    return new AutoValue_Password(sanitizedString);
  }

  Password() {
  }

  public abstract String value();
}
