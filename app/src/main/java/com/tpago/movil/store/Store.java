package com.tpago.movil.store;

import android.support.annotation.Nullable;

/**
 * Key-value store
 * <p>
 * Capable of associating any {@link String key} to any object.
 */
public interface Store {

  /**
   * Checks whether a {@link String key} is associated to a value or not.
   *
   * @return True if the given {@code key} is associated to a value, false otherwise.
   *
   * @throws IllegalArgumentException
   *   If {@code key} is {@code null} or empty.
   */
  boolean isSet(String key);

  /**
   * Associates a {@link String key} to a {@link T value}.
   *
   * @throws IllegalArgumentException
   *   If {@code key} is {@code null} or empty.
   * @throws NullPointerException
   *   If {@code value} is null.
   */
  <T> void set(String key, T value);

  /**
   * Gets the {@link T value} associated to a {@link String key}.
   *
   * @return {@link T Value} associated to the given {@link String key}, or {@code null} otherwise.
   *
   * @throws IllegalArgumentException
   *   If {@code key} is {@code null} or empty.
   * @throws NullPointerException
   *   If the given {@code valueType} is null.
   * @throws ClassCastException
   *   If the given {@code valueType} is not the type of the value associated to the given {@code
   *   key}.
   */
  @Nullable
  <T> T get(String key, Class<T> valueType);

  /**
   * Removes the value associated to a {@link String key}.
   *
   * @throws IllegalArgumentException
   *   If {@code key} is {@code null} or empty.
   */
  void remove(String key);
}
