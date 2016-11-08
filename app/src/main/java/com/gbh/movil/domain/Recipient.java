package com.gbh.movil.domain;

import android.support.annotation.NonNull;

/**
 * Abstract recipient representation.
 *
 * @author hecvasro
 */
public abstract class Recipient {
  /**
   * Recipient's type.
   */
  @RecipientType
  protected final int type;

  /**
   * Constructs a new recipient.
   *
   * @param type
   *   Recipient's type.
   */
  protected Recipient(@RecipientType int type) {
    this.type = type;
  }

  /**
   * Gets the type of the recipient.
   *
   * @return Recipient's type.
   */
  @RecipientType
  public final int getType() {
    return type;
  }

  @Override
  public String toString() {
    return "Recipient:{type=" + type + "}";
  }

  /**
   * Checks if any matcheable property matches the given {@code query}.
   *
   * @return True if any matcheable property matches the given {@code query}, false otherwise.
   */
  abstract boolean matches(@NonNull String query);
}
