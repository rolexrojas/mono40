package com.gbh.movil.ui.main.pin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gbh.movil.R;

/**
 * TODO
 *
 * @author hecvasro
 */
public class PinConfirmationFragment extends Fragment {
  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_pin_confirmation, container, false);
  }

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
