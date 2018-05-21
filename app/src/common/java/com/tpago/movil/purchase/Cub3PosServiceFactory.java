package com.tpago.movil.purchase;

import android.content.Context;
import android.nfc.NfcAdapter;
import android.support.annotation.Nullable;

import com.cube.sdk.altpan.CubeSdkImpl;
import com.tpago.movil.util.ObjectHelper;

/**
 * {@inheritDoc}
 */
final class Cub3PosServiceFactory implements PosServiceFactory {

  static Cub3PosServiceFactory create(Context context) {
    return new Cub3PosServiceFactory(context);
  }

  private final Context context;

  private PosService service = null;
  private boolean isServiceSet = false;

  private Cub3PosServiceFactory(Context context) {
    this.context = ObjectHelper.checkNotNull(context, "context");
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public PosService create() {
    if (!this.isServiceSet) {
      synchronized (this) {
        if (!this.isServiceSet) {
          if (ObjectHelper.isNotNull(NfcAdapter.getDefaultAdapter(this.context))) {
            this.service = Cub3PosService.builder()
              .sdk(() -> new CubeSdkImpl(this.context))
              .build();
          }
          this.isServiceSet = true;
        }
      }
    }
    return this.service;
  }
}
