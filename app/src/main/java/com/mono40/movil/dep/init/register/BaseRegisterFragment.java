package com.mono40.movil.dep.init.register;

import androidx.fragment.app.Fragment;

import com.mono40.movil.dep.widget.Keyboard;
import com.mono40.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
public abstract class BaseRegisterFragment extends Fragment {

  private RegisterComponent component;

  protected final RegisterComponent getRegisterComponent() {
    if (ObjectHelper.isNull(this.component)) {
      final Fragment f = ObjectHelper.checkNotNull(
        this.getParentFragment(),
        "this.getParentFragment()"
      );
      if (!(f instanceof RegisterContainer)) {
        throw new ClassCastException("!(this.getParentFragment() instanceof RegisterContainer)");
      }
      this.component = ((RegisterContainer) f).getRegisterComponent();
    }
    return this.component;
  }

  @Override
  public void onResume() {
    super.onResume();
    Keyboard.hide(this.getView());
  }
}
