package com.tpago.movil.domain;

import android.support.annotation.NonNull;

/**
 * Bank representation.
 *
 * @author hecvasro
 */
public final class Bank {
  /**
   * Bank's identifier.
   */
  private final String id;

  /**
   * Bank's name.
   */
  private final String name;

  /**
   * Indicates whether is active or not.
   */
  private boolean state;

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
    return "Bank{id='" + id + "',name='" + name + "',state=" + state + "}";
  }
}
