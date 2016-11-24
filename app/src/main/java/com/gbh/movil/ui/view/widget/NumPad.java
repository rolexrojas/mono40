package com.gbh.movil.ui.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
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

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
public class NumPad extends LinearLayout {
  @ColorInt
  private int buttonColor;

  private Drawable deleteButtonDrawable;
  private OnButtonClickedListener listener;
  private boolean showDot;

  @BindViews({ R.id.num_pad_button_dot, R.id.num_pad_button_zero, R.id.num_pad_button_one,
    R.id.num_pad_button_two, R.id.num_pad_button_three, R.id.num_pad_button_four,
    R.id.num_pad_button_five, R.id.num_pad_button_six, R.id.num_pad_button_seven,
    R.id.num_pad_button_eight, R.id.num_pad_button_nine })
  List<Button> textButtons;
  @BindView(R.id.num_pad_button_dot)
  Button dotButton;
  @BindView(R.id.num_pad_button_delete)
  ImageButton deleteButton;

  public NumPad(Context context) {
    this(context, null);
  }

  public NumPad(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public NumPad(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setOrientation(VERTICAL);
    final TypedArray array = context
      .obtainStyledAttributes(attrs, R.styleable.NumPad, defStyleAttr, 0);
    try {
      buttonColor = array
        .getColor(R.styleable.NumPad_buttonColor, ContextCompat.getColor(context, R.color.black));
      deleteButtonDrawable = array.getDrawable(R.styleable.NumPad_deleteButtonDrawable);
      showDot = array.getBoolean(R.styleable.NumPad_showDot, false);
    } finally {
      array.recycle();
    }
    LayoutInflater.from(context).inflate(R.layout.num_pad, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
    for (Button button : textButtons) {
      button.setTextColor(buttonColor);
    }
    deleteButton.setImageDrawable(deleteButtonDrawable);
    dotButton.setEnabled(showDot);
    dotButton.setVisibility(showDot ? View.VISIBLE : View.INVISIBLE);
  }

  /**
   * TODO
   *
   * @param button
   *   {@link Button} that was clicked.
   */
  @OnClick({ R.id.num_pad_button_dot, R.id.num_pad_button_zero, R.id.num_pad_button_one,
    R.id.num_pad_button_two, R.id.num_pad_button_three, R.id.num_pad_button_four,
    R.id.num_pad_button_five, R.id.num_pad_button_six, R.id.num_pad_button_seven,
    R.id.num_pad_button_eight, R.id.num_pad_button_nine })
  void onTextButtonClicked(@NonNull Button button) {
    final String content = button.getText().toString();
    Timber.d("Text (%1$s) button clicked", content);
    if (Utils.isNotNull(listener)) {
      listener.onTextButtonClicked(content);
    }
  }

  /**
   * TODO
   */
  @OnClick(R.id.num_pad_button_delete)
  void onDeleteButtonClicked() {
    Timber.d("Delete button clicked");
    if (Utils.isNotNull(listener)) {
      listener.onDeleteButtonClicked();
    }
  }

  /**
   * TODO
   *
   * @param listener
   *   TODO
   */
  public void setOnButtonClickedListener(@Nullable OnButtonClickedListener listener) {
    this.listener = listener;
  }

  /**
   * TODO
   */
  public interface OnButtonClickedListener {
    /**
     * TODO
     *
     * @param content
     *   TODO
     */
    void onTextButtonClicked(@NonNull String content);

    /**
     * TODO
     */
    void onDeleteButtonClicked();
  }
}
