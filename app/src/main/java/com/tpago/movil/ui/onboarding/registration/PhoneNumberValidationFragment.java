package com.tpago.movil.ui.onboarding.registration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpago.movil.R;

/**
 * @author hecvasro
 */
public final class PhoneNumberValidationFragment extends Fragment {
  public static PhoneNumberValidationFragment create() {
    return new PhoneNumberValidationFragment();
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_phone_number_validation, container, false);
  }
}
