package com.tpago.movil.app.ui.main.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.util.StringHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Base option of the settings.
 *
 * @author hecvasro
 */
abstract class SettingsOption extends RelativeLayout {

  private String primaryText;

  @BindView(R.id.primaryTextView) TextView primaryTextView;

  SettingsOption(Context context, AttributeSet attributeSet) {
    this(context, attributeSet, R.attr.settingsOptionStyle);
  }

  SettingsOption(Context context, AttributeSet attributeSet, int defaultStyleAttribute) {
    super(context, attributeSet, defaultStyleAttribute);

    // Extracts the primary text from the given attribute set.
    final TypedArray array = context.obtainStyledAttributes(
      attributeSet,
      R.styleable.SettingsOption,
      defaultStyleAttribute,
      0
    );

    this.primaryText = array.getString(R.styleable.SettingsOption_primaryText);

    array.recycle();

    // Inflates its layout.
    LayoutInflater.from(context)
      .inflate(this.layoutResId(), this);
  }

  private void updatePrimaryTextView() {
    this.primaryTextView.setText(this.primaryText);
  }

  public final void primaryText(@Nullable String text) {
    this.primaryText = StringHelper.nullIfEmpty(text);
    this.updatePrimaryTextView();
  }

  /**
   * Resource identifier of its layout.
   */
  @LayoutRes
  protected abstract int layoutResId();

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    // Injects all annotated views, resources, and methods.
    ButterKnife.bind(this);

    this.updatePrimaryTextView();
  }
}
