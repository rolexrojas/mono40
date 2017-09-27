package com.tpago.movil.app.ui.main.settings;

import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.main.BaseMainFragment;

import butterknife.BindView;

/**
 * @author hecvasro
 */
public final class SettingsIndexFragment extends BaseMainFragment {

  @BindView(R.id.profileSettingsOption) MultiLineSettingsOption profileSettingsOption;

  public static SettingsIndexFragment create() {
    return new SettingsIndexFragment();
  }

  @Override
  @LayoutRes
  protected int layoutResId() {
    return R.layout.settings_index;
  }

  @Override
  @StringRes
  protected int titleResId() {
    return R.string.settings;
  }
}
