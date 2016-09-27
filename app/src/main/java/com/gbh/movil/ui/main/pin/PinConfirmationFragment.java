package com.gbh.movil.ui.main.pin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gbh.movil.R;
import com.gbh.movil.ui.view.widget.NumPad;
import com.gbh.movil.ui.view.widget.PinView;

import butterknife.BindView;
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
  private Unbinder unbinder;

  /**
   * TODO
   */
  @BindView(R.id.pin_view)
  PinView pinView;

  /**
   * TODO
   */
  @BindView(R.id.num_pad)
  NumPad numPad;

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
    if (TextUtils.isDigitsOnly(content)) {
      pinView.push(Integer.parseInt(content));
    }
  }

  @Override
  public void onDeleteButtonClicked() {
    pinView.pop();
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
