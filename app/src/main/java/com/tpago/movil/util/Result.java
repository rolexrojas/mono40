package com.tpago.movil.util;

/**
 * Result representation
 *
 * @author hecvasro
 */
public class Result<T> {

  /**
   * Creates a new successful result.
   *
   * @throws NullPointerException
   *   If {@code successData} is {@code null}.
   */
  public static <T> Result<T> create(T successData) {
    return new Result<>(ObjectHelper.checkNotNull(successData, "successData"), null);
  }

  /**
   * Creates a new failure result.
   *
   * @throws NullPointerException
   *   If {@code failureData} is {@code null}.
   */
  public static <T, C> Result<T> create(FailureData failureData) {
    return new Result<>(null, ObjectHelper.checkNotNull(failureData, "failureData"));
  }

  private final T successData;
  private final FailureData failureData;

  private final boolean successful;

  private String toString;
  private boolean toStringMemoized;
  private int hashCode;
  private boolean hashCodeMemoized;

  private Result(T successData, FailureData failureData) {
    this.successData = successData;
    this.failureData = failureData;

    this.successful = ObjectHelper.isNotNull(this.successData);
  }

  /**
   * @throws IllegalStateException
   *   If it isn't {@link #isSuccessful() successful}.
   */
  public final T successData() {
    if (!this.isSuccessful()) {
      throw new IllegalStateException("!isSuccessful()");
    }
    return this.successData;
  }

  /**
   * @throws IllegalStateException
   *   If it is {@link #isSuccessful() successful}.
   */
  public final FailureData failureData() {
    if (this.isSuccessful()) {
      throw new IllegalStateException("isSuccessful()");
    }
    return this.failureData;
  }

  /**
   * Indicates whether it's successful or not.
   *
   * @return True if it's successful, or otherwise false.
   */
  public final boolean isSuccessful() {
    return this.successful;
  }

  @Override
  public String toString() {
    if (!this.toStringMemoized) {
      synchronized (this) {
        if (!this.toStringMemoized) {
          this.toString = new StringBuilder("Result")
            .append("{")
            .append("successData=")
            .append(this.successData)
            .append(", ")
            .append("failureData=")
            .append(this.failureData)
            .append(", ")
            .append("successful=")
            .append(this.successful)
            .append("}")
            .toString();
          this.toStringMemoized = true;
        }
      }
    }
    return this.toString;
  }

  @Override
  public boolean equals(Object object) {
    if (object == this) {
      return true;
    }
    if (object instanceof Result) {
      final Result that = (Result) object;
      if (that.isSuccessful()) {
        return that.successData.equals(this.successData);
      } else {
        return that.failureData.equals(this.failureData);
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    if (!this.hashCodeMemoized) {
      synchronized (this) {
        if (!this.hashCodeMemoized) {
          this.hashCode
            = this.successful ? this.successData.hashCode() : this.failureData.hashCode();
          this.hashCodeMemoized = true;
        }
      }
    }
    return this.hashCode;
  }
}
