package com.tpago.movil.d.ui.main;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.DNumPad;
import com.tpago.movil.d.ui.FullScreenDialogFragment;
import com.tpago.movil.d.ui.view.BaseAnimatorListener;
import com.tpago.movil.d.ui.view.widget.PinView;
import com.tpago.movil.dep.widget.FullSizeLoadIndicator;
import com.tpago.movil.dep.widget.LoadIndicator;
import com.tpago.movil.util.function.Action;
import com.tpago.movil.util.function.Consumer;
import com.tpago.movil.util.ObjectHelper;

import java.io.Serializable;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.codetail.animation.ViewAnimationUtils;

/**
 * @author hecvasro
 */
@Deprecated
public class PinConfirmationDialogFragment
  extends FullScreenDialogFragment
  implements DialogInterface.OnShowListener,
  PinView.Listener {

  private static final String TAG = PinConfirmationDialogFragment.class.getSimpleName();

  private static final String KEY_ACTION_DESCRIPTION = "actionDescription";
  private static final String KEY_ORIGIN_X = "originX";
  private static final String KEY_ORIGIN_Y = "originY";

  //TODO: Create automatic show

  public static void show(
    FragmentManager fragmentManager,
    String actionDescription,
    Callback callback,
    int originX,
    int originY
  ) {
    ObjectHelper.checkNotNull(fragmentManager, "fragmentManager");
    ObjectHelper.checkNotNull(callback, "callback");
    final Bundle argumentBundle = new Bundle();
    argumentBundle.putString(KEY_ACTION_DESCRIPTION, actionDescription);
    argumentBundle.putInt(KEY_ORIGIN_X, originX);
    argumentBundle.putInt(KEY_ORIGIN_Y, originY);
    final PinConfirmationDialogFragment fragment = new PinConfirmationDialogFragment();
    fragment.callback = callback;
    fragment.setArguments(argumentBundle);
    fragment.show(fragmentManager, TAG);
  }

  public static void dismiss(FragmentManager fragmentManager, boolean succedded) {
    ObjectHelper.checkNotNull(fragmentManager, "fragmentManager");
    final Fragment fragment = fragmentManager.findFragmentByTag(TAG);
    if (ObjectHelper.isNotNull(fragment) && fragment instanceof PinConfirmationDialogFragment) {
      final PinConfirmationDialogFragment dialogFragment = (PinConfirmationDialogFragment) fragment;
      dialogFragment.loadIndicator.stop();
//      if (succedded) {
        dialogFragment.finish();
//      }
    }
  }

  private Unbinder unbinder;

  private int originX;
  private int originY;

  private String actionDescription;

  private Callback callback;
  private LoadIndicator loadIndicator;

  @BindInt(android.R.integer.config_shortAnimTime)
  int enterDuration;
  @BindInt(android.R.integer.config_shortAnimTime)
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
  DNumPad DNumPad;

  private Consumer<Integer> numPadDigitConsumer;
  private Action numPadDeleteAction;

  private void finish() {
    if (ObjectHelper.isNotNull(containerFrameLayout)) {
      // Prepares the background animator.
      final int radius = (int) Math.hypot(
        containerFrameLayout.getWidth(),
        containerLinearLayout.getHeight()
      );
      final Animator animator = ViewAnimationUtils
        .createCircularReveal(containerFrameLayout, originX, originY, radius, 0)
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
  }

  @Override
  protected int getCustomTheme() {
    return R.style.Dep_FullScreenDialogTheme;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final Bundle bundle
      = ObjectHelper.isNotNull(savedInstanceState) ? savedInstanceState : getArguments();
    if (ObjectHelper.isNotNull(bundle)) {
      if (!bundle.containsKey(KEY_ORIGIN_X)) {
        throw new NullPointerException("Center X must be specified as an argument");
      } else if (!bundle.containsKey(KEY_ORIGIN_Y)) {
        throw new NullPointerException("Center Y must be specified as an argument");
      } else if (!bundle.containsKey(KEY_ACTION_DESCRIPTION)) {
        throw new NullPointerException("Action description must be specified as an argument");
      } else {
        originX = bundle.getInt(KEY_ORIGIN_X);
        originY = bundle.getInt(KEY_ORIGIN_Y);
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
  public View onCreateView(
    LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState
  ) {
    return inflater.inflate(R.layout.d_fragment_pin_confirmation, container, false);
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
    this.numPadDigitConsumer = (digit) -> this.pinView.push(digit);
    this.DNumPad.addDigitConsumer(this.numPadDigitConsumer);
    this.numPadDeleteAction = this.pinView::pop;
    this.DNumPad.addDeleteAction(this.numPadDeleteAction);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Removes the listener that gets notified every time a button of the num pad is clicked.
    this.DNumPad.removeDeleteAction(this.numPadDeleteAction);
    this.numPadDeleteAction = null;
    this.DNumPad.removeDigitConsumer(this.numPadDigitConsumer);
    this.numPadDigitConsumer = null;
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
    final int radius = (int) Math.hypot(
      containerFrameLayout.getWidth(),
      containerLinearLayout.getHeight()
    );
    final Animator backgroundAnimator = ViewAnimationUtils
      .createCircularReveal(containerFrameLayout, originX, originY, 0, radius)
      .setDuration(enterDuration);
    // Prepares the combined animator.
    final AnimatorSet animator = new AnimatorSet();
    animator.play(foregroundAnimator)
      .with(backgroundAnimator);
    // Starts the combined animator.
    animator.start();
  }

  @Override
  public void onConfirmationStarted(@NonNull String pin) {
    if (ObjectHelper.isNull(loadIndicator)) {
      loadIndicator = new FullSizeLoadIndicator(getChildFragmentManager());
    }
    loadIndicator.start();
    callback.confirm(pin);
  }

  public interface Callback extends Serializable {

    void confirm(String pin);
  }
}
