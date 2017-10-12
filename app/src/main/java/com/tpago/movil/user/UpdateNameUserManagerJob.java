package com.tpago.movil.user;

import com.tpago.movil.util.ObjectHelper;

import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class UpdateNameUserManagerJob extends UserManagerJob {

  static UpdateNameUserManagerJob create(String firstName, String lastName) {
    return new UpdateNameUserManagerJob(firstName, lastName);
  }

  private final String firstName;
  private final String lastName;

  private UpdateNameUserManagerJob(String firstName, String lastName) {
    super("UpdateNameUserManagerJob");

    this.firstName = ObjectHelper.checkNotNull(firstName, "firstName");
    this.lastName = ObjectHelper.checkNotNull(lastName, "lastName");
  }

  @Override
  public void onRun() throws Throwable {
    Timber.d("UpdateNameUserManagerJob started");

    // TODO

    Timber.d("UpdateNameUserManagerJob finished");
  }
}
