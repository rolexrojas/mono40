package com.tpago.movil.app.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.StyleRes;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.util.Truss;
import com.tpago.movil.util.StringHelper;

/**
 * @author hecvasro
 */
public final class LabelPrefix extends Label {

  @StyleRes private Integer prefixAppearance;
  private CharSequence prefix;

  @StyleRes private Integer valueAppearance;
  private CharSequence value;

  public LabelPrefix(Context context, AttributeSet attrs) {
    super(context, attrs);

    // Applies default attributes.
    this.setMaxLines(1);

    // Extracts the given attributes.
    final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LabelPrefix);
    try {
      this.prefixAppearance = array
        .getResourceId(R.styleable.LabelPrefix_prefixAppearance, 0);
      this.prefix = array.getString(R.styleable.LabelPrefix_prefix);

      this.valueAppearance = array
        .getResourceId(R.styleable.LabelPrefix_valueAppearance, 0);
      this.value = array.getString(R.styleable.LabelPrefix_value);
    } finally {
      array.recycle();
    }

    // Updates the text.
    this.updateText();
  }

  private void updateText() {
    final Context context = this.getContext();

    final Truss builder = Truss.create();
    if (!StringHelper.isNullOrEmpty(this.prefix)) {
      builder
        .pushSpan(new TextAppearanceSpan(context, this.prefixAppearance))
        .pushSpan(new RelativeSizeSpan(0.5F))
        .pushSpan(new SuperscriptSpan())
        .append(this.prefix)
        .popSpans();
    }
    if (!StringHelper.isNullOrEmpty(this.value)) {
      builder
        .pushSpan(new TextAppearanceSpan(context, this.valueAppearance))
        .append(this.value)
        .popSpans();
    }
    this.setText(builder.build());
  }

  public final void setPrefix(String text) {
    this.prefix = StringHelper.nullIfEmpty(text);
    this.updateText();
  }

  public final void setValue(String text) {
    this.value = StringHelper.nullIfEmpty(text);
    this.updateText();
  }
}
