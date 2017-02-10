package com.tpago.movil.d.ui.view.widget.pad;

import android.support.annotation.NonNull;

import com.tpago.movil.d.misc.Utils;

/**
 * Num pad's cell representation.
 *
 * @author hecvasro
 */
public class NumPadCell<T> {
  /**
   * Cell's value.
   */
  private final T value;

  /**
   * Constructs a new cell.
   *
   * @param value
   *   Cell's value.
   */
  protected NumPadCell(@NonNull T value) {
    this.value = value;
  }

  /**
   * Gets the value of the cell.
   *
   * @return Cell's value.
   */
  @NonNull
  public final T getValue() {
    return value;
  }

  @Override
  public boolean equals(Object object) {
    return super.equals(object) || (Utils.isNotNull(object) && object instanceof NumPadCell<?>
      && ((NumPadCell<?>) object).value.equals(value));
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
