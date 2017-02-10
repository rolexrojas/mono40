package com.tpago.movil.d.domain.api;

/**
 * @author hecvasro
 */
public final class ApiError {
  private final Code code;
  private final String description;

  public ApiError(Code code, String description) {
    this.code = code;
    this.description = description;
  }

  public final Code getCode() {
    return code;
  }

  public final String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    return String.format("{code:%1$s,description:'%2$s'}", code, description);
  }

  public enum Code {
    ALREADY_ASSOCIATED_DEVICE(112),
    ALREADY_ASSOCIATED_PROFILE(12),
    ALREADY_REGISTERED_EMAIL(55),
    ALREADY_REGISTERED_USERNAME(21),
    INCORRECT_USERNAME_AND_PASSWORD(4),
    INVALID_DEVICE_ID(9),
    INVALID_EMAIL(5),
    INVALID_PHONE_NUMBER(13),
    INVALID_PIN(16),
    UNASSOCIATED_PROFILE(144),
    UNAVAILABLE_SERVICE(14),
    UNEXPECTED(500);

    private final int value;

    Code(int value) {
      this.value = value;
    }

    public static Code fromValue(int value) {
      for (Code code : Code.values()) {
        if (value == code.value) {
          return code;
        }
      }
      return UNEXPECTED;
    }

    public final int getValue() {
      return value;
    }
  }
}
