package com.gbh.movil.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Result representation.
 *
 * @author hecvasro
 */
public abstract class Result<C, D> {
  /**
   * Result's code.
   */
  private final C code;
  /**
   * Result's data.
   */
  private final D data;

  /**
   * Constructs a new result.
   *
   * @param code
   *   Result's code.
   * @param data
   *   Result's data.
   */
  protected Result(@NonNull C code, @Nullable D data) {
    this.code = code;
    this.data = data;
  }

  /**
   * Gets the code of the result.
   *
   * @return Result's code.
   */
  @NonNull
  public final C getCode() {
    return code;
  }

  /**
   * Gets the data of the result.
   *
   * @return Result's data.
   */
  @Nullable
  public final D getData() {
    return data;
  }

  /**
   * Indicates if is successful result or not.
   *
   * @return True if it is successful result, false otherwise.
   */
  public abstract boolean isSuccessful();

  @Override
  public String toString() {
    return String.format("(%1$s) %2$s", code, data);
  }
}
