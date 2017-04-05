package com.tpago.movil.main.purchase;

import android.support.v4.app.Fragment;

import com.tpago.movil.nfc.NfcHelper;
import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
public final class PurchaseFragmentCreator {
  private final NfcHelper nfcHelper;

  public PurchaseFragmentCreator(NfcHelper nfcHelper) {
    this.nfcHelper = Preconditions.assertNotNull(nfcHelper, "nfcHelper == null");
  }

  public final Fragment create() {
    if (nfcHelper.isNfcAvailable()) {
      return com.tpago.movil.d.ui.main.purchase.PurchaseFragment.newInstance();
    } else {
      return NonNfcPurchaseFragment.create();
    }
  }
}
