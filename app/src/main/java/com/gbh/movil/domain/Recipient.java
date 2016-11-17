package com.gbh.movil.domain;

import com.gbh.movil.domain.recipient.RecipientType;

/**
 * Abstract recipient representation.
 *
 * @author hecvasro
 */
public abstract class Recipient implements Matchable {
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
}
