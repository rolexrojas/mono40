package com.tpago.movil.util.digit;

/**
 * Helper for the creation of objects composed by a {@link String string} of {@link Digit digits}.
 *
 * @param <T>
 *   Type of object that can be created with a {@link String string} of {@link Digit digits}.
 *
 * @author hecvasro
 */
public interface DigitValueCreator<T> {

  /**
   * Removes all the {@link Digit digits} that were added.
   */
  void clear();

  /**
   * Adds the given {@link Digit digit}.
   *
   * @param digit
   *   {@link Digit} that will be added.
   */
  void addDigit(@Digit int digit);

  /**
   * Removes the last {@link Digit digit} that was added.
   */
  void removeLastDigit();

  /**
   * Checks whether a value can be created or not with the added digits.
   *
   * @return True if it can be created with the added digits, false otherwise.
   */
  boolean canCreate();

  /**
   * Creates a value with the added digits.
   *
   * @return A value created with the added digits.
   *
   * @throws IllegalStateException
   *   If a value with the added digits cannot be created.
   */
  T create();
}
