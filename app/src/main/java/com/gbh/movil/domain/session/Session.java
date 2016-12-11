package com.gbh.movil.domain.session;

import android.support.annotation.NonNull;

/**
 * Session representation.
 *
 * @author hecvasro
 */
public final class Session {
  /**
   * User's phone number.
   */
  private final String phoneNumber;
  /**
   * User's email address.
   */
  private final String email;
  /**
   * Session's authentication token.
   */
  private final String authToken;

  /**
   * Constructs a new session.
   *
   * @param phoneNumber
   *   User's phone number.
   * @param email
   *   User's email address.
   * @param authToken
   *   Session's authentication token.
   */
  public Session(@NonNull String phoneNumber, @NonNull String email, @NonNull String authToken) {
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.authToken = authToken;
  }

  /**
   * Gets the phone number of the user.
   *
   * @return User's phone number.
   */
  @NonNull
  public final String getPhoneNumber() {
    return phoneNumber;
  }

  /**
   * Gets the email address of the user.
   *
   * @return User's email address.
   */
  @NonNull
  public final String getEmail() {
    return email;
  }

  /**
   * Gets the authentication token of the session.
   *
   * @return Session's authentication token.
   */
  public String getAuthToken() {
    return authToken;
  }
}
