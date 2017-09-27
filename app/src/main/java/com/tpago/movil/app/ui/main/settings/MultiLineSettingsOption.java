package com.tpago.movil.app.ui.main.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.util.StringHelper;

import butterknife.BindView;

/**
 * {@link SettingsOption Option} that has primary and secondary lines of text.
 *
 * @author hecvasro
 */
public class MultiLineSettingsOption extends SettingsOption {

  private String secondaryText;

  @BindView(R.id.secondaryTextView) TextView secondaryTextView;

  public MultiLineSettingsOption(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);

    // Extracts the secondary text from the given attribute set.
    final TypedArray array = context.obtainStyledAttributes(
      attributeSet,
      R.styleable.MultiLineSettingsOption
    );

    this.secondaryText = array.getString(R.styleable.MultiLineSettingsOption_secondaryText);

    array.recycle();
  }

  private void updateSecondaryTextView() {
    this.secondaryTextView.setText(this.secondaryText);
  }

  public final void secondaryText(@Nullable String text) {
    this.secondaryText = StringHelper.nullIfEmpty(text);
    this.updateSecondaryTextView();
  }

  @Override
  @LayoutRes
  protected int layoutResId() {
    return R.layout.settings_option_multi_line;
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    this.updateSecondaryTextView();
  }
}
