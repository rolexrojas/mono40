package com.tpago.movil.ui;

import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class ConfirmPinDialogFragment extends DialogFragment {
  /**
   * TODO
   *
   * @param correctly TODO
   */
  public final void dismiss(boolean correctly) {
    // TODO
  }

  /**
   * TODO
   */
  public interface Listener {
    /**
     * TODO
     *
     * @param pin
     *   TODO
     */
    void onConfirm(@NonNull String pin);
  }
}
