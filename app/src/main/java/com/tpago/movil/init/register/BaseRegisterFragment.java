package com.tpago.movil.init.register;

import android.support.v4.app.Fragment;

import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
public abstract class BaseRegisterFragment extends Fragment {
  private RegisterComponent component;

  protected final RegisterComponent getRegisterComponent() {
    if (Objects.isNull(component)) {
      final Fragment f = Preconditions
        .checkNotNull(getParentFragment(), "getParentFragment() == null");
      if (!(f instanceof RegisterContainer)) {
        throw new ClassCastException("!(getParentFragment() instanceof OnboardingScreen)");
      }
      component = ((RegisterContainer) f).getRegisterComponent();
    }
    return component;
  }
}
