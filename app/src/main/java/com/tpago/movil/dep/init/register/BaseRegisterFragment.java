package com.tpago.movil.dep.init.register;

import android.support.v4.app.Fragment;

import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
public abstract class BaseRegisterFragment extends Fragment {

  private RegisterComponent component;

  protected final RegisterComponent getRegisterComponent() {
    if (ObjectHelper.isNull(component)) {
      final Fragment f = ObjectHelper.checkNotNull(
        this.getParentFragment(),
        "this.getParentFragment()"
      );
      if (!(f instanceof RegisterContainer)) {
        throw new ClassCastException("!(this.getParentFragment() instanceof RegisterContainer)");
      }
      component = ((RegisterContainer) f).getRegisterComponent();
    }
    return component;
  }
}
