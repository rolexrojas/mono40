package com.tpago.movil.dep.init.register;

import android.support.v4.app.Fragment;

import com.tpago.movil.dep.Objects;
import com.tpago.movil.dep.Preconditions;

/**
 * @author hecvasro
 */
public abstract class BaseRegisterFragment extends Fragment {
  private RegisterComponent component;

  protected final RegisterComponent getRegisterComponent() {
    if (Objects.checkIfNull(component)) {
      final Fragment f = Preconditions
        .assertNotNull(getParentFragment(), "getParentFragment() == null");
      if (!(f instanceof RegisterContainer)) {
        throw new ClassCastException("!(getParentFragment() instanceof OnboardingScreen)");
      }
      component = ((RegisterContainer) f).getRegisterComponent();
    }
    return component;
  }
}
