package com.gbh.movil.domain;

import android.support.annotation.NonNull;

/**
 * Abstract recipient representation.
 *
 * @author hecvasro
 */
public abstract class Recipient implements Matchable {
  /**
   * Recipient's {@link RecipientType type}.
   */
  protected final RecipientType type;

  /**
   * Constructs a new recipient.
   *
   * @param type
   *   Recipient's {@link RecipientType type}.
   */
  protected Recipient(@NonNull RecipientType type) {
    this.type = type;
  }

  /**
   * Gets the {@link RecipientType type} of the recipient.
   *
   * @return Recipient's {@link RecipientType type}.
   */
  @NonNull
  public final RecipientType getType() {
    return type;
  }

  @Override
  public String toString() {
    return "Recipient:{type=" + type + "}";
  }
}
