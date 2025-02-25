package com.tpago.movil.session;

import com.tpago.movil.Name;
import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
public final class UpdateUserNameJob extends SessionJob {

  public static final String TYPE = "UpdateUserNameJob";

  static UpdateUserNameJob create(Name name) {
    return new UpdateUserNameJob(name);
  }

  final Name name;

  private UpdateUserNameJob(Name name) {
    super(TYPE);
    this.name = ObjectHelper.checkNotNull(name, "name");
  }

  @Override
  public void onAdded() {
    this.sessionManager.getUser()
      .updateName(this.name);
  }

  @Override
  public void onRun() throws Throwable {
    final User user = this.sessionManager.getUser();
    this.api.updateUserName(user, this.name)
      .blockingAwait();
  }
}
