package com.mono40.movil;

import android.os.Parcelable;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.mono40.movil.util.digit.DigitUtil;
import com.mono40.movil.util.digit.DigitValueCreator;
import com.mono40.movil.util.ObjectHelper;
import com.mono40.movil.util.StringHelper;
import com.mono40.movil.util.digit.DigitValueCreatorImpl;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.regex.Pattern;

/**
 * Phone number representation
 *
 * @author hecvasro
 */
@AutoValue
public abstract class PhoneNumber implements Comparable<PhoneNumber>, Parcelable {

  private static final Pattern PATTERN = Pattern.compile("\\A8[024]9[0-9]{7}\\z");
  private static final Pattern PATTERN_WITH_ADDITIONAL_CODE = Pattern.compile("\\A1?8[024]9[0-9]{7}\\z");
  /**
   * Checks whether the given {@link String string} is a valid phone number or not.
   *
   * @param shouldSanitize
   *   {@link Boolean Flag} that indicates whether the given {@link String string} should be
   *   sanitized or not.
   *
   * @return True if the given {@link String string} is a valid phone number, or otherwise false.
   */
  private static boolean isValid(String s, boolean shouldSanitize) {
    final String sanitizedString;
    if (shouldSanitize) {
      sanitizedString = DigitUtil.removeNonDigits(s);
    } else {
      sanitizedString = s;
    }
    if (StringHelper.isNullOrEmpty(sanitizedString)) {
      return false;
    } else {
      return PATTERN.matcher(sanitizedString)
        .matches();
    }
  }

  /**
   * Checks whether the given {@link String string} is a valid phone number(with additional code) or not.
   *
   * @return True if the given {@link String string} is a valid phone number (with additional code) , or otherwise false.
   */

  public static boolean isValidWithAdditionalCode(String s) {
    final String sanitizedString = DigitUtil.removeNonDigits(s);
    if (StringHelper.isNullOrEmpty(sanitizedString)) {
      return false;
    } else {
      return PATTERN_WITH_ADDITIONAL_CODE.matcher(sanitizedString)
              .matches();
    }
  }

  /**
   * Checks whether the given {@link String string} is a valid phone number or not.
   *
   * @return True if the given {@link String string} is a valid phone number, or otherwise false.
   */
  public static boolean isValid(String s) {
    return isValid(s, true);
  }

  /**
   * Inserts the separator into the given {@link StringBuilder string builder} if the {@link
   * String#length() length} of the given {@link String string} is greater than the value of the
   * given position.
   */
  private static void insertSeparatorIfGreaterThan(String s, int i, StringBuilder sb) {
    if (s.length() > i && i > 0) {
      sb.insert(i, '-');
    }
  }

  /**
   * Formats the given {@link String string} as a phone number.
   *
   * @return A {@link String string} formatted as a phone number.
   */
  public static String format(String s) {
    String sanitizedString = DigitUtil.removeNonDigits(s);
    if (sanitizedString.startsWith("1")) {
      sanitizedString = sanitizedString.replaceFirst("1", "");
    }
    final StringBuilder formattedStringBuilder = new StringBuilder(sanitizedString);
    insertSeparatorIfGreaterThan(sanitizedString, sanitizedString.length()-7, formattedStringBuilder);
    insertSeparatorIfGreaterThan(sanitizedString, sanitizedString.length()-3, formattedStringBuilder);

    if(sanitizedString.length() > 10){
      insertSeparatorIfGreaterThan(sanitizedString, sanitizedString.length()-10, formattedStringBuilder);
    }

    return formattedStringBuilder.toString();
  }

  /**
   * Creates an instance from the given {@link String string}.
   *
   * @return An instance created from the given {@link String string}.
   *
   * @throws IllegalArgumentException
   *   If {@code s} is not a {@link #isValid(String) valid} phone number.
   */
  public static PhoneNumber create(String s) {
    final String sanitizedString = DigitUtil.removeNonDigits(s);
    if (!isValid(sanitizedString, false) && !isValidWithAdditionalCode(sanitizedString)) {
      throw new IllegalArgumentException(String.format("!isValid(%1$s, false)", sanitizedString));
    }
    return new AutoValue_PhoneNumber(sanitizedString);
  }

  private static DigitValueCreator<PhoneNumber> creator(String value) {
    return DigitValueCreatorImpl.<PhoneNumber>builder()
      .canAdd((i) -> i < 10)
      .isValid((s) -> isValid(s, false))
      .formatter(PhoneNumber::format)
      .mapper(PhoneNumber::create)
      .value(value)
      .build();
  }

  public static DigitValueCreator<PhoneNumber> creator(PhoneNumber phoneNumber) {
    return creator(
      ObjectHelper.checkNotNull(phoneNumber, "phoneNumber")
        .value()
    );
  }

  public static DigitValueCreator<PhoneNumber> creator() {
    return creator("");
  }

  PhoneNumber() {
  }

  public abstract String value();

  @Memoized
  public String formattedValued() {
    return format(this.value());
  }

  @Memoized
  @Override
  public abstract String toString();

  @Memoized
  @Override
  public abstract int hashCode();

  @Override
  public int compareTo(@NonNull PhoneNumber that) {
    return this.value()
      .compareTo(that.value());
  }

  @IntDef({
    State.NONE,
    State.AFFILIATED,
    State.REGISTERED
  })
  @Retention(RetentionPolicy.SOURCE)
  public @interface State {

    int NONE = 1;
    int AFFILIATED = 2;
    int REGISTERED = 3;
  }
}
