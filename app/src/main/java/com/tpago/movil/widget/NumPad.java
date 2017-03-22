package com.tpago.movil.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tpago.movil.Digit;
import com.tpago.movil.R;
import com.tpago.movil.util.Objects;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hecvasro
 */
public final class NumPad extends LinearLayout {
  private int color;
  private boolean dotEnabled;

  private OnDotClickedListener dotListener;
  private OnDigitClickedListener digitListener;
  private OnDeleteClickedListener deleteListener;

  @BindViews({
    R.id.num_pad_cell_zero,
    R.id.num_pad_cell_one,
    R.id.num_pad_cell_two,
    R.id.num_pad_cell_three,
    R.id.num_pad_cell_four,
    R.id.num_pad_cell_five,
    R.id.num_pad_cell_six,
    R.id.num_pad_cell_seven,
    R.id.num_pad_cell_eight,
    R.id.num_pad_cell_nine
  })
  List<Button> digitButtons;
  @BindView(R.id.num_pad_cell_dot)
  Button dotButton;
  @BindView(R.id.num_pad_cell_delete)
  ImageView deleteButton;

  public NumPad(Context context) {
    super(context);
    initializeNumPad(context, null, 0);
  }

  public NumPad(Context context, AttributeSet attrs) {
    super(context, attrs);
    initializeNumPad(context, attrs, 0);
  }

  public NumPad(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initializeNumPad(context, attrs, defStyleAttr);
  }

  private void initializeNumPad(Context c, AttributeSet attrs, int defStyleAttr) {
    // Sets the orientation.
    setOrientation(VERTICAL);
    // Initializes the attributes from the given replace or from the default one.
    final TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.NumPad, defStyleAttr, 0);
    try {
      color = a.getColor(R.styleable.NumPad_color, ContextCompat.getColor(c, R.color.common_white));
      dotEnabled = a.getBoolean(R.styleable.DepNumPad_dotEnabled, false);
    } finally {
      a.recycle();
    }
    // Inflates the layout.
    LayoutInflater.from(getContext()).inflate(R.layout.widget_num_pad, this);
  }

  @OnClick({
    R.id.num_pad_cell_zero,
    R.id.num_pad_cell_one,
    R.id.num_pad_cell_two,
    R.id.num_pad_cell_three,
    R.id.num_pad_cell_four,
    R.id.num_pad_cell_five,
    R.id.num_pad_cell_six,
    R.id.num_pad_cell_seven,
    R.id.num_pad_cell_eight,
    R.id.num_pad_cell_nine
  })
  void onDigitClicked(Button button) {
    final Digit digit = Digit.find(Integer.parseInt(button.getText().toString()));
    if (Objects.isNotNull(digitListener)) {
      digitListener.onDigitClicked(digit);
    }
  }

  @OnClick(R.id.num_pad_cell_dot)
  void onDotClicked() {
    if (Objects.isNotNull(dotListener)) {
      dotListener.onDotClicked();
    }
  }

  @OnClick(R.id.num_pad_cell_delete)
  void onDeleteClicked() {
    if (Objects.isNotNull(deleteListener)) {
      deleteListener.onDeleteClicked();
    }
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    // Binds all annotated views, resources and methods.
    ButterKnife.bind(this);
    // Sets the color of each digit button.
    for (Button button : digitButtons) {
      button.setTextColor(color);
    }
    // Sets the color of the dot button.
    dotButton.setTextColor(color);
    // Sets the visibility of the dot button.
    dotButton.setClickable(dotEnabled);
    dotButton.setVisibility(dotEnabled ? View.VISIBLE : View.INVISIBLE);
    // Sets the color of the delete button.
    final Drawable drawable = deleteButton.getDrawable();
    if (Objects.isNotNull(drawable)) {
      drawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }
    deleteButton.setImageDrawable(drawable);
  }

  public void setOnDotClickedListener(OnDotClickedListener dotListener) {
    this.dotListener = this.dotEnabled ? dotListener : null;
  }

  public void setOnDigitClickedListener(OnDigitClickedListener digitListener) {
    this.digitListener = digitListener;
  }

  public void setOnDeleteClickedListener(OnDeleteClickedListener deleteListener) {
    this.deleteListener = deleteListener;
  }

  public interface OnDotClickedListener {
    void onDotClicked();
  }

  public interface OnDigitClickedListener {
    void onDigitClicked(Digit digit);
  }

  public interface OnDeleteClickedListener {
    void onDeleteClicked();
  }
}
