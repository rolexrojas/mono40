package com.gbh.movil.ui;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * TODO
 *
 * @author hecvasro
 */
public abstract class SubFragment<C> extends Fragment {
  protected ParentScreen<C> parentScreen;

  @Override
  @SuppressWarnings("unchecked")
  public void onAttach(Context context) {
    super.onAttach(context);
    // Attaches the parent screen to the fragment.
    final Activity activity = getActivity();
    if (!(activity instanceof ParentScreen)) {
      throw new ClassCastException("Activity must implement the 'ParentScreen' interface");
    } else {
      parentScreen = (ParentScreen<C>) activity;
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    // Detaches the parent screen from the fragment.
    parentScreen = null;
  }
}
