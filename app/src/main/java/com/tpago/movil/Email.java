package com.tpago.movil;

import android.util.Patterns;

import com.google.auto.value.AutoValue;
import com.tpago.movil.util.Objects;
import com.tpago.movil.text.Texts;

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

  private static boolean isValid(String email, boolean sanitize) {
    return PATTERN.matcher(sanitize ? sanitize(email) : email).matches();
  }

  public static boolean isValid(String email) {
    return Texts.isNotEmpty(email) && isValid(email, true);
  }

  public static Email create(String value) {
    if (Objects.isNull(value)) {
      throw new NullPointerException("Null value");
    }
    final String sanitizedValue = sanitize(value);
    if (!isValid(sanitizedValue, false)) {
      throw new IllegalArgumentException("Invalid value");
    }
    return new AutoValue_Email(sanitizedValue);
  }

  public abstract String getValue();
}
