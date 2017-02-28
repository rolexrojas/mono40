package com.tpago.movil.dep.ui.main;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.dep.misc.Utils;
import com.tpago.movil.dep.misc.rx.RxUtils;
import com.tpago.movil.dep.ui.view.BaseAnimatorListener;
import com.tpago.movil.dep.ui.view.widget.PinView;
import com.tpago.movil.dep.ui.view.widget.pad.Digit;
import com.tpago.movil.dep.ui.view.widget.pad.NumPad;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.codetail.animation.ViewAnimationUtils;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class PinConfirmator {
  /**
   * TODO
   */
  private static final String TAG = PinConfirmator.class.getSimpleName();

  /**
   * TODO
   */
  private final FragmentManager fragmentManager;

  /**
   * TODO
   *
   * @param fragmentManager
   *   TODO
   */
  public PinConfirmator(@NonNull FragmentManager fragmentManager) {
    this.fragmentManager = fragmentManager;
  }

  /**
   * TODO
   *
   * @param message
   *   TODO
   * @param originX
   *   TODO
   * @param originY
   *   TODO
   */
  public final void show(@NonNull String message, int originX, int originY,
    @NonNull Callback callback) {
    final Fragment fragment = fragmentManager.findFragmentByTag(TAG);
    if (Utils.isNotNull(fragment)) {
      if (fragment instanceof PinConfirmatorFragment) {
        ((PinConfirmatorFragment) fragment).dismiss();
      } else {
        fragmentManager.beginTransaction()
          .remove(fragment)
          .commit();
      }
    }
    final PinConfirmatorFragment pinConfirmatorFragment = PinConfirmatorFragment
      .newInstance(message, originX, originY);
    pinConfirmatorFragment.setCallback(callback);
    pinConfirmatorFragment.show(fragmentManager, TAG);
  }

  /**
   * TODO
   *
   * @param message
   *   TODO
   */
  public final void show(@NonNull String message, @NonNull Callback callback) {
    show(message, -1, -1, callback);
  }

  /**
   * TODO
   */
  public static class PinConfirmatorFragment extends DialogFragment
    implements DialogInterface.OnShowListener, PinView.Listener, NumPad.OnDigitClickedListener,
    NumPad.OnDeleteClickedListener {
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_ORIGIN_X = "originX";
    private static final String KEY_ORIGIN_Y = "originY";

    private Unbinder unbinder;

    private Callback callback;
    private String message;
    private int originX;
    private int originY;

    private Subscription subscription = Subscriptions.unsubscribed();

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
    NumPad numPad;

    /**
     * TODO
     *
     * @param message
     *   TODO
     * @param originX
     *   TODO
     * @param originY
     *   TODO
     *
     * @return TODO
     */
    @NonNull
    private static PinConfirmatorFragment newInstance(@NonNull String message, int originX, int originY) {
      final Bundle bundle = new Bundle();
      bundle.putString(KEY_MESSAGE, message);
      bundle.putInt(KEY_ORIGIN_X, originX);
      bundle.putInt(KEY_ORIGIN_Y, originY);
      final PinConfirmatorFragment fragment = new PinConfirmatorFragment();
      fragment.setArguments(bundle);
      return fragment;
    }

    /**
     * TODO
     *
     * @param callback
     *   TODO
     */
    private void setCallback(@NonNull Callback callback) {
      this.callback = callback;
    }

    /**
     * TODO
     */
    private void finish() {
      if (Utils.isNotNull(containerFrameLayout)) {
        // Prepares the background animator.
        final int radius = (int) Math.hypot(containerFrameLayout.getWidth(),
          containerLinearLayout.getHeight());
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      final Bundle bundle = Utils.isNotNull(savedInstanceState) ? savedInstanceState : getArguments();
      if (Utils.isNotNull(bundle)) {
        if (!bundle.containsKey(KEY_ORIGIN_X)) {
          throw new NullPointerException("Argument '" + KEY_ORIGIN_X + "' is missing");
        } else if (!bundle.containsKey(KEY_ORIGIN_Y)) {
          throw new NullPointerException("Argument '" + KEY_ORIGIN_Y + "' is missing");
        } else if (!bundle.containsKey(KEY_MESSAGE)) {
          throw new NullPointerException("Argument '" + KEY_MESSAGE + "' is missing");
        } else {
          originX = bundle.getInt(KEY_ORIGIN_X);
          originY = bundle.getInt(KEY_ORIGIN_Y);
          message = bundle.getString(KEY_MESSAGE);
        }
      } else {
        throw new NullPointerException("All required arguments must be specified");
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
      super.onViewCreated(view, savedInstanceState);
      // Binds all the annotated views and methods.
      unbinder = ButterKnife.bind(this, view);
      // Sets the action description.
      actionDescriptionTextView.setText(message);
      // Adds a listener that gets notified every time the pin view starts or finishes loading.
      pinView.setListener(this);
      // Adds a listener that gets notified every time a button of the num pad is clicked.
      numPad.setOnDigitClickedListener(this);
      numPad.setOnDeleteClickedListener(this);
    }

    @Override
    public void onStop() {
      super.onStop();
      RxUtils.unsubscribe(subscription);
    }

    @Override
    public void onDestroyView() {
      super.onDestroyView();
      // Removes the listener that gets notified every time a button of the num pad is clicked.
      numPad.setOnDeleteClickedListener(null);
      numPad.setOnDigitClickedListener(null);
      // Removes the listener that gets notified every time the pin view starts or finishes loading.
      pinView.setListener(this);
      // Unbinds all the annotated views and methods.
      unbinder.unbind();
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
        .createCircularReveal(containerFrameLayout, originX, originY, 0, radius)
        .setDuration(enterDuration);
      // Prepares the combined animator.
      final AnimatorSet animator = new AnimatorSet();
      animator.play(foregroundAnimator).with(backgroundAnimator);
      // Starts the combined animator.
      animator.start();
    }

    @Override
    public void onConfirmationStarted(@NonNull String pin) {
      subscription = callback.confirm(pin)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Boolean>() {
          @Override
          public void call(Boolean succeeded) {
            pinView.resolve(succeeded);
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "Failed to confirm user's pin");
            // TODO: Let the user know that the PIN confirmation failed.
          }
        });
    }

    @Override
    public void onConfirmationFinished(boolean succeeded) {
      if (succeeded) {
        finish();
      }
    }

    @Override
    public void onDigitClicked(@NonNull Digit digit) {
      pinView.push(digit.getValue());
    }

    @Override
    public void onDeleteClicked() {
      pinView.pop();
    }
  }

  /**
   * TODO
   */
  public interface Callback {
    /**
     * TODO
     *
     * @param pin
     *   TODO
     *
     * @return TODO
     */
    @NonNull
    Observable<Boolean> confirm(@NonNull String pin);
  }
}
