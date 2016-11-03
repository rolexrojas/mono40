package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import java.util.Set;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class InitialData {
  private final Set<Account> accounts;
  private final Set<Recipient> recipients;

  public InitialData(@NonNull Set<Account> accounts, @NonNull Set<Recipient> recipients) {
    this.accounts = accounts;
    this.recipients = recipients;
  }

  @NonNull
  public final Set<Account> getAccounts() {
    return accounts;
  }

  @NonNull
  public final Set<Recipient> getRecipients() {
    return recipients;
  }
}
