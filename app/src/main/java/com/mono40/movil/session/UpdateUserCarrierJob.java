package com.mono40.movil.session;

import com.mono40.movil.company.partner.Partner;
import com.mono40.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
public final class UpdateUserCarrierJob extends SessionJob {

  public static final String TYPE = "UpdateUserCarrierJob";

  static UpdateUserCarrierJob create(Partner carrier) {
    return new UpdateUserCarrierJob(carrier);
  }

  final Partner carrier;

  private UpdateUserCarrierJob(Partner carrier) {
    super(TYPE);
    this.carrier = ObjectHelper.checkNotNull(carrier, "carrier");
  }

  @Override
  public void onAdded() {
    this.sessionManager.getUser()
      .updateCarrier(this.carrier);
  }

  @Override
  public void onRun() throws Throwable {
    final User user = this.sessionManager.getUser();
    this.api.updateUserCarrier(user, this.carrier)
      .blockingAwait();
  }
}
