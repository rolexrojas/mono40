package com.gbh.movil.ui.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.gbh.movil.R;
import com.gbh.movil.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * TODO
 *
 * @author hecvasro
 */
public class NumPad extends LinearLayout {
  /**
   * TODO
   */
  private final Map<Button, Digit> digitValues = new HashMap<>();

  /**
   * TODO
   */
  @ColorInt
  private int tintColor;

  /**
   * TODO
   */
  private boolean dotEnabled;

  /**
   * TODO
   */
  private boolean deleteEnabled;
  /**
   * TODO
   */
  @DrawableRes
  private int deleteDrawable;

  /**
   * TODO
   */
  private OnDigitClickedListener onDigitClickedListener;
  /**
   * TODO
   */
  private OnDotClickedListener onDotClickedListener;
  /**
   * TODO
   */
  private OnDeleteClickedListener onDeleteClickedListener;

  @BindViews({ R.id.num_pad_cell_digit_zero, R.id.num_pad_cell_digit_one,
    R.id.num_pad_cell_digit_two, R.id.num_pad_cell_digit_three, R.id.num_pad_cell_digit_four,
    R.id.num_pad_cell_digit_five, R.id.num_pad_cell_digit_six, R.id.num_pad_cell_digit_seven,
    R.id.num_pad_cell_digit_eight, R.id.num_pad_cell_digit_nine })
  List<Button> digitButtons;

  @BindView(R.id.num_pad_cell_dot)
  Button dotButton;

  @BindView(R.id.num_pad_cell_delete)
  ImageButton deleteButton;

  public NumPad(Context context) {
    this(context, null);
  }

  public NumPad(Context context, AttributeSet attrs) {
    this(context, attrs, R.attr.numPadStyle);
  }

  public NumPad(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setOrientation(VERTICAL);
    final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NumPad,
      defStyleAttr, 0);
    try {
      tintColor = array.getColor(R.styleable.NumPad_tintColor,
        ContextCompat.getColor(context, R.color.app_widget_num_pad_tint));
      dotEnabled = array.getBoolean(R.styleable.NumPad_dotEnabled, false);
      deleteEnabled = array.getBoolean(R.styleable.NumPad_deleteEnabled, true);
      deleteDrawable = array.getResourceId(R.styleable.NumPad_deleteDrawable,
        R.drawable.app_widget_num_pad_delete);
    } finally {
      array.recycle();
    }
    LayoutInflater.from(context).inflate(R.layout.widget_num_pad, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
    int i = 0;
    Digit digit;
    for (Button button : digitButtons) {
      digit = new Digit(i);
      digitValues.put(button, digit);
      button.setTextColor(tintColor);
      button.setText(digit.getValue().toString());
      i++;
    }
    dotButton.setText(".");
    dotButton.setTextColor(tintColor);
    dotButton.setClickable(dotEnabled);
    dotButton.setVisibility(dotEnabled ? View.VISIBLE : View.INVISIBLE);
    final Drawable drawable = ContextCompat.getDrawable(getContext(), deleteDrawable);
    if (Utils.isNotNull(drawable)) {
      drawable.mutate().setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
    }
    deleteButton.setImageDrawable(drawable);
    deleteButton.setClickable(deleteEnabled);
    deleteButton.setVisibility(deleteEnabled ? View.VISIBLE : View.INVISIBLE);
  }

  /**
   * TODO
   *
   * @param button
   *   {@link Button} that was clicked.
   */
  @OnClick({ R.id.num_pad_cell_digit_zero, R.id.num_pad_cell_digit_one,
    R.id.num_pad_cell_digit_two, R.id.num_pad_cell_digit_three, R.id.num_pad_cell_digit_four,
    R.id.num_pad_cell_digit_five, R.id.num_pad_cell_digit_six, R.id.num_pad_cell_digit_seven,
    R.id.num_pad_cell_digit_eight, R.id.num_pad_cell_digit_nine })
  void onDigitClicked(@NonNull Button button) {
    if (Utils.isNotNull(onDigitClickedListener)) {
      onDigitClickedListener.onDigitClicked(digitValues.get(button));
    }
  }

  @OnClick(R.id.num_pad_cell_dot)
  void onDotClicked() {
    if (dotEnabled && Utils.isNotNull(onDotClickedListener)) {
      onDotClickedListener.onDotClicked();
    }
  }

  /**
   * TODO
   */
  @OnClick(R.id.num_pad_cell_delete)
  void onDeleteClicked() {
    if (deleteEnabled && Utils.isNotNull(onDeleteClickedListener)) {
      onDeleteClickedListener.onDeleteClicked();
    }
  }

  /**
   * TODO
   *
   * @param listener
   *   TODO
   */
  public void setOnDigitClickedListener(@Nullable OnDigitClickedListener listener) {
    onDigitClickedListener = listener;
  }

  /**
   * TODO
   *
   * @param listener
   *   TODO
   */
  public void setOnDotClickedListener(@Nullable OnDotClickedListener listener) {
    onDotClickedListener = listener;
  }

  /**
   * TODO
   *
   * @param listener
   *   TODO
   */
  public void setOnDeleteClickedListener(@Nullable OnDeleteClickedListener listener) {
    onDeleteClickedListener = listener;
  }

  /**
   * Num pad's cell representation.
   */
  private static class Cell<T> {
    /**
     * Cell's value.
     */
    private final T value;

    /**
     * Constructs a new cell.
     *
     * @param value
     *   Cell's value.
     */
    Cell(@NonNull T value) {
      this.value = value;
    }

    /**
     * Gets the value of the cell.
     *
     * @return Cell's value.
     */
    @NonNull
    public final T getValue() {
      return value;
    }

    @Override
    public boolean equals(Object object) {
      return super.equals(object) || (Utils.isNotNull(object) && object instanceof Cell
        && ((Cell) object).value.equals(value));
    }

    @Override
    public int hashCode() {
      return value.hashCode();
    }
  }

  /**
   * Num pad's digit representation.
   */
  public static class Digit extends Cell<Integer> {
    private Digit(@NonNull Integer value) {
      super(value);
    }
  }

  /**
   * TODO
   */
  public interface OnDigitClickedListener {
    /**
     * TODO
     *
     * @param digit
     *   TODO
     */
    void onDigitClicked(@NonNull Digit digit);
  }

  /**
   * TODO
   */
  public interface OnDotClickedListener {
    /**
     * TODO
     */
    void onDotClicked();
  }

  /**
   * TODO
   */
  public interface OnDeleteClickedListener {
    /**
     * TODO
     */
    void onDeleteClicked();
  }
}
