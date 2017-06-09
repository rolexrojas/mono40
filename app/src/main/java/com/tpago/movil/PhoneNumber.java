package com.tpago.movil;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.text.Texts;
import com.tpago.movil.util.Preconditions;

import java.util.regex.Pattern;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class PhoneNumber {
  private static final Pattern SANITIZER = Pattern.compile("[\\D]");

  private static final Pattern PATTERN = Pattern.compile("\\A8[024]9[0-9]{7}\\z");

  private static String sanitize(String value) {
    return SANITIZER.matcher(value).replaceAll("");
  }

  private static boolean checkIfValid(String phoneNumber, boolean sanitize) {
    return PATTERN.matcher(sanitize ? sanitize(phoneNumber) : phoneNumber).matches();
  }

  public static boolean checkIfValid(String phoneNumber) {
    return Texts.checkIfNotEmpty(phoneNumber) && checkIfValid(phoneNumber, true);
  }

  public static String format(String phoneNumber) {
    if (Texts.checkIfEmpty(phoneNumber)) {
      return phoneNumber;
    } else {
      final StringBuilder builder = new StringBuilder();
      final String sanitizedValue = sanitize(phoneNumber);
      for (int i = 0; i < sanitizedValue.length(); i++) {
        if (i == 3 || i == 6) {
          builder.append('-');
        }
        builder.append(sanitizedValue.charAt(i));
      }
      return builder.toString();
    }
  }

  public static PhoneNumber create(String value) {
    Preconditions.assertNotNull(value, "value == null");
    final String sanitizedValue = sanitize(value);
    if (!checkIfValid(sanitizedValue, false)) {
      throw new IllegalArgumentException("checkIfValid(sanitizedValue, false) == false");
    }
    return new AutoValue_PhoneNumber(sanitizedValue);
  }

  public abstract String getValue();

  @Memoized
  public String formattedValued() {
    return format(getValue());
  }

  public enum State {
    @SerializedName("1") NONE,
    @SerializedName("2") AFFILIATED,
    @SerializedName("3") REGISTERED
  }
}
