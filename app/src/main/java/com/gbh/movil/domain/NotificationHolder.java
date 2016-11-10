package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class NotificationHolder {
  /**
   * TODO
   */
  private final List<Notification> notifications = new ArrayList<>();

  public NotificationHolder() {
  }

  /**
   * TODO
   *
   * @param notification
   *   TODO
   */
  void add(@NonNull Notification notification) {
    if (notifications.contains(notification)) {
      notifications.remove(notification);
    }
    notifications.add(0, notification);
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final List<Notification> getAll() {
    return notifications;
  }

  /**
   * TODO
   *
   * @param notification
   *   TODO
   */
  public final void markAsShown(@NonNull Notification notification) {
    if (notifications.contains(notification)) {
      notifications.remove(notification);
    }
  }
}
