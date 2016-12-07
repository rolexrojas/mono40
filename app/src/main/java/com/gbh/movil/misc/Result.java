package com.gbh.movil.misc;

import android.support.annotation.NonNull;

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
  protected Result(@NonNull C code, D data) {
    this.code = code;
    this.data = data;
  }

  /**
   * TODO
   *
   * @param code
   *   TODO
   */
  protected Result(@NonNull C code) {
    this(code, null);
  }

  /**
   * TODO
   *
   * @return TODO
   */
  public abstract boolean isSuccessful();

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
  public final D getData() {
    return data;
  }

  @Override
  public String toString() {
    return String.format("(%1$s) %2$s", code, data);
  }
}
