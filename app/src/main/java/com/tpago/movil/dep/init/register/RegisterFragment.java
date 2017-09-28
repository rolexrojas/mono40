package com.tpago.movil.dep.init.register;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpago.movil.dep.Avatar;
import com.tpago.movil.R;
import com.tpago.movil.dep.BackEventHandler;
import com.tpago.movil.dep.FragmentBackEventHandler;
import com.tpago.movil.app.ui.FragmentQualifier;
import com.tpago.movil.app.ui.FragmentReplacer;
import com.tpago.movil.dep.init.BaseInitFragment;
import com.tpago.movil.dep.init.LogoAnimator;

import javax.inject.Inject;

/**
 * @author hecvasro
 */
public final class RegisterFragment extends BaseInitFragment implements RegisterContainer {
  private RegisterComponent component;

  private FragmentBackEventHandler fragmentBackEventHandler;

  @Inject Avatar avatar;

  @Inject BackEventHandler backEventHandler;
  @Inject LogoAnimator logoAnimator;
  @Inject @FragmentQualifier FragmentReplacer fragmentReplacer;

  public static RegisterFragment create() {
    return new RegisterFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Initializes the dependency injector.
    component = getInitComponent().plus(new RegisterModule(this));
    // Injects all the annotated dependencies.
    component.inject(this);
    // Pushes a fragment back event handler to the global handler.
    fragmentBackEventHandler = new FragmentBackEventHandler(getChildFragmentManager());
    backEventHandler.push(fragmentBackEventHandler);
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
    // Shows the initial fragment.
    fragmentReplacer.begin(NameRegisterFormFragment.create())
      .commit();
    // Clears the avatar, if needed.
    avatar.clear();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    // Removes the fragment back event handler from the global handler.
    backEventHandler.remove(fragmentBackEventHandler);
    fragmentBackEventHandler = null;
  }

  @Override
  public RegisterComponent getRegisterComponent() {
    return component;
  }
}
