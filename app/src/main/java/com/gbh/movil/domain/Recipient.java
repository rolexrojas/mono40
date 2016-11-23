package com.gbh.movil.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.Utils;
import com.gbh.movil.domain.util.StringUtils;

import java.io.Serializable;

/**
 * Abstract recipient representation.
 *
 * @author hecvasro
 */
public abstract class Recipient implements Serializable, Matchable {
  /**
   * Recipient's {@link RecipientType type}.
   */
  private final RecipientType type;

  /**
   * Recipient's label.
   */
  private String label;

  /**
   * Constructs a new recipient.
   *
   * @param type
   *   Recipient's {@link RecipientType type}.
   * @param label
   *   Recipient's label.
   */
  protected Recipient(@NonNull RecipientType type, @Nullable String label) {
    this.type = type;
    this.label = label;
  }

  /**
   * Constructs a new recipient.
   *
   * @param type
   *   Recipient's {@link RecipientType type}.
   */
  protected Recipient(@NonNull RecipientType type) {
    this(type, null);
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

  /**
   * Gets the label of the recipient.
   *
   * @return Recipient's label.
   */
  @Nullable
  public String getLabel() {
    return label;
  }

  /**
   * Gets the identifier of the recipient.
   *
   * @return Recipient's identifier.
   */
  @NonNull
  public abstract String getIdentifier();

  /**
   * Sets the label of the recipient.
   *
   * @param label
   *   Recipient's label.
   */
  public void setLabel(@Nullable String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return "Recipient:{type='" + type + "',label='" + label + "'}";
  }

  @Override
  public boolean matches(@Nullable String query) {
    return Utils.isNotNull(label) && StringUtils.matches(label, query);
  }
}
