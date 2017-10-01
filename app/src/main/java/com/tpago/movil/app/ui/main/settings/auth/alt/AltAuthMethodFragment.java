package com.tpago.movil.app.ui.main.settings.auth.alt;

import android.support.v4.app.Fragment;

import com.google.auto.value.AutoValue;
import com.tpago.movil.R;
import com.tpago.movil.app.ui.BaseFragment;
import com.tpago.movil.app.ui.main.FragmentCreator;

/**
 * @author hecvasro
 */
public final class AltAuthMethodFragment extends BaseFragment {

  public static FragmentCreator creator() {
    return new AutoValue_AltAuthMethodFragment_Creator();
  }

  @Override
  protected int layoutResId() {
    return R.layout.fragment_alt_auth_method;
  }

  @AutoValue
  public static abstract class Creator extends FragmentCreator {

    Creator() {
    }

    @Override
    public Fragment create() {
      return new AltAuthMethodFragment();
    }
  }
}
