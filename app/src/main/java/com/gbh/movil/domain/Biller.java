package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import com.gbh.movil.Utils;

/**
 * Biller representation.
 *
 * @author hecvasro
 */
public class Biller {
  /**
   * Biller's identifier.
   */
  private final String id;
  /**
   * Biller's name.
   */
  private final String name;

  /**
   * Constructs a new biller.
   *
   * @param id
   *   Biller's identifier.
   * @param name
   *   Biller's name.
   */
  public Biller(@NonNull String id, @NonNull String name) {
    this.id = id;
    this.name = name;
  }

  /**
   * Gets the identifier of the biller.
   *
   * @return Biller's identifier.
   */
  @NonNull
  public final String getId() {
    return id;
  }

  /**
   * Gets the name of the biller.
   *
   * @return Biller's name.
   */
  @NonNull
  public final String getName() {
    return name;
  }

  @Override
  public boolean equals(Object object) {
    return super.equals(object) || (Utils.isNotNull(object) && object instanceof Biller
      && ((Biller) object).id.equals(id));
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    return Biller.class.getSimpleName() + ":{id='" + id + ",name='" + name + "'}";
  }
}
