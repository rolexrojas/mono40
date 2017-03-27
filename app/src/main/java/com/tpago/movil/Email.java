package com.tpago.movil;

import android.util.Patterns;

import com.google.auto.value.AutoValue;
import com.tpago.movil.text.Texts;
import com.tpago.movil.util.Preconditions;

import java.util.regex.Pattern;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class Email {
  private static final Pattern SANITIZER = Pattern.compile("[\\s]");

  private static final Pattern PATTERN = Patterns.EMAIL_ADDRESS;

  private static String sanitize(String value) {
    return SANITIZER.matcher(value).replaceAll("");
  }

  private static boolean checkIfValue(String email, boolean sanitize) {
    return PATTERN.matcher(sanitize ? sanitize(email) : email).matches();
  }

  public static boolean checkIfValue(String email) {
    return Texts.isNotEmpty(email) && checkIfValue(email, true);
  }

  public static Email create(String value) {
    Preconditions.checkNotNull(value, "value == null");
    final String sanitizedValue = sanitize(value);
    if (!checkIfValue(sanitizedValue, false)) {
      throw new IllegalArgumentException("checkIfValid(sanitizedValue, false) == false");
    }
    return new AutoValue_Email(sanitizedValue);
  }

  public abstract String getValue();
}
