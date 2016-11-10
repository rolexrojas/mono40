package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import com.gbh.movil.Utils;

/**
 * Notification representation.
 *
 * @author hecvasro
 */
public abstract class Notification {
  /**
   * Notification's {@link NotificationType type}.
   */
  private final NotificationType type;

  /**
   * Constructs a new notification.
   *
   * @param type
   *   Notification's {@link NotificationType type}.
   */
  protected Notification(@NonNull NotificationType type) {
    this.type = type;
  }

  /**
   * Gets the {@link NotificationType type} of the notification.
   *
   * @return Notification's {@link NotificationType type}.
   */
  @NonNull
  public final NotificationType getType() {
    return type;
  }

  @Override
  public boolean equals(Object object) {
    return super.equals(object) || (Utils.isNotNull(object) &&
      object instanceof Notification && ((Notification) object).getType().equals(getType()));
  }

  @Override
  public int hashCode() {
    return getType().hashCode();
  }
}
