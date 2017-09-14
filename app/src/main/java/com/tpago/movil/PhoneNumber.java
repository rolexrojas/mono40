package com.tpago.movil;

import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;

import java.lang.annotation.Retention;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.tpago.movil.DigitHelper.removeNonDigits;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Phone number representation
 *
 * @author hecvasro
 */
@AutoValue
public abstract class PhoneNumber implements Comparable<PhoneNumber>, Parcelable {

  private static final Pattern PATTERN = Pattern.compile("\\A8[024]9[0-9]{7}\\z");

  /**
   * Checks whether the given {@link String string} is a valid phone number or not.
   *
   * @param shouldSanitize
   *   {@link Boolean Flag} that indicates whether the given {@link String string} should be
   *   sanitized or not.
   *
   * @return True if the given {@link String string} is a valid phone number, false otherwise.
   */
  private static boolean isValid(String s, boolean shouldSanitize) {
    final String sanitizedString;
    if (shouldSanitize) {
      sanitizedString = removeNonDigits(s);
    } else {
      sanitizedString = s;
    }
    if (isNullOrEmpty(sanitizedString)) {
      return false;
    } else {
      return PATTERN.matcher(sanitizedString)
        .matches();
    }
  }

  /**
   * Checks whether the given {@link String string} is a valid phone number or not.
   *
   * @return True if the given {@link String string} is a valid phone number, false otherwise.
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
    if (s.length() > i) {
      sb.insert(i, '-');
    }
  }

  /**
   * Formats the given {@link String string} as a phone number.
   *
   * @return A {@link String string} formatted as a phone number.
   */
  public static String format(String s) {
    final String sanitizedString = removeNonDigits(s);
    final StringBuilder formattedStringBuilder = new StringBuilder(sanitizedString);
    insertSeparatorIfGreaterThan(sanitizedString, 3, formattedStringBuilder);
    insertSeparatorIfGreaterThan(sanitizedString, 7, formattedStringBuilder);
    return formattedStringBuilder.toString();
  }

  /**
   * Creates a phone number from the given {@link String string}.
   *
   * @return A phone number created from the given {@link String string}.
   *
   * @throws IllegalArgumentException
   *   If {@code s} is not a {@link #isValid(String) valid} phone number.
   */
  public static PhoneNumber create(String s) {
    checkArgument(isValid(s), "!isValid(%1$s)", s);
    return new AutoValue_PhoneNumber(s);
  }

  public abstract String value();

  @Memoized
  public String formattedValued() {
    return format(this.value());
  }

  @Memoized
  @Override
  public abstract int hashCode();

  @Memoized
  @Override
  public abstract String toString();

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
  @Retention(SOURCE)
  public @interface State {

    int NONE = 1;
    int AFFILIATED = 2;
    int REGISTERED = 3;
  }
}
