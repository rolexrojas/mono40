package com.tpago.movil.app.ui.main.settings;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;

import com.tpago.movil.R;

/**
 * {@link SettingsOption Option} that only has a primary line of text.
 *
 * @author hecvasro
 */
public class SingleLineSettingsOption extends SettingsOption {

  public SingleLineSettingsOption(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);
  }

  @Override
  @LayoutRes
  protected int layoutResId() {
    return R.layout.settings_option_single_line;
  }
}
