package com.tpago.movil.nfc;

import android.content.Context;
import android.nfc.NfcAdapter;

import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
public final class NfcHelper {
  private final NfcAdapter adapter;

  NfcHelper(Context context) {
    adapter = NfcAdapter.getDefaultAdapter(Preconditions.checkNotNull(context, "context == null"));
  }

  public final boolean isNfcAvailable() {
    return false;
//    return Objects.isNotNull(adapter);
  }

  public final boolean isNfcEnabled() {
    return isNfcAvailable() && adapter.isEnabled();
  }
}
