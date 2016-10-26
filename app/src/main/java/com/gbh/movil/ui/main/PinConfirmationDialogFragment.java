package com.gbh.movil.ui.main;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gbh.movil.R;
import com.gbh.movil.ui.view.BaseAnimatorListener;
import com.gbh.movil.ui.view.widget.NumPad;
import com.gbh.movil.ui.view.widget.PinView;

import java.io.Serializable;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.codetail.animation.ViewAnimationUtils;

/**
 * TODO
 *
 * @author hecvasro
 */
public class PinConfirmationDialogFragment extends DialogFragment
  implements DialogInterface.OnShowListener, PinView.Listener, NumPad.OnButtonClickedListener {
  private static final String KEY_CENTER_X = "centerX";
  private static final String KEY_CENTER_Y = "centerY";
  private static final String KEY_ACTION_DESCRIPTION = "actionDescription";

  private Unbinder unbinder;

  private int centerX;
  private int centerY;

  private String actionDescription;

  private Callback callback;

  @BindInt(R.integer.pin_confirmation_enter_duration)
  int enterDuration;
  @BindInt(R.integer.pin_confirmation_exit_duration)
  int exitDuration;

  @BindView(R.id.frame_layout_container)
  FrameLayout containerFrameLayout;
  @BindView(R.id.linear_layout_container)
  LinearLayout containerLinearLayout;
  @BindView(R.id.text_view_action_description)
  TextView actionDescriptionTextView;
  @BindView(R.id.pin_view)
  PinView pinView;
  @BindView(R.id.num_pad)
  NumPad numPad;

  /**
   * TODO
   *
   * @param centerX
   *   TODO
   * @param centerY
   *   TODO
   * @param actionDescription
   *   TODO
   * @param callback
   *   TODO
   *
   * @return TODO
   */
  public static PinConfirmationDialogFragment newInstance(int centerX, int centerY,
    @NonNull String actionDescription, @NonNull Callback callback) {
    final Bundle bundle = new Bundle();
    bundle.putInt(KEY_CENTER_X, centerX);
    bundle.putInt(KEY_CENTER_Y, centerY);
    bundle.putString(KEY_ACTION_DESCRIPTION, actionDescription);
    final PinConfirmationDialogFragment fragment = new PinConfirmationDialogFragment();
    fragment.callback = callback;
    fragment.setArguments(bundle);
    return fragment;
  }

  /**
   * TODO
   */
  private void finish() {
    // Prepares the background animator.
    final int radius = (int) Math.hypot(containerFrameLayout.getWidth(),
      containerLinearLayout.getHeight());
    final Animator animator = ViewAnimationUtils
      .createCircularReveal(containerFrameLayout, centerX, centerY, radius, 0)
      .setDuration(exitDuration);
    animator.addListener(new BaseAnimatorListener() {
      @Override
      public void onAnimationEnd(Animator animator) {
        dismiss();
      }
    });
    // Starts the background animator.
    animator.start();
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
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setStyle(DialogFragment.STYLE_NO_FRAME, R.style.FullScreenDialogTheme);
    final Bundle bundle = savedInstanceState != null ? savedInstanceState : getArguments();
    if (bundle != null) {
      if (!bundle.containsKey(KEY_CENTER_X)) {
        throw new NullPointerException("Center X must be specified as an argument");
      } else if (!bundle.containsKey(KEY_CENTER_Y)) {
        throw new NullPointerException("Center Y must be specified as an argument");
      } else if (!bundle.containsKey(KEY_ACTION_DESCRIPTION)) {
        throw new NullPointerException("Action description must be specified as an argument");
      } else {
        centerX = bundle.getInt(KEY_CENTER_X);
        centerY = bundle.getInt(KEY_CENTER_Y);
        actionDescription = bundle.getString(KEY_ACTION_DESCRIPTION);
      }
    } else {
      throw new NullPointerException("All required arguments must be specified");
    }
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    final Dialog dialog = new Dialog(getActivity(), getTheme()) {
      @Override
      public void onBackPressed() {
        finish();
      }
    };
    dialog.setOnShowListener(this);
    return dialog;
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
    // Sets the action description.
    actionDescriptionTextView.setText(actionDescription);
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

  @Override
  public void onShow(DialogInterface dialog) {
    // Prepares the foreground for the animation.
    containerLinearLayout.setAlpha(0.0F);
    containerLinearLayout.setScaleX(0.9F);
    containerLinearLayout.setScaleY(0.9F);
    // Prepares the foreground animator.
    final AnimatorSet foregroundAnimator = new AnimatorSet();
    foregroundAnimator.setDuration(enterDuration / 2);
    foregroundAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    foregroundAnimator.play(ObjectAnimator.ofFloat(containerLinearLayout, "alpha", 1.0F))
      .with(ObjectAnimator.ofFloat(containerLinearLayout, "scaleX", 1.0F))
      .with(ObjectAnimator.ofFloat(containerLinearLayout, "scaleY", 1.0F))
      .after(enterDuration / 2);
    // Prepares the background animator.
    final int radius = (int) Math.hypot(containerFrameLayout.getWidth(),
      containerLinearLayout.getHeight());
    final Animator backgroundAnimator = ViewAnimationUtils
      .createCircularReveal(containerFrameLayout, centerX, centerY, 0, radius)
      .setDuration(enterDuration);
    // Prepares the combined animator.
    final AnimatorSet animator = new AnimatorSet();
    animator.play(foregroundAnimator).with(backgroundAnimator);
    // Starts the combined animator.
    animator.start();
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
  public void onConfirmationStarted(@NonNull String pin) {
    callback.confirm(pin);
  }

  @Override
  public void onConfirmationFinished(boolean succeeded) {
    if (succeeded) {
      finish();
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
