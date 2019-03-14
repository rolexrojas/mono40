package com.tpago.movil.app.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.tpago.movil.R;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.digit.Digit;
import com.tpago.movil.util.digit.DigitUtil;
import com.tpago.movil.util.function.Action;
import com.tpago.movil.util.function.Consumer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hecvasro
 */
public final class NumPad extends ConstraintLayout {

  private boolean dotButtonEnabled;

  @ColorInt private int textButtonTextColor;
  @DrawableRes private int deleteButtonImageSource;

  private final List<Consumer<Integer>> digitConsumers = new ArrayList<>();
  private final List<Action> deleteActions = new ArrayList<>();
  private final List<Action> dotActions = new ArrayList<>();

  @BindViews({
    R.id.num_pad_button_text_0,
    R.id.num_pad_button_text_1,
    R.id.num_pad_button_text_2,
    R.id.num_pad_button_text_3,
    R.id.num_pad_button_text_4,
    R.id.num_pad_button_text_5,
    R.id.num_pad_button_text_6,
    R.id.num_pad_button_text_7,
    R.id.num_pad_button_text_8,
    R.id.num_pad_button_text_9
  })
  List<Button> digitButtons;
  @BindView(R.id.num_pad_button_text_dot) Button dotButton;
  @BindView(R.id.num_pad_button_delete) ImageButton deleteButton;

  public NumPad(Context context, AttributeSet attrs) {
    super(context, attrs);

    // Extracts the given attributes.
    final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NumPad);
    try {
      this.dotButtonEnabled = array.getBoolean(R.styleable.NumPad_dotButtonEnabled, false);

      if (!array.hasValue(R.styleable.NumPad_textButtonTextColor)) {
        throw new IllegalArgumentException("!array.hasValue(R.styleable.NumPad_textButtonTextColor");
      }
      this.textButtonTextColor = array.getColor(R.styleable.NumPad_textButtonTextColor, 0);

      if (!array.hasValue(R.styleable.NumPad_deleteButtonImageSource)) {
        throw new IllegalArgumentException(
          "!array.hasValue(R.styleable.NumPad_deleteButtonImageSource");
      }
      this.deleteButtonImageSource = array.getResourceId(
        R.styleable.NumPad_deleteButtonImageSource,
        0
      );
    } finally {
      array.recycle();
    }

    // Inflates the associated layout.
    LayoutInflater.from(context)
      .inflate(R.layout.num_pad, this);
  }

  @OnClick({
    R.id.num_pad_button_text_0,
    R.id.num_pad_button_text_1,
    R.id.num_pad_button_text_2,
    R.id.num_pad_button_text_3,
    R.id.num_pad_button_text_4,
    R.id.num_pad_button_text_5,
    R.id.num_pad_button_text_6,
    R.id.num_pad_button_text_7,
    R.id.num_pad_button_text_8,
    R.id.num_pad_button_text_9
  })
  final void onDigitButtonPressed(Button button) {
    @Digit final int digit = DigitUtil.toDigit(button.getText());
    for (Consumer<Integer> consumer : this.digitConsumers) {
      consumer.accept(digit);
    }
  }

  @OnClick(R.id.num_pad_button_delete)
  final void onDeleteButtonPressed() {
    for (Action action : this.deleteActions) {
      action.run();
    }
  }

  @OnClick(R.id.num_pad_button_text_dot)
  final void onDotButtonPressed() {
    for (Action action : this.dotActions) {
      action.run();
    }
  }

  public final void addDigitConsumer(Consumer<Integer> consumer) {
    ObjectHelper.checkNotNull(consumer, "consumer");
    if (!this.digitConsumers.contains(consumer)) {
      this.digitConsumers.add(consumer);
    }
  }

  public final void removeDigitConsumer(Consumer<Integer> consumer) {
    ObjectHelper.checkNotNull(consumer, "consumer");
      this.digitConsumers.remove(consumer);
  }

  public final void addDeleteAction(Action action) {
    ObjectHelper.checkNotNull(action, "action");
    if (!this.deleteActions.contains(action)) {
      this.deleteActions.add(action);
    }
  }

  public final void removeDeleteAction(Action action) {
    ObjectHelper.checkNotNull(action, "action");
      this.deleteActions.remove(action);
  }

  public final void addDotAction(Action action) {
    ObjectHelper.checkNotNull(action, "action");
    if (!this.dotActions.contains(action)) {
      this.dotActions.add(action);
    }
  }

  public final void removeDotAction(Action action) {
    ObjectHelper.checkNotNull(action, "action");
      this.dotActions.remove(action);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    // Binds all annotated methods, resources, and views.
    ButterKnife.bind(this);

    // Apply the given attributes.
    for (Button digitButton : this.digitButtons) {
      digitButton.setTextColor(this.textButtonTextColor);
    }

    this.dotButton.setTextColor(this.textButtonTextColor);
    this.dotButton.setEnabled(this.dotButtonEnabled);
    this.dotButton.setVisibility(this.dotButtonEnabled ? View.VISIBLE : View.INVISIBLE);

    this.deleteButton.setImageResource(this.deleteButtonImageSource);
  }
}
