package com.gbh.movil.domain;

/**
 * {@link Notification} that represents the removal an {@link Account account}.
 *
 * @author hecvasro
 */
public class AccountRemovalNotification extends Notification {
  public AccountRemovalNotification() {
    super(NotificationType.ACCOUNT_REMOVAL);
  }
}
