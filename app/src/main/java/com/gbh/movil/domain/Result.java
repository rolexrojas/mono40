package com.gbh.movil.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Result representation.
 *
 * @author hecvasro
 */
public final class Result<C, D> {
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
  private Result(@NonNull C code, @Nullable D data) {
    this.code = code;
    this.data = data;
  }

  /**
   * Creates a {@link Result result} with the given code and data.
   */
  @NonNull
  public static <C, D> Result<C, D> create(@NonNull C code, @Nullable D data) {
    return new Result<>(code, data);
  }

  /**
   * Creates a {@link Result result} with the given code.
   */
  @NonNull
  public static <C, D> Result<C, D> create(@NonNull C code) {
    return create(code, null);
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

  @Override
  public String toString() {
    return "Result:{code='" + code + "',data='" + data + "'}";
  }
}
