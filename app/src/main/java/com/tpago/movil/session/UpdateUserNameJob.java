package com.tpago.movil.session;

import com.tpago.movil.util.StringHelper;

/**
 * @author hecvasro
 */
public final class UpdateUserNameJob extends SessionJob {

  public static final String TYPE = "UpdateUserNameJob";

  static UpdateUserNameJob create(String firstName, String lastName) {
    return new UpdateUserNameJob(firstName, lastName);
  }

  final String firstName;
  final String lastName;

  private UpdateUserNameJob(String firstName, String lastName) {
    super(TYPE);

    this.firstName = StringHelper.checkIsNotNullNorEmpty(firstName, "firstName");
    this.lastName = StringHelper.checkIsNotNullNorEmpty(lastName, "lastName");
  }

  @Override
  public void onAdded() {
    this.sessionManager.getUser()
      .updateName(this.firstName, this.lastName);
  }

  @Override
  public void onRun() throws Throwable {
    final User user = this.sessionManager.getUser();
    this.api.updateUserName(user, this.firstName, this.lastName)
      .blockingAwait();
  }
}
