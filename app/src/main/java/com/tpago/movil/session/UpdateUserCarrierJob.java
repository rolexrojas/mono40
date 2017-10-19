package com.tpago.movil.session;

import com.tpago.movil.payment.Carrier;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Placeholder;
import com.tpago.movil.util.Result;

/**
 * @author hecvasro
 */
public final class UpdateUserCarrierJob extends SessionJob {

  public static final String TYPE = "UpdateUserCarrierJob";

  static UpdateUserCarrierJob create(Carrier carrier) {
    return new UpdateUserCarrierJob(carrier);
  }

  final Carrier carrier;

  private UpdateUserCarrierJob(Carrier carrier) {
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
    final Result<Placeholder> result = this.api.updateUserCarrier(user, this.carrier);
    if (!result.isSuccessful()) {
      // TODO: Handle request failure.
    }
  }
}
