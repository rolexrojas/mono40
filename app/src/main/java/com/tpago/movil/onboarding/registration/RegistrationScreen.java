package com.tpago.movil.onboarding.registration;

import android.support.v4.app.Fragment;

import com.tpago.movil.app.Fragments;

/**
 * @author hecvasro
 */
interface RegistrationScreen {
  RegistrationComponent getRegistrationComponent();

  void setScreen(Fragment fragment, Fragments.Transition transition, boolean addToBackStack);
}
