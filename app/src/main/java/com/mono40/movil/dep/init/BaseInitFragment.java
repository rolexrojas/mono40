package com.mono40.movil.dep.init;

import android.app.Activity;
import android.content.Context;
import androidx.fragment.app.Fragment;

/**
 * @author hecvasro
 */
public abstract class BaseInitFragment extends Fragment {
  private InitComponent initComponent;

  protected final InitComponent getInitComponent() {
    return initComponent;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    final Activity a = getActivity();
    if (!(a instanceof InitContainer)) {
      throw new ClassCastException("!(getActivity() instanceof InitContainer)");
    }
    initComponent = ((InitContainer) a).getInitComponent();
  }
}
