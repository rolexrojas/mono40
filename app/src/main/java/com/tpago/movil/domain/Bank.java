package com.tpago.movil.domain;

import android.support.annotation.NonNull;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class Bank {
  /**
   * TODO
   */
  private final String id;

  /**
   * TODO
   */
  private final String name;

  /**
   * TODO
   */
  private boolean active;

  /**
   * TODO
   *
   * @param id TODO
   * @param name TODO
   */
  public Bank(@NonNull String id, @NonNull String name) {
    this.id = id;
    this.name = name;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final String getId() {
    return id;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final String getName() {
    return name;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  public boolean isActive() {
    return active;
  }

  /**
   * TODO
   *
   * @param active TODO
   */
  public void setActive(boolean active) {
    this.active = active;
  }

  @Override
  public boolean equals(Object object) {
    // TODO
    return super.equals(object);
  }

  @Override
  public int hashCode() {
    // TODO
    return super.hashCode();
  }

  @Override
  public String toString() {
    // TODO
    return super.toString();
  }
}
