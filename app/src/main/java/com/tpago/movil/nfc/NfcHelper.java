package com.tpago.movil.nfc;

import android.content.Context;
import android.nfc.NfcAdapter;

import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
public final class NfcHelper {
  private final NfcAdapter adapter;

  public NfcHelper(Context context) {
    this.adapter = NfcAdapter
      .getDefaultAdapter(Preconditions.checkNotNull(context, "context == null"));
  }

  public final boolean isNfcAvailable() {
    return Objects.isNotNull(adapter);
  }

  public final boolean isNfcEnabled() {
    return isNfcAvailable() && adapter.isEnabled();
  }
}
