package com.tpago.movil;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.tpago.movil.util.StringHelper;

import static android.util.Patterns.EMAIL_ADDRESS;

/**
 * Email representation
 *
 * @author hecvasro
 */
@AutoValue
public abstract class Email implements Comparable<Email>, Parcelable {

  /**
   * Checks whether the given {@link String string} is a valid email address or not.
   *
   * @return True if the given {@link String string} is a valid email address, or otherwise false.
   */
  public static boolean isValid(String s) {
    if (StringHelper.isNullOrEmpty(s)) {
      return false;
    } else {
      return EMAIL_ADDRESS.matcher(s)
        .matches();
    }
  }

  /**
   * Creates an email from the given {@link String string}.
   *
   * @return An email created from the given {@link String string}.
   *
   * @throws IllegalArgumentException
   *   If {@code s} is not a {@link #isValid(String) valid} email address.
   */
  public static Email create(String s) {
    if (!isValid(s)) {
      throw new IllegalArgumentException(String.format("!isValid(%1$s)", s));
    }
    return new AutoValue_Email(s);
  }

  Email() {
  }

  public abstract String value();

  @Memoized
  @Override
  public abstract String toString();

  @Memoized
  @Override
  public abstract int hashCode();

  @Override
  public int compareTo(@NonNull Email that) {
    return this.value()
      .compareTo(that.value());
  }
}
