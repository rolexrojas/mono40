package com.tpago.movil.ui.onboarding;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * @author hecvasro
 */
public abstract class OnboardingFragment extends Fragment {
  private OnboardingScreen parentScreen;

  protected final OnboardingComponent getParentComponent() {
    return parentScreen.getComponent();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    // Attaches the parent screen.
    final Activity parentActivity = getActivity();
    if (!(parentActivity instanceof OnboardingScreen)) {
      throw new ClassCastException("!(getActivity() instanceof OnboardingScreen)");
    }
    parentScreen = (OnboardingScreen) parentActivity;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    // Detaches the parent screen.
    parentScreen = null;
  }
}
