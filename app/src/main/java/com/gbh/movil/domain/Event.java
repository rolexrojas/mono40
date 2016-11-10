package com.gbh.movil.domain;

import android.support.annotation.NonNull;

/**
 * Notification representation.
 *
 * @author hecvasro
 */
public abstract class Event {
  /**
   * Event's {@link EventType type}.
   */
  private final EventType type;

  /**
   * Indicates if it is a sticky event or not.
   */
  private final boolean sticky;

  /**
   * Constructs a new notification.
   *
   * @param type
   *   Event's {@link EventType type}.
   * @param sticky
   *   Indicates if it is a sticky event or not.
   */
  protected Event(@NonNull EventType type, boolean sticky) {
    this.type = type;
    this.sticky = sticky;
  }

  /**
   * Gets the {@link EventType type} of the event.
   *
   * @return Event's {@link EventType type}.
   */
  @NonNull
  public final EventType getType() {
    return type;
  }

  /**
   * Indicates if it is a sticky event or not.
   *
   * @return True if it is a sticky event, false otherwise.
   */
  @NonNull
  public final boolean isSticky() {
    return sticky;
  }

  @Override
  public String toString() {
    return Event.class.getSimpleName() + ":{type='" + type + "',sticky='" + sticky + "'}";
  }
}
