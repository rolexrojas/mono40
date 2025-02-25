package com.tpago.movil.app.ui.main.settings.profile.change.password;

import com.google.auto.value.AutoValue;
import com.tpago.movil.app.ui.activity.toolbar.ActivityToolbar;
import com.tpago.movil.app.ui.activity.toolbar.FragmentToolbarRetained;
import com.tpago.movil.app.ui.fragment.base.FragmentBase;
import com.tpago.movil.app.ui.main.settings.profile.change.FragmentChange;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class ArgumentChangePassword extends ActivityToolbar.Argument {

  public static ArgumentChangePassword create() {
    return new AutoValue_ArgumentChangePassword();
  }

  ArgumentChangePassword() {
  }

  @Override
  public FragmentToolbarRetained createFragmentRetained() {
    return FragmentChange.create();
  }

  @Override
  public FragmentBase createFragment() {
    return FragmentChangePassword.create();
  }
}
