package com.gbh.movil.ui.view.widget;

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
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.gbh.movil.R;
import com.gbh.movil.ui.utils.Truss;

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
  @Dimension
  private int prefixTextSize;
  /**
   * TODO
   */
  @ColorInt
  private int prefixTextColor;
  /**
   * TODO
   */
  private String prefixFontPath;

  public PrefixableTextView(Context context) {
    this(context, null);
  }

  public PrefixableTextView(Context context, AttributeSet attrs) {
    this(context, attrs, R.attr.prefixableTextViewStyle);
  }

  public PrefixableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PrefixableTextView,
      defStyleAttr, R.style.App_Text_Widget_PrefixableTextView);
    try {
      prefix = array.getString(R.styleable.PrefixableTextView_prefix);
      prefixTextAppearance = array.getResourceId(
        R.styleable.PrefixableTextView_prefixTextAppearance,
        R.style.App_Text_Widget_PrefixableTextView_Prefix);
      final int defaultPrefixTextSize = getResources().getDimensionPixelSize(
        R.dimen.app_text_widget_prefixable_text_view_prefix);
      final int defaultPrefixTextColor = ContextCompat.getColor(context,
        R.color.app_text_widget_prefixable_text_view);
      final TypedArray prefixTextAppearanceArray = context.obtainStyledAttributes(
        prefixTextAppearance, R.styleable.PrefixableTextViewAppearance);
      try {
        prefixTextSize = prefixTextAppearanceArray.getDimensionPixelSize(
          R.styleable.PrefixableTextViewAppearance_android_textSize, defaultPrefixTextSize);
        prefixTextColor = prefixTextAppearanceArray.getColor(
          R.styleable.PrefixableTextViewAppearance_android_textColor, defaultPrefixTextColor);
        prefixFontPath = prefixTextAppearanceArray.getString(
          R.styleable.PrefixableTextViewAppearance_fontPath);
      } finally {
        prefixTextAppearanceArray.recycle();
      }
      if (array.hasValue(R.styleable.PrefixableTextView_prefixTextSize)) {
        prefixTextSize = array.getDimensionPixelSize(R.styleable.PrefixableTextView_prefixTextSize,
          defaultPrefixTextSize);
      }
      if (array.hasValue(R.styleable.PrefixableTextView_prefixTextColor)) {
        prefixTextColor = array.getColor(R.styleable.PrefixableTextView_prefixTextColor,
          defaultPrefixTextColor);
      }
      if (array.hasValue(R.styleable.PrefixableTextView_prefixFontPath)) {
        prefixFontPath = array.getString(R.styleable.PrefixableTextView_prefixFontPath);
      }
      if (TextUtils.isEmpty(prefixFontPath)) {
        prefixFontPath = context.getString(R.string.app_text_widget_prefixable_text_view_font);
      }
    } finally {
      array.recycle();
    }
    setPrefix(prefix);
  }

  /**
   * {@inheritDoc}
   * <p>
   * TODO
   */
  @Override
  public void setText(CharSequence text, BufferType type) {
    this.content = text;
    final CharSequence result;
    if (!TextUtils.isEmpty(this.prefix)) {
      final Context context = getContext();
      final Resources resources = getResources();
      final AssetManager assetManager = resources.getAssets();
      final CharSequence prefixResult = Truss.create()
        .pushSpan(new TextAppearanceSpan(context, prefixTextAppearance))
        .pushSpan(new AbsoluteSizeSpan(prefixTextSize))
        .pushSpan(new ForegroundColorSpan(prefixTextColor))
        .pushSpan(new CalligraphyTypefaceSpan(TypefaceUtils.load(assetManager, prefixFontPath)))
        .pushSpan(new SuperscriptSpan())
        .append(this.prefix)
        .build();
      result = TextUtils.concat(prefixResult, this.content);
    } else {
      result = this.content;
    }
    super.setText(result, type);
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
    this.setText(this.content);
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
    setTextColor(getTextColors());
  }

  /**
   * TODO
   *
   * @param colorId
   *   TODO
   */
  public void setPrefixTextColorFromResource(@ColorRes int colorId) {
    setTextColor(ContextCompat.getColor(getContext(), colorId));
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @Dimension
  public int getPrefixTextSize() {
    return prefixTextSize;
  }

  /**
   * TODO
   *
   * @param size
   *   TODO
   */
  public void setPrefixTextSize(@Dimension int size) {
    prefixTextSize = size;
    setTextSize(getTextSize());
  }
}
