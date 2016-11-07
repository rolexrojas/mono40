package com.gbh.movil.ui.main;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * TODO
 *
 * @author hecvasro
 */
public abstract class SubFragment extends Fragment {
  protected ParentScreen parentScreen;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    // Attaches the parent screen to the fragment.
    final Activity activity = getActivity();
    if (activity instanceof ParentScreen) {
      parentScreen = (ParentScreen) activity;
    } else {
      throw new ClassCastException("Activity must implement the 'ParentScreen' interface");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    // Detaches the parent screen from the fragment.
    parentScreen = null;
  }
}
