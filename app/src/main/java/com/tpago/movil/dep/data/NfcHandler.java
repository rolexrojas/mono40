package com.tpago.movil.dep.data;

import android.content.Context;
import android.nfc.NfcAdapter;

/**
 * @author hecvasro
 */
@Deprecated
public final class NfcHandler {
  private final NfcAdapter nfcAdapter;

  public NfcHandler(Context context) {
    nfcAdapter = NfcAdapter.getDefaultAdapter(context);
  }

  public final boolean isAvailable() {
    return nfcAdapter != null;
  }

  public final boolean isEnabled() {
    return isAvailable() && nfcAdapter.isEnabled();
  }
}
