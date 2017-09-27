package com.tpago.movil.app.ui.main.settings;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tpago.movil.R;
import com.tpago.movil.util.ObjectHelper;

import butterknife.BindView;

/**
 * {@link SettingsOption Option} that has an icon, a primary text line, and a selection {@link
 * ImageView indicator}.
 */
public class SelectableSettingsOption extends SettingsOption {

  private Uri iconUri;
  private boolean selected;

  @BindView(R.id.icon) ImageView iconImageView;
  @BindView(R.id.indicator) ImageView indicatorImageView;

  public SelectableSettingsOption(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);
  }

  private void updateIconImageView() {
    if (ObjectHelper.isNull(this.iconUri)) {
      this.iconImageView.setVisibility(View.GONE);
    } else {
      this.iconImageView.setVisibility(View.VISIBLE);

      Picasso.with(this.getContext())
        .load(this.iconUri)
        .noFade()
        .resizeDimen(R.dimen.smallIconSize, R.dimen.smallIconSize)
        .into(this.iconImageView);
    }
  }

  public final void iconUri(@Nullable Uri uri) {
    this.iconUri = uri;
    this.updateIconImageView();
  }

  private void updateIndicatorImageView() {
    this.indicatorImageView.setVisibility(this.selected ? View.VISIBLE : View.GONE);
  }

  public final void selected(boolean selected) {
    this.selected = selected;
    this.updateIndicatorImageView();
  }

  @Override
  @LayoutRes
  protected int layoutResId() {
    return R.layout.settings_option_selectable;
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    this.updateIconImageView();
    this.updateIndicatorImageView();
  }
}
