package com.tpago.movil.onboarding.registration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpago.movil.R;
import com.tpago.movil.app.Fragments;
import com.tpago.movil.onboarding.OnboardingFragment;

/**
 * @author hecvasro
 */
public final class RegistrationFragment extends OnboardingFragment implements RegistrationScreen {
  private RegistrationComponent component;

  public static RegistrationFragment create() {
    return new RegistrationFragment();
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_registration, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Initializes the dependency injector.
    component = getOnboardingComponent().plus(new RegistrationModule());
    // Sets the initial registration screen.
    setScreen(PhoneNumberFormFragment.create(), Fragments.Transition.NONE, false);
  }

  @Override
  public RegistrationComponent getRegistrationComponent() {
    return component;
  }

  @Override
  public void setScreen(Fragment fragment, Fragments.Transition transition, boolean addToBackStack) {
    Fragments.replace(getChildFragmentManager(), fragment, transition, addToBackStack);
  }
}
