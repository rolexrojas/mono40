package com.mono40.movil.app.ui.main.settings;

import android.content.Context;
import androidx.annotation.AttrRes;
import androidx.annotation.Nullable;
import androidx.legacy.widget.Space;
import android.util.AttributeSet;

import com.mono40.movil.R;

/**
 * @author hecvasro
 */
public class SettingsSectionDivider extends Space {

  public SettingsSectionDivider(Context context) {
    this(context, null);
  }

  public SettingsSectionDivider(Context context, @Nullable AttributeSet attributeSet) {
    this(context, attributeSet, R.attr.settingsSectionDividerStyle);
  }

  public SettingsSectionDivider(
    Context context,
    @Nullable AttributeSet attributeSet,
    @AttrRes int defaultStyleAttribute
  ) {
    super(context, attributeSet, defaultStyleAttribute);
  }
}
