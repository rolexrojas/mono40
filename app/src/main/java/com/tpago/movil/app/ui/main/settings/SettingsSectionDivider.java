package com.tpago.movil.app.ui.main.settings;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.Space;
import android.util.AttributeSet;

import com.tpago.movil.R;

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
