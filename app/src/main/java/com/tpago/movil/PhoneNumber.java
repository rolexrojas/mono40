package com.tpago.movil;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.util.Objects;
import com.tpago.movil.text.Texts;

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

  private static boolean isValid(String phoneNumber, boolean sanitize) {
    return PATTERN.matcher(sanitize ? sanitize(phoneNumber) : phoneNumber).matches();
  }

  public static boolean isValid(String phoneNumber) {
    return Texts.isNotEmpty(phoneNumber) && isValid(phoneNumber, true);
  }

  public static String format(String phoneNumber) {
    if (Texts.isEmpty(phoneNumber)) {
      return phoneNumber;
    } else {
      final String sanitizedPhoneNumber = sanitize(phoneNumber);
      final StringBuilder phoneNumberBuilder = new StringBuilder();
      for (int i = 0; i < sanitizedPhoneNumber.length(); i++) {
        if (i == 3 || i == 6) {
          phoneNumberBuilder.append('-');
        }
        phoneNumberBuilder.append(sanitizedPhoneNumber.charAt(i));
      }
      return phoneNumberBuilder.toString();
    }
  }

  public static PhoneNumber create(String value) {
    if (Objects.isNull(value)) {
      throw new NullPointerException("Null value");
    }
    final String sanitizedValue = sanitize(value);
    if (!isValid(sanitizedValue, false)) {
      throw new IllegalArgumentException("Invalid value");
    }
    return new AutoValue_PhoneNumber(sanitizedValue);
  }

  public abstract String getValue();

  public enum State {
    @SerializedName("1") NONE,
    @SerializedName("2") AFFILIATED,
    @SerializedName("3") REGISTERED
  }
}
