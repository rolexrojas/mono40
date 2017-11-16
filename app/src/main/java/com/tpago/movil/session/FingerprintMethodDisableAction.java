package com.tpago.movil.session;

final class FingerprintMethodDisableAction implements UnlockMethodDisableAction {

  static FingerprintMethodDisableAction create() {
    return new FingerprintMethodDisableAction();
  }

  private FingerprintMethodDisableAction() {
  }

  @Override
  public void run() throws Exception {
  }
}
