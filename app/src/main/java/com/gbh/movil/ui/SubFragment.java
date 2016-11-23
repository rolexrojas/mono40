package com.gbh.movil.ui;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * TODO
 *
 * @author hecvasro
 */
public abstract class SubFragment<T extends Container<?>> extends Fragment {
  protected T container;

  @Override
  @SuppressWarnings("unchecked")
  public void onAttach(Context context) {
    super.onAttach(context);
    // Attaches the parent screen to the fragment.
    final Activity activity = getActivity();
    if (!(activity instanceof Container)) {
      throw new ClassCastException("Activity must implement the 'Container' interface");
    } else {
      container = (T) activity;
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    // Detaches the parent screen from the fragment.
    container = null;
  }
}
