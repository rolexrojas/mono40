package com.gbh.movil.domain.session;

/**
 * @author hecvasro
 */
public enum AuthCode {
  SUCCEEDED,
  /**
   * Indicates that the given phone number is already associated to another profile.
   */
  FAILED_ALREADY_ASSOCIATED_PROFILE,
  /**
   * Indicates that the given phone number is already associated to another device.
   */
  FAILED_ALREADY_ASSOCIATED_DEVICE,
  /**
   * Indicates that the given pin does not match with the pin associated with the given phone
   * number.
   */
  FAILED_INCORRECT_PIN,
  /**
   * Indicates that the given email and password does not match with the given phone number.
   */
  FAILED_INCORRECT_USERNAME_AND_PASSWORD,
  /**
   * Indicates that an unexpected failure occurred.
   */
  FAILED_UNEXPECTED
}
