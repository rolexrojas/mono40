package com.tpago.movil.init.register;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
public abstract class BaseRegisterFragment extends Fragment {
  private RegisterComponent component;

  protected final RegisterComponent getRegisterComponent() {
    return component;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    // Attaches the parent screen.
    final Fragment f = Preconditions.checkNotNull(getParentFragment(), "getParentFragment() == null");
    if (!(f instanceof RegisterContainer)) {
      throw new ClassCastException("!(getParentFragment() instanceof OnboardingScreen)");
    }
    component = ((RegisterContainer) f).getRegisterComponent();
  }
}
