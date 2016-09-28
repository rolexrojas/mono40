package com.gbh.movil.ui.view.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gbh.movil.R;
import com.gbh.movil.ui.view.BaseAnimatorListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * TODO: Find a better name for this class.
 *
 * @author hecvasro
 */
public class PinView extends LinearLayout {
  /**
   * TODO
   */
  private static final int DEFAULT_MAX_LENGTH = 4;

  /**
   * TODO
   */
  private static final long VISIBILITY_DELAY_DOT = 75L;

  /**
   * TODO
   */
  private static final String ANIMATION_PROPERTY_ALPHA = "alpha";

  /**
   * TODO
   */
  private static final String ANIMATION_PROPERTY_Y = "y";

  /**
   * TODO
   */
  private static final long ANIMATION_DURATION_DOT = 300L;

  /**
   * TODO
   */
  private final List<Integer> digits = new ArrayList<>();

  /**
   * TODO
   */
  private View[] dots = new View[DEFAULT_MAX_LENGTH];

  /**
   * TODO
   */
  private View cursor;

  /**
   * TODO
   */
  private AnimatorSet loadAnimator;

  /**
   * TODO
   */
  private AnimatorSet resetAnimator;

  /**
   * TODO
   */
  private Listener listener;

  /**
   * TODO
   */
  @BindView(R.id.linear_layout_container)
  LinearLayout containerLinearLayout;

  /**
   * TODO
   */
  @BindView(R.id.image_view_done_icon)
  ImageView doneIconImageView;

  public PinView(Context context) {
    this(context, null);
  }

  public PinView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public PinView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    // Sets the orientation of the layout.
    setOrientation(VERTICAL);
    // Inflates the content layout.
    LayoutInflater.from(context).inflate(R.layout.pin_view, this);
  }

  /**
   * TODO
   */
  private void initializeLoadAnimator() {
    View dot;
    ValueAnimator fadeIn;
    ValueAnimator fadeOut;
    final List<Pair<ValueAnimator, ValueAnimator>> animators = new ArrayList<>();
    for (int i = 0; i < DEFAULT_MAX_LENGTH; i++) {
      dot = dots[i];
      fadeIn = ObjectAnimator.ofFloat(dot, ANIMATION_PROPERTY_ALPHA, 0F, 1F)
        .setDuration(ANIMATION_DURATION_DOT);
      fadeOut = ObjectAnimator.ofFloat(dot, ANIMATION_PROPERTY_ALPHA, 1F, 0F)
        .setDuration(ANIMATION_DURATION_DOT);
      animators.add(Pair.create(fadeIn, fadeOut));
    }
    final AnimatorSet fadeOutAnimator = new AnimatorSet();
    for (int i = 0; i < DEFAULT_MAX_LENGTH; i++) {
      if (i == 0) {
        fadeOutAnimator.play(animators.get(i).second);
      } else {
        fadeOutAnimator.play(animators.get(i).second).after(animators.get(i - 1).second);
      }
    }
    final AnimatorSet blinkAnimator = new AnimatorSet();
    for (int i = 0; i < DEFAULT_MAX_LENGTH; i++) {
      if (i == 0) {
        blinkAnimator.play(animators.get(i).first);
      } else {
        blinkAnimator.play(animators.get(i).first).after(animators.get(i - 1).first);
        blinkAnimator.play(animators.get(i - 1).second).after(animators.get(i).first);
        if (i == (DEFAULT_MAX_LENGTH - 1)) {
          blinkAnimator.play(animators.get(i).second).after(animators.get(i - 1).second);
        }
      }
    }
    blinkAnimator.addListener(new BaseAnimatorListener() {
      boolean canceled = false;

      @Override
      public void onAnimationStart(Animator animator) {
        Timber.d("Blink animation started");
        canceled = false;
      }

      @Override
      public void onAnimationEnd(Animator animator) {
        Timber.d("Blink animation ended and is %1$scanceled", canceled ? "" : "not ");
        if (!canceled) {
          blinkAnimator.start();
        }
      }

      @Override
      public void onAnimationCancel(Animator animator) {
        Timber.d("Blink animation canceled");
        canceled = true;
      }
    });
    loadAnimator = new AnimatorSet();
    loadAnimator.play(fadeOutAnimator);
    loadAnimator.play(blinkAnimator).after(fadeOutAnimator);
    loadAnimator.addListener(new BaseAnimatorListener() {
      @Override
      public void onAnimationStart(Animator animator) {
        Timber.d("Loading animation started");
        if (listener != null) {
          listener.onLoadingStarted(Arrays.toString(digits.toArray()));
        }
      }
    });
  }

  /**
   * TODO
   *
   * @param dot
   *   TODO
   * @param visible
   *   TODO
   */
  private void setDotVisibility(@NonNull final View dot, final boolean visible) {
    dot.postDelayed(new Runnable() {
      @Override
      public void run() {
        dot.setVisibility(visible ? View.VISIBLE : View.GONE);
      }
    }, VISIBILITY_DELAY_DOT);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    // Binds all the annotated views and methods.
    ButterKnife.bind(this);
    // Adds the dots to the container.
    View view;
    final LayoutInflater inflater = LayoutInflater.from(getContext());
    for (int i = 0; i < DEFAULT_MAX_LENGTH; i++) {
      view = inflater.inflate(R.layout.pin_dot, containerLinearLayout, false);
      view.setId(View.generateViewId());
      containerLinearLayout.addView(view);
      dots[i] = view;
    }
    // Adds the cursor to the container.
    cursor = inflater.inflate(R.layout.pin_cursor, containerLinearLayout, false);
    cursor.setId(View.generateViewId());
    containerLinearLayout.addView(cursor);
    // Initializes the load animator.
    initializeLoadAnimator();
  }

  /**
   * TODO
   *
   * @param listener
   *   TODO
   */
  public void setListener(@Nullable Listener listener) {
    this.listener = listener;
  }

  /**
   * TODO
   *
   * @param digit
   *   TODO
   */
  public void push(final int digit) {
    if (!loadAnimator.isRunning()) {
      if (digit >= 0 && digit <= 9 && digits.size() < DEFAULT_MAX_LENGTH) {
        digits.add(digit);
        final int size = digits.size();
        setDotVisibility(dots[size - 1], true);
        if (size == DEFAULT_MAX_LENGTH) {
          cursor.setVisibility(View.INVISIBLE);
          if (!loadAnimator.isRunning()) {
            loadAnimator.start();
          }
        }
      }
    }
  }

  /**
   * TODO
   */
  public void pop() {
    if (!loadAnimator.isRunning()) {
      final int last = digits.size() - 1;
      if (last >= 0) {
        digits.remove(last);
        setDotVisibility(dots[last], false);
        if (last < (DEFAULT_MAX_LENGTH - 1)) {
          if (cursor.getVisibility() == View.INVISIBLE) {
            cursor.setVisibility(View.VISIBLE);
          }
        }
      }
    }
  }

  /**
   * TODO
   */
  public void reset(boolean success) {
    if (resetAnimator == null || !resetAnimator.isRunning()) {
      if (loadAnimator.isRunning()) {
        loadAnimator.cancel();
      }
      resetAnimator = new AnimatorSet();
      if (success) {
        View dot;
        final AnimatorSet dotsAnimator = new AnimatorSet();
        final float[] dotYs = new float[DEFAULT_MAX_LENGTH];
        final int containerTop = containerLinearLayout.getTop();
        for (int i = 0; i < DEFAULT_MAX_LENGTH; i++) {
          dot = dots[i];
          dotYs[i] = dot.getY();
          dotsAnimator.playTogether(ObjectAnimator.ofFloat(dot, ANIMATION_PROPERTY_Y, containerTop),
            ObjectAnimator.ofFloat(dot, ANIMATION_PROPERTY_ALPHA, 0F));
        }
        dotsAnimator.addListener(new BaseAnimatorListener() {
          @Override
          public void onAnimationEnd(Animator animator) {
            View dot;
            for (int i = 0; i < DEFAULT_MAX_LENGTH; i++) {
              dot = dots[i];
              dot.setVisibility(View.GONE);
              dot.setAlpha(1F);
              dot.setY(dotYs[i]);
            }
          }
        });
        final AnimatorSet doneIconAnimator = new AnimatorSet();
        final int containerBottom = containerLinearLayout.getBottom();
        doneIconAnimator.setDuration(400L);
        doneIconAnimator.play(ObjectAnimator.ofFloat(doneIconImageView, ANIMATION_PROPERTY_Y,
          containerBottom, ((containerBottom - containerTop) / 2))).with(ObjectAnimator.ofFloat(
          doneIconImageView, ANIMATION_PROPERTY_ALPHA, 0F, 1F, 1F, 0F));
        doneIconAnimator.addListener(new BaseAnimatorListener() {
          @Override
          public void onAnimationStart(Animator animator) {
            doneIconImageView.setY(containerBottom);
            doneIconImageView.setAlpha(0F);
            doneIconImageView.setVisibility(View.VISIBLE);
          }

          @Override
          public void onAnimationEnd(Animator animator) {
            doneIconImageView.setVisibility(View.GONE);
            doneIconImageView.setAlpha(0F);
            doneIconImageView.setY(containerBottom);
          }
        });
        resetAnimator.play(dotsAnimator);
        resetAnimator.play(doneIconAnimator).after(dotsAnimator);
      }
      resetAnimator.addListener(new BaseAnimatorListener() {
        @Override
        public void onAnimationEnd(Animator animator) {
          Timber.d("Loading animation finished");
          digits.clear();
          cursor.setVisibility(View.VISIBLE);
          if (listener != null) {
            listener.onLoadingFinished();
          }
        }
      });
      resetAnimator.start();
    }
  }

  /**
   * TODO
   */
  public interface Listener {
    /**
     * TODO
     */
    void onLoadingStarted(@NonNull String pin);

    /**
     * TODO
     */
    void onLoadingFinished();
  }
}
