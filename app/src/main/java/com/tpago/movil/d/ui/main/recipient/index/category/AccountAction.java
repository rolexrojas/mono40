package com.tpago.movil.d.ui.main.recipient.index.category;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;

import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static com.tpago.movil.util.DigitHelper.removeNonDigits;
import static com.tpago.movil.d.ui.main.recipient.index.category.Action.Type.ADD_ACCOUNT;
import static com.tpago.movil.d.ui.main.recipient.index.category.Action.Type.TRANSACTION_WITH_ACCOUNT;

/**
 * @author hecvasro
 */
@AutoValue
abstract class AccountAction extends Action {

  private static final Pattern PATTERN = Pattern.compile("\\A[0-9]{3,}\\z");

  /**
   * Checks whether the given {@link String string} can be a {@link Product product} number or not.
   *
   * @param shouldSanitize
   *   {@link Boolean Flag} that indicates whether the given {@link String string} should be
   *   sanitized or not.
   *
   * @return True if the given {@link String string} is a valid {@link Product product} number,
   * false otherwise.
   */
  private static boolean isProductNumber(String s, boolean shouldSanitize) {
    return PATTERN.matcher(shouldSanitize ? removeNonDigits(s) : s)
      .matches();
  }

  /**
   * Checks whether the given {@link String string} can be a {@link Product product} number or not.
   *
   * @return True if the given {@link String string} is a valid {@link Product product} number,
   * false otherwise.
   */
  static boolean isProductNumber(String s) {
    return isProductNumber(s, true);
  }

  /**
   * Creates an instance from the given {@link String string}.
   *
   * @return Instance created from the given {@link String string}.
   *
   * @throws IllegalArgumentException
   *   If {@code type} is not {@link Type#TRANSACTION_WITH_ACCOUNT} or {@link Type#ADD_ACCOUNT}.
   * @throws IllegalArgumentException
   *   If {@code s} is not a {@link #isProductNumber(String) valid} {@link Product product} number.
   */
  static AccountAction create(Type type, String s) {
    checkArgument(
      type == TRANSACTION_WITH_ACCOUNT || type == ADD_ACCOUNT,
      "%1$s != %2$s && %1$s != %3$s",
      type,
      TRANSACTION_WITH_ACCOUNT,
      ADD_ACCOUNT
    );
    final String sanitizedString = removeNonDigits(s);
    checkArgument(
      isProductNumber(sanitizedString, false),
      "!isValid(%1$s, false)",
      sanitizedString
    );
    return new AutoValue_AccountAction(type, sanitizedString);
  }

  AccountAction() {
  }

  abstract String number();

  @Memoized
  @Override
  public abstract int hashCode();

  @Memoized
  @Override
  public abstract String toString();
}
