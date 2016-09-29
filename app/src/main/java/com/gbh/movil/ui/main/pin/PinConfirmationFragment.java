package com.gbh.movil.ui.main.pin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gbh.movil.R;
import com.gbh.movil.ui.FullScreenDialogFragment;
import com.gbh.movil.ui.view.widget.NumPad;
import com.gbh.movil.ui.view.widget.PinView;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * TODO
 *
 * @author hecvasro
 */
public class PinConfirmationFragment extends FullScreenDialogFragment implements PinView.Listener,
  NumPad.OnButtonClickedListener {
  /**
   * TODO
   */
  private static final String KEY_CALLBACK = "callback";

  /**
   * TODO
   */
  private Unbinder unbinder;

  /**
   * TODO
   */
  private Callback callback;

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

  /**
   * TODO
   *
   * @param callback
   *   TODO
   *
   * @return TODO
   */
  public static PinConfirmationFragment newInstance(@NonNull Callback callback) {
    final Bundle bundle = new Bundle();
    bundle.putSerializable(KEY_CALLBACK, callback);
    final PinConfirmationFragment fragment = new PinConfirmationFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Attaches the callback to the fragment.
    final Bundle bundle = savedInstanceState != null ? savedInstanceState : getArguments();
    if (bundle != null && bundle.containsKey(KEY_CALLBACK)) {
      final Serializable serializable = bundle.getSerializable(KEY_CALLBACK);
      if (serializable instanceof Callback) {
        callback = (Callback) serializable;
      } else {
        throw new ClassCastException("Argument must implement the 'Callback' interface");
      }
    } else {
      throw new NullPointerException("Callback must be set");
    }
  }

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
    // Adds a listener that gets notified every time the pin view starts or finishes loading.
    pinView.setListener(this);
    // Adds a listener that gets notified every time a button of the num pad is clicked.
    numPad.setOnButtonClickedListener(this);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Removes the listener that gets notified every time a button of the num pad is clicked.
    numPad.setOnButtonClickedListener(null);
    // Removes the listener that gets notified every time the pin view starts or finishes loading.
    pinView.setListener(this);
    // Unbinds all the annotated views and methods.
    unbinder.unbind();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    // Detaches the callback from the fragment.
    callback = null;
  }

  /**
   * TODO
   *
   * @param succeeded
   *   TODO
   */
  public final void resolve(boolean succeeded) {
    pinView.resolve(succeeded);
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

  @Override
  public void onLoadingStarted(@NonNull String pin) {
    callback.confirm(pin);
  }

  @Override
  public void onLoadingFinished(boolean succeeded) {
    if (succeeded) {
      dismiss();
    }
  }

  /**
   * TODO
   */
  public interface Callback extends Serializable {
    /**
     * TODO
     *
     * @param pin
     *   TODO
     */
    void confirm(@NonNull String pin);
  }
}
