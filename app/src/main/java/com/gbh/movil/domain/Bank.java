package com.gbh.movil.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Bank representation.
 *
 * @author hecvasro
 */
public class Bank {
  /**
   * Bank's identifier.
   */
  private final String id;
  /**
   * Bank's name.
   */
  private final String name;
  /**
   * Bank's state. True if it is active, false otherwise.
   */
  private boolean state;
  /**
   * Bank's uri.
   */
  private String logoUri;

  /**
   * Constructs a new bank.
   *
   * @param id
   *   Bank's identifier.
   * @param name
   *   Bank's name.
   */
  public Bank(@NonNull String id, @NonNull String name) {
    this.id = id;
    this.name = name;
  }

  /**
   * Gets the identifier of the bank.
   *
   * @return Bank's identifier.
   */
  @NonNull
  public final String getId() {
    return id;
  }

  /**
   * Gets the name of the bank.
   *
   * @return Bank's name.
   */
  @NonNull
  public final String getName() {
    return name;
  }

  /**
   * Gets the state of the bank.
   *
   * @return True if it is active, false otherwise.
   */
  public boolean getState() {
    return state;
  }

  /**
   * Sets the state of the bank.
   *
   * @param state
   *   True if it is active, false otherwise.
   */
  public void setState(boolean state) {
    this.state = state;
  }

  /**
   * Gets the uri of the logo of the bank.
   *
   * @return Bank's logo uri.
   */
  @Nullable
  public String getLogoUri() {
    return logoUri;
  }

  /**
   * Sets the uri of the logo of the bank.
   *
   * @param logoUri
   *   Bank's logo uri.
   */
  public void setLogoUri(@Nullable String logoUri) {
    this.logoUri = logoUri;
  }

  @Override
  public boolean equals(Object object) {
    return super.equals(object) || (object != null && object instanceof Bank &&
      ((Bank) object).id.equals(id));
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    return "Bank{id='" + id + "',name='" + name + "',state=" + state + ",logoUri='" + logoUri
      + "'}";
  }
}
