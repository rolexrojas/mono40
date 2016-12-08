package com.gbh.movil.domain.session;

import android.support.annotation.NonNull;

/**
 * Session representation.
 *
 * @author hecvasro
 */
public final class Session {
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
   * @param name
   *   User's name.
   * @param authToken
   *   Session's authentication token.
   */
  public Session(@NonNull String name, @NonNull String authToken) {
    this.name = name;
    this.authToken = authToken;
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
