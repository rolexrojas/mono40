package com.tpago.movil.ui.onboarding.registration;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
public abstract class ChildRegistrationFragment extends Fragment {
  private RegistrationScreen parentScreen;

  protected final RegistrationComponent getRegistrationComponent() {
    return parentScreen.getRegistrationComponent();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    // Attaches the parent screen.
    final Fragment parentFragment = Preconditions
      .checkNotNull(getParentFragment(), "getParentFragment() == null");
    if (!(parentFragment instanceof RegistrationScreen)) {
      throw new ClassCastException("!(parentFragment instanceof OnboardingScreen)");
    }
    parentScreen = (RegistrationScreen) parentFragment;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    // Detaches the parent screen.
    parentScreen = null;
  }
}
