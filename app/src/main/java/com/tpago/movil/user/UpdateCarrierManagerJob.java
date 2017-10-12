package com.tpago.movil.user;

import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class UpdateCarrierManagerJob extends UserManagerJob {

  static UpdateCarrierManagerJob create(int carrierCode) {
    return new UpdateCarrierManagerJob(carrierCode);
  }

  private final int carrierCode;

  private UpdateCarrierManagerJob(int carrierCode) {
    super("UpdateCarrierManagerJob");

    this.carrierCode = carrierCode;
  }

  @Override
  public void onRun() throws Throwable {
    Timber.d("UpdateCarrierUserManagerJob started");

    // TODO

    Timber.d("UpdateCarrierUserManagerJob finished");
  }
}
