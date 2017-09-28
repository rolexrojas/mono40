package com.tpago.movil.d.misc;

/**
 * Result representation.
 *
 * @author hecvasro
 */
@Deprecated
public abstract class Result<C, D> {
  private final boolean successful;
  private final C code;
  private final D data;

  /**
   * Constructs a new result.
   */
  public Result(boolean successful, C code, D data) {
    this.successful = successful;
    this.code = code;
    this.data = data;
  }

  /**
   * Checks whether it's successful or not.
   *
   * @return True if it's successful, false otherwise.
   */
  public final boolean isSuccessful() {
    return successful;
  }

  /**
   * Gets the code of the result.
   *
   * @return Result code.
   */
  public final C getCode() {
    return code;
  }

  /**
   * Gets the data of the result.
   *
   * @return Result data.
   */
  public final D getData() {
    return data;
  }

  @Override
  public String toString() {
    return String.format("%1$s - (%2$s) %3$s", successful, code, data);
  }
}
