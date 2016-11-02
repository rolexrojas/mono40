package com.gbh.movil.domain;

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
  public Recipient(@RecipientType int type) {
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
}
