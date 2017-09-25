package com.tpago.movil.app.ui.main.settings;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.tpago.movil.R;

/**
 * {@link AppCompatTextView View} that displays a section title of the settings.
 *
 * @author hecvasro
 */
public class SettingsSectionTitleView extends AppCompatTextView {

  public SettingsSectionTitleView(Context context, AttributeSet attributeSet) {
    super(context, attributeSet, R.attr.settingsSectionTitleViewStyle);
  }
}
