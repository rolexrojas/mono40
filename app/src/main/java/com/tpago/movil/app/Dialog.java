package com.tpago.movil.app;

import android.support.v7.app.AlertDialog;

import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
public final class Dialog {
  private final AlertDialog internalDialog;

  private Dialog(AlertDialog internalDialog) {
    this.internalDialog = Preconditions.checkNotNull(internalDialog, "internalDialog == null");
  }
}
