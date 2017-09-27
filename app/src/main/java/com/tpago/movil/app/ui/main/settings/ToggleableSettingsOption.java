package com.tpago.movil.app.ui.main.settings;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.widget.Switch;

import com.tpago.movil.R;

import butterknife.BindView;

/**
 * {@link SettingsOption Option} that has a primary line of text and a toggle {@link Switch
 * indicator}.
 *
 * @author hecvasro
 */
public class ToggleableSettingsOption extends SettingsOption {

  private boolean checked = false;

  @BindView(R.id.indicator) Switch indicatorSwitch;

  public ToggleableSettingsOption(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);
  }

  private void updateIndicatorSwitch() {
    this.indicatorSwitch.setChecked(this.checked);
  }

  public final void checked(boolean checked) {
    this.checked = checked;
    this.updateIndicatorSwitch();
  }

  @Override
  @LayoutRes
  protected int layoutResId() {
    return R.layout.settings_option_toggleable;
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    this.updateIndicatorSwitch();
  }
}
