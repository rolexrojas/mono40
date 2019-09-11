package com.tpago.movil.app.ui.main.settings;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
  private int primaryTextColor;

  @BindView(R.id.primaryTextView) TextView primaryTextView;

  SettingsOption(Context context, AttributeSet attributeSet) {
    this(context, attributeSet, R.attr.settingsOptionStyle);
  }

  SettingsOption(Context context, AttributeSet attributeSet, int defaultStyleAttribute) {
    super(context, attributeSet, defaultStyleAttribute);

    // Extracts the primary text from the given attribute sync.
    final TypedArray array = context.obtainStyledAttributes(
      attributeSet,
      R.styleable.SettingsOption,
      defaultStyleAttribute,
      R.style.SettingsOption
    );

    this.primaryText = array.getString(R.styleable.SettingsOption_primaryText);
    this.primaryTextColor = array.getColor(
      R.styleable.SettingsOption_primaryTextColor,
      ContextCompat.getColor(context, R.color.normalSettingsText)
    );

    array.recycle();

    // Inflates its layout.
    LayoutInflater.from(context)
      .inflate(this.layoutResId(), this);
  }

  private void updatePrimaryTextView() {
    this.primaryTextView.setText(this.primaryText);
    this.primaryTextView.setTextColor(this.primaryTextColor);
  }

  public final void setPrimaryText(@Nullable String text) {
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
