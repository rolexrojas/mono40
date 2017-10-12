package com.tpago.movil.domain.balance;

import android.support.annotation.Nullable;

import com.tpago.movil.function.Consumer;

import java.util.List;

/**
 * In memory store for a {@link Balance balance}
 * <p>
 * {@link Consumer Consumers} that accept changes can be added and removed.
 *
 * @param <T>
 *   Type of {@link Balance balance} stored.
 *
 * @author hecvasro
 */
public class BalanceStore<T extends Balance> {

  public static <T extends Balance> BalanceStore<T> create() {
    throw new UnsupportedOperationException("not implemented");
  }

  private T balance;
  private final List<Consumer<T>> consumers;

  private BalanceStore() {
    throw new UnsupportedOperationException("not implemented");
  }

  /**
   * Clears the current {@link Balance balance} and notifies each {@link Consumer consumer} that it
   * was cleared.
   */
  public final void clear() {
    throw new UnsupportedOperationException("not implemented");
  }

  /**
   * Sets the given {@link Balance balance} and notifies each {@link Consumer consumer} that it was
   * set.
   *
   * @param balance
   *   {@link Balance} that will be stored.
   *
   * @throws NullPointerException
   *   If {@code balance} is null.
   */
  public final void set(T balance) {
    throw new UnsupportedOperationException("not implemented");
  }

  /**
   * Checks if the {@link #balance} has been set.
   *
   * @return True if it has been set, false otherwise.
   */
  public final boolean isSet() {
    throw new UnsupportedOperationException("not implemented");
  }

  /**
   * Gets the current {@link Balance balance}.
   *
   * @return Current {@link Balance balance}, if set.
   */
  @Nullable
  public final T get() {
    throw new UnsupportedOperationException("not implemented");
  }

  /**
   * Adds the given {@link Consumer consumer} to the {@link #consumers list}.
   *
   * @param consumer
   *   {@link Consumer} that will be added.
   *
   * @throws NullPointerException
   *   If {@code consumer} is null.
   */
  public final void addConsumer(Consumer<T> consumer) {
    throw new UnsupportedOperationException("not implemented");
  }

  /**
   * Removes the given {@link Consumer consumer} from the {@link #consumers list}.
   *
   * @param consumer
   *   {@link Consumer} that will be removed.
   *
   * @throws NullPointerException
   *   If {@code consumer} is null.
   */
  public final void removeConsumer(Consumer<T> consumer) {
    throw new UnsupportedOperationException("not implemented");
  }
}
