package com.tpago.movil.ui.onboarding.registration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpago.movil.R;
import com.tpago.movil.ui.Fragments;
import com.tpago.movil.ui.onboarding.OnboardingFragment;

import timber.log.Timber;

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
    // Sets the initial fragment.
    Fragments.replace(getChildFragmentManager(), PhoneNumberValidationFragment.create());
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    Timber.d("onDestroyView");
  }

  @Override
  public RegistrationComponent getRegistrationComponent() {
    return component;
  }
}
