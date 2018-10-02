package com.tpago.movil.d.ui.view.widget;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Dimension;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.util.Truss;

import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * TODO
 *
 * @author hecvasro
 */
public class PrefixableTextView extends TextView {
  /**
   * TODO
   */
  private static final float DEFAULT_PREFIX_TEXT_SIZE_PROPORTION = 0.5F;

  /**
   * TODO
   */
  private CharSequence content;
  /**
   * TODO
   */
  private CharSequence prefix;

  /**
   * TODO
   */
  @StyleRes
  private int prefixTextAppearance;
  /**
   * TODO
   */
  @ColorInt
  private int prefixTextColor;
  /**
   * TODO
   */
  private String prefixFontPath;
  /**
   * TODO
   */
  private float prefixTextSizeProportion;
  /**
   * TODO
   */
  @Dimension
  private int prefixPaddingEnd;

  public PrefixableTextView(Context context) {
    this(context, null);
  }

  public PrefixableTextView(Context context, AttributeSet attrs) {
    this(context, attrs, R.attr.prefixableTextViewStyle);
  }

  public PrefixableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DepPrefixableTextView,
      defStyleAttr, R.style.Dep_App_Text_Widget_PrefixableTextView);
    try {
      content = array.getString(R.styleable.DepPrefixableTextView_content);
      prefix = array.getString(R.styleable.DepPrefixableTextView_prefix);
      final Resources resources = getResources();
      final int defaultPrefixTextColor = ContextCompat.getColor(context,
        R.color.d_app_text_widget_prefixable_text_view_content);
      prefixTextColor = array.getColor(R.styleable.DepPrefixableTextView_prefixTextColor,
        defaultPrefixTextColor);
      prefixFontPath = array.getString(R.styleable.DepPrefixableTextView_prefixFontPath);
      if (TextUtils.isEmpty(prefixFontPath)) {
        prefixFontPath = context.getString(R.string.app_text_widget_prefixable_text_view_font);
      }
      prefixTextSizeProportion = array.getFloat(
        R.styleable.DepPrefixableTextView_prefixTextSizeProportion,
        DEFAULT_PREFIX_TEXT_SIZE_PROPORTION);
      final int defaultPrefixPaddingEnd = resources.getDimensionPixelOffset(
        R.dimen.app_text_widget_prefixable_text_view_prefix_padding_end);
      prefixPaddingEnd = array.getDimensionPixelOffset(
        R.styleable.DepPrefixableTextView_prefixPaddingEnd, defaultPrefixPaddingEnd);
    } finally {
      array.recycle();
    }
    setMaxLines(1);
    updateText();
  }

  /**
   * TODO
   */
  private void updateText() {
    final CharSequence text;
    if (!TextUtils.isEmpty(prefix)) {
      final Context context = getContext();
      final AssetManager assetManager = getResources().getAssets();
      final CharSequence styledPrefix = Truss.create()
        .pushSpan(new TextAppearanceSpan(context, prefixTextAppearance))
        .pushSpan(new ForegroundColorSpan(prefixTextColor))
        .pushSpan(new CalligraphyTypefaceSpan(TypefaceUtils.load(assetManager, prefixFontPath)))
        .pushSpan(new RelativeSizeSpan(prefixTextSizeProportion))
        .pushSpan(new SuperscriptSpan())
        .append(this.prefix)
        .build();
      if (!TextUtils.isEmpty(content)) {
        text = TextUtils.concat(styledPrefix, content);
      } else {
        text = styledPrefix;
      }
    } else {
      text = content;
    }
    setText(text);
  }

  /**
   * TODO
   *
   * @param colorId
   *   TODO
   */
  public void setTextColorFromResource(@ColorRes int colorId) {
    setTextColor(ContextCompat.getColor(getContext(), colorId));
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @Nullable
  public CharSequence getContent() {
    return content;
  }

  /**
   * TODO
   *
   * @param content
   *   TODO
   */
  public void setContent(@Nullable CharSequence content) {
    this.content = content;
    this.updateText();
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @Nullable
  public CharSequence getPrefix() {
    return prefix;
  }

  /**
   * TODO
   *
   * @param prefix
   *   TODO
   */
  public void setPrefix(@Nullable CharSequence prefix) {
    this.prefix = prefix;
    this.updateText();
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @ColorInt
  public int getPrefixTextColor() {
    return prefixTextColor;
  }

  /**
   * TODO
   *
   * @param color
   *   TODO
   */
  public void setPrefixTextColor(@ColorInt int color) {
    prefixTextColor = color;
    updateText();
  }

  /**
   * TODO
   *
   * @param colorId
   *   TODO
   */
  public void setPrefixTextColorFromResource(@ColorRes int colorId) {
    setPrefixTextColor(ContextCompat.getColor(getContext(), colorId));
  }

  /**
   * TODO
   *
   * @return TODO
   */
  public float getPrefixTextSizeProportion() {
    return prefixTextSizeProportion;
  }

  /**
   * TODO
   *
   * @param proportion
   *   TODO
   */
  public void setPrefixTextSizeProportion(float proportion) {
    prefixTextSizeProportion = proportion;
    updateText();
  }

  @Override
  public void setTextSize(int unit, float size) {
    super.setTextSize(unit, size);
    updateText();
  }
}
