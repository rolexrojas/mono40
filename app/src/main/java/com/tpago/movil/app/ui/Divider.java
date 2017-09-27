package com.tpago.movil.app.ui;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.tpago.movil.R;

/**
 * @author hecvasro
 */
public class Divider extends View {

  public Divider(Context context) {
    this(context, null);
  }

  public Divider(Context context, @Nullable AttributeSet attributeSet) {
    this(context, attributeSet, R.attr.dividerStyle);
  }

  public Divider(
    Context context,
    @Nullable AttributeSet attributeSet,
    @AttrRes int defaultStyleAttribute
  ) {
    super(context, attributeSet, defaultStyleAttribute);
  }
}
