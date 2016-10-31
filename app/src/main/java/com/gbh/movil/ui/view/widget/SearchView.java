package com.gbh.movil.ui.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gbh.movil.R;
import com.gbh.movil.ui.UiUtils;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * TODO
 *
 * @author hecvasro
 */
public class SearchView extends LinearLayout {
  private String hint;

  @BindColor(R.color.search_view_drawable)
  int drawableColor;

  @BindView(R.id.edit_text_query)
  EditText queryEditText;
  @BindView(R.id.image_view_search)
  ImageView searchImageView;
  @BindView(R.id.image_view_clear)
  ImageView clearImageView;

  public SearchView(Context context) {
    this(context, null);
  }

  public SearchView(Context context, AttributeSet attrs) {
    this(context, attrs, R.attr.searchViewStyle);
  }

  public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setOrientation(LinearLayout.HORIZONTAL);
    final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SearchView,
      defStyleAttr, R.style.Widget_SearchView);
    try {
      hint = array.getString(R.styleable.SearchView_hint);
    } finally {
      array.recycle();
    }
    LayoutInflater.from(context).inflate(R.layout.search_view, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
    UiUtils.setColorFilter(searchImageView, drawableColor, PorterDuff.Mode.SRC_IN);
    UiUtils.setColorFilter(clearImageView, drawableColor, PorterDuff.Mode.SRC_IN);
    setHint(hint);
  }

  public void setHint(@Nullable String hint) {
    this.hint = hint;
    this.queryEditText.setHint(hint);
  }
}
