package com.gbh.movil.domain;

/**
 * {@link Notification} that represents the addition an {@link Account account}.
 *
 * @author hecvasro
 */
public class AccountAdditionNotification extends Notification {
  public AccountAdditionNotification() {
    super(NotificationType.ACCOUNT_ADDITION);
  }
}
