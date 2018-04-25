package com.tpago.movil.purchase;

import android.support.annotation.Nullable;

/**
 * Factory of {@link PosService}
 */
public interface PosServiceFactory {

  /**
   * Creates a new {@link PosService} instance.
   *
   * @return A new {@link PosService} instance if there is an {@link android.nfc.NfcAdapter}
   * available, {@code null} otherwise.
   */
  @Nullable
  PosService create();
}
