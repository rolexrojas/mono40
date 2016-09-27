package com.gbh.movil.ui.main.pin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gbh.movil.R;
import com.gbh.movil.ui.view.widget.NumPad;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * TODO
 *
 * @author hecvasro
 */
public class PinConfirmationFragment extends Fragment implements NumPad.OnButtonClickedListener {
  /**
   * TODO
   */
  private static final long PIN_DOT_ANIMATION_DURATION = 75L;

  /**
   * TODO
   */
  private static final int SELECTED_DIGITS_LIMIT = 4;

  /**
   * TODO
   */
  private final List<Integer> selectedDigits = new ArrayList<>();

  /**
   * TODO
   */
  private Unbinder unbinder;

  /**
   * TODO
   */
  @BindViews({ R.id.pin_dot_first, R.id.pin_dot_second, R.id.pin_dot_third, R.id.pin_dot_fourth })
  List<View> pinDots;

  /**
   * TODO
   */
  @BindView(R.id.num_pad)
  NumPad numPad;

  /**
   * TODO
   */
//  @BindViews({ R.id.num_pad_button_zero, R.id.num_pad_button_one, R.id.num_pad_button_two,
//    R.id.num_pad_button_three, R.id.num_pad_button_four, R.id.num_pad_button_five,
//    R.id.num_pad_button_six, R.id.num_pad_button_seven, R.id.num_pad_button_eight,
//    R.id.num_pad_button_nine })
//  List<View> digitPinPadButtons;

  /**
   * TODO
   */
//  @BindView(R.id.num_pad_button_delete)
//  View deletePinPadButton;

  /**
   * TODO
   *
   * @param index
   *   TODO
   * @param show
   *   TODO
   */
  private void updatePinDot(final int index, final boolean show) {
    if (index >= 0 && index < pinDots.size()) {
      final View dot = pinDots.get(index);
      dot.postDelayed(new Runnable() {
        @Override
        public void run() {
          dot.setVisibility(show ? View.VISIBLE : View.GONE);
          if (index == (SELECTED_DIGITS_LIMIT - 1)) {
          }
        }
      }, PIN_DOT_ANIMATION_DURATION);
    }
  }

  /**
   * TODO
   *
   * @param digitPinPadButton
   *   TODO
   */
//  @OnClick({ R.id.num_pad_button_zero, R.id.num_pad_button_one, R.id.num_pad_button_two,
//    R.id.num_pad_button_three, R.id.num_pad_button_four, R.id.num_pad_button_five,
//    R.id.num_pad_button_six, R.id.num_pad_button_seven, R.id.num_pad_button_eight,
//    R.id.num_pad_button_nine })
//  void onDigitPinPadButtonClicked(@NonNull TextView digitPinPadButton) {
//    final int digit = Integer.parseInt(digitPinPadButton.getText().toString());
//    Timber.d("Pin pad digit (%1$d) button clicked", digit);
//    if (selectedDigits.size() < SELECTED_DIGITS_LIMIT) {
//      selectedDigits.add(digit);
//      updatePinDot(selectedDigits.size() - 1, true);
//    }
//  }

  /**
   * TODO
   */
//  @OnClick(R.id.num_pad_button_delete)
//  void onDeletePinPadButtonClicked() {
//    Timber.d("Pin pad delete button clicked");
//    final int index = selectedDigits.size() - 1;
//    if (index >= 0) {
//      selectedDigits.remove(index);
//      updatePinDot(index, false);
//    }
//  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_pin_confirmation, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all the annotated views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Adds a listener that gets notified every time a button of the num pad is clicked.
    numPad.setOnButtonClickedListener(this);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Removes the listener that gets notified every time a button of the num pad is clicked.
    numPad.setOnButtonClickedListener(null);
    // Unbinds all the annotated views and methods.
    unbinder.unbind();
  }

  /**
   * TODO
   *
   * @param correctly
   *   TODO
   */
  public final void dismiss(boolean correctly) {
    // TODO
  }

  @Override
  public void onTextButtonClicked(@NonNull String content) {
    // TODO
  }

  @Override
  public void onDeleteButtonClicked() {
    // TODO
  }

  /**
   * TODO
   */
  public interface Listener {
    /**
     * TODO
     *
     * @param pin
     *   TODO
     */
    void onConfirm(@NonNull String pin);
  }
}
