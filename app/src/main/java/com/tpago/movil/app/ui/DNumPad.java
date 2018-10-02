package com.tpago.movil.app.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tpago.movil.R;
import com.tpago.movil.util.function.Action;
import com.tpago.movil.util.function.Consumer;
import com.tpago.movil.util.ObjectHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hecvasro
 */
@Deprecated
public final class DNumPad extends LinearLayout {

  private int buttonTextColor;
  private boolean dotButtonEnabled;
  private Drawable deleteButtonDrawable;

  @BindViews({
    R.id.digitZeroNumPadButton,
    R.id.digitOneNumPadButton,
    R.id.digitTwoNumPadButton,
    R.id.digitThreeNumPadButton,
    R.id.digitFourNumPadButton,
    R.id.digitFiveNumPadButton,
    R.id.digitSixNumPadButton,
    R.id.digitSevenNumPadButton,
    R.id.digitEightNumPadButton,
    R.id.digitNineNumPadButton
  })
  List<Button> digitButtonList;

  @BindView(R.id.dotNumPadButton) Button dotButton;
  @BindView(R.id.deleteNumPadButton) ImageView deleteButton;

  private final List<Consumer<Integer>> digitConsumerList = new ArrayList<>();
  private final List<Action> dotActionList = new ArrayList<>();
  private final List<Action> deleteActionList = new ArrayList<>();

  public DNumPad(Context context, AttributeSet attrSet) {
    super(context, attrSet);

    // Extracts all attributes from the given attribute sync.
    final TypedArray array = context.obtainStyledAttributes(attrSet, R.styleable.DNumPad);

    this.buttonTextColor = array.getColor(R.styleable.DNumPad_buttonTextColor, 0);
    this.dotButtonEnabled = array.getBoolean(R.styleable.DNumPad_dotButtonEnabled, false);
    this.deleteButtonDrawable = array.getDrawable(R.styleable.DNumPad_deleteButtonDrawable);

    array.recycle();

    // Inflates its layout.
    LayoutInflater.from(context)
      .inflate(R.layout.d_num_pad, this);
  }

  @OnClick({
    R.id.digitZeroNumPadButton,
    R.id.digitOneNumPadButton,
    R.id.digitTwoNumPadButton,
    R.id.digitThreeNumPadButton,
    R.id.digitFourNumPadButton,
    R.id.digitFiveNumPadButton,
    R.id.digitSixNumPadButton,
    R.id.digitSevenNumPadButton,
    R.id.digitEightNumPadButton,
    R.id.digitNineNumPadButton
  })
  final void onDigitNumPadButtonClicked(Button button) {
    final int digit = Integer.parseInt(
      button.getText()
        .toString()
    );
    for (Consumer<Integer> consumer : this.digitConsumerList) {
      consumer.accept(digit);
    }
  }

  @OnClick(R.id.dotNumPadButton)
  final void onDotNumPadButtonClicked() {
    for (Action action : this.dotActionList) {
      action.run();
    }
  }

  @OnClick(R.id.deleteNumPadButton)
  final void onDeleteNumPadButtonClicked() {
    for (Action action : this.deleteActionList) {
      action.run();
    }
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    // Injects all annotated views, resources, and methods.
    ButterKnife.bind(this);

    // Initializes all digit buttons.
    for (Button button : this.digitButtonList) {
      button.setTextColor(this.buttonTextColor);
    }

    // Initializes the dot button.
    this.dotButton.setTextColor(this.buttonTextColor);

    this.dotButton.setClickable(this.dotButtonEnabled);
    this.dotButton.setVisibility(this.dotButtonEnabled ? View.VISIBLE : View.INVISIBLE);

    // Initializes the delete button.
    this.deleteButton.setImageDrawable(this.deleteButtonDrawable);
  }

  public final void addDigitConsumer(Consumer<Integer> consumer) {
    ObjectHelper.checkNotNull(consumer, "consumer");
    if (!this.digitConsumerList.contains(consumer)) {
      this.digitConsumerList.add(consumer);
    }
  }

  public final void removeDigitConsumer(Consumer<Integer> consumer) {
    ObjectHelper.checkNotNull(consumer, "consumer");
    if (this.digitConsumerList.contains(consumer)) {
      this.digitConsumerList.remove(consumer);
    }
  }

  public final void addDotAction(Action action) {
    ObjectHelper.checkNotNull(action, "action");
    if (!this.dotActionList.contains(action)) {
      this.dotActionList.add(action);
    }
  }

  public final void removeDotAction(Action action) {
    ObjectHelper.checkNotNull(action, "action");
    if (this.dotActionList.contains(action)) {
      this.dotActionList.remove(action);
    }
  }

  public final void addDeleteAction(Action action) {
    ObjectHelper.checkNotNull(action, "action");
    if (!this.deleteActionList.contains(action)) {
      this.deleteActionList.add(action);
    }
  }

  public final void removeDeleteAction(Action action) {
    ObjectHelper.checkNotNull(action, "action");
    if (this.deleteActionList.contains(action)) {
      this.deleteActionList.remove(action);
    }
  }
}
