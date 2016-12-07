package com.gbh.movil.domain;

import android.support.annotation.NonNull;

/**
 * Session representation.
 *
 * @author hecvasro
 */
public final class Session {
  /**
   * User's {@link PhoneNumber phone number}.
   */
  private final PhoneNumber phoneNumber;
  /**
   * User's name.
   */
  private final String name;
  /**
   * Session's authentication token.
   */
  private final String authToken;

  /**
   * Constructs a new session.
   *
   * @param phoneNumber
   *   User's {@link PhoneNumber phone number}.
   * @param name
   *   User's name.
   * @param authToken
   *   Session's authentication token.
   */
  public Session(@NonNull PhoneNumber phoneNumber, @NonNull String name,
    @NonNull String authToken) {
    this.phoneNumber = phoneNumber;
    this.name = name;
    this.authToken = authToken;
  }

  /**
   * Gets the {@link PhoneNumber phone number} of the user.
   *
   * @return User's {@link PhoneNumber phone number}.
   */
  @NonNull
  public final PhoneNumber getPhoneNumber() {
    return phoneNumber;
  }

  /**
   * Gets the name of the user.
   *
   * @return User's name.
   */
  @NonNull
  public final String getName() {
    return name;
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
