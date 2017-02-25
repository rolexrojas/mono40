package com.tpago.movil.init.register;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpago.movil.R;
import com.tpago.movil.app.FragmentQualifier;
import com.tpago.movil.app.FragmentReplacer;
import com.tpago.movil.init.BaseInitFragment;

import javax.inject.Inject;

/**
 * @author hecvasro
 */
public final class RegisterFragment extends BaseInitFragment implements RegisterContainer {
  private RegisterComponent component;

  @Inject
  @FragmentQualifier
  FragmentReplacer fragmentReplacer;

  public static RegisterFragment create() {
    return new RegisterFragment();
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_register, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Initializes the dependency injector.
    component = getInitComponent().plus(new RegisterModule(this));
    // Injects all the annotated dependencies.
    component.inject(this);
    // Shows the initial fragment.
    fragmentReplacer.begin(PhoneNumberFormFragment.create())
      .commit();
  }

  @Override
  public RegisterComponent getRegisterComponent() {
    return component;
  }
}
