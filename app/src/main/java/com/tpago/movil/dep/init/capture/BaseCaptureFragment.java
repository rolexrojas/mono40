package com.tpago.movil.dep.init.capture;

import androidx.fragment.app.Fragment;

import com.tpago.movil.dep.widget.Keyboard;
import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
public abstract class BaseCaptureFragment extends Fragment {

  private CaptureComponent component;

  protected final CaptureComponent getCaptureComponent() {
    if (ObjectHelper.isNull(this.component)) {
      final Fragment f = ObjectHelper.checkNotNull(
        this.getParentFragment(),
        "this.getParentFragment()"
      );
      if (!(f instanceof CaptureContainer)) {
        throw new ClassCastException("!(this.getParentFragment() instanceof CaptureContainer)");
      }
      this.component = ((CaptureContainer) f).getCaptureComponent();
    }
    return this.component;
  }

  @Override
  public void onResume() {
    super.onResume();
    Keyboard.hide(this.getView());
  }
}
