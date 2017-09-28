package com.tpago.movil.dep;

import android.support.v7.app.AlertDialog;

import com.tpago.movil.dep.Preconditions;

/**
 * @author hecvasro
 */
@Deprecated
public final class Dialog {
  private final AlertDialog internalDialog;

  private Dialog(AlertDialog internalDialog) {
    this.internalDialog = Preconditions.assertNotNull(internalDialog, "internalDialog == null");
  }
}
