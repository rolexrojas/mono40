package com.tpago.movil.domain;

import android.support.annotation.NonNull;

/**
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
   * Flag that indicates whether the bank is active or not.
   */
  private boolean active;

  /**
   * Creates a bank with the given identifier and name.
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
   * @return
   *  Bank's identifier.
   */
  @NonNull
  public final String getId() {
    return id;
  }

  /**
   * Gets the name of the bank.
   *
   * @return
   *  Bank's name.
   */
  @NonNull
  public final String getName() {
    return name;
  }

  /**
   * Indicates whether the bank is active or not.
   *
   * @return
   *  True if it is active, false otherwise.
   */
  public boolean isActive() {
    return active;
  }

  /**
   * Sets the flag that indicates whether the bank is active or not.
   */
  public void setActive(boolean active) {
    this.active = active;
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
    return "Bank{id='" + id + "',name='" + name + "',active=" + active + "}";
  }
}
