package com.tpago.movil.ui.onboarding.registration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpago.movil.R;
import com.tpago.movil.ui.widget.TextInput;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public final class PhoneNumberValidationFragment extends Fragment {
  private Unbinder unbinder;

  @BindView(R.id.num_pad_text_input)
  TextInput numPadTextInput;

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

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all annotated views, resources and methods.
    unbinder = ButterKnife.bind(this, view);
  }

  @Override
  public void onResume() {
    super.onResume();
    // Sets focus on the num pad text input.
    numPadTextInput.requestFocus();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Binds all annotated views, resources and methods.
    unbinder.unbind();
  }
}
