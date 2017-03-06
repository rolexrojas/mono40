package com.tpago.movil.main.purchase;

import com.tpago.movil.nfc.NfcHelper;
import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
public final class PurchaseFragmentCreator {
  private final NfcHelper nfcHelper;

  public PurchaseFragmentCreator(NfcHelper nfcHelper) {
    this.nfcHelper = Preconditions.checkNotNull(nfcHelper, "nfcHelper == null");
  }

  public final PurchaseFragment create() {
    throw new UnsupportedOperationException("Not implemented");
  }
}
