package com.gbh.movil.ui.view.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gbh.movil.R;
import com.gbh.movil.Utils;
import com.gbh.movil.ui.view.BaseAnimatorListener;

import java.util.ArrayList;
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
  private static final int DEFAULT_MAX_LENGTH = 4;

  private static final String ANIMATION_PROPERTY_ALPHA = "alpha";
  private static final String ANIMATION_PROPERTY_Y = "y";
  private static final String ANIMATION_PROPERTY_TRANSLATION_X = "translationX";

  private final List<Integer> digits = new ArrayList<>();

  private AnimatorSet failureResolveAnimator;
  private AnimatorSet loadAnimator;
  private AnimatorSet resolveAnimator;
  private AnimatorSet successResolveAnimator;
  private Listener listener;
  private ObjectAnimator cursorAnimator;
  private View cursor;
  private View[] dots = new View[DEFAULT_MAX_LENGTH];
  private boolean mustRestartLoading = true;

  @BindView(R.id.linear_layout_container)
  LinearLayout containerLinearLayout;
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
    setOrientation(VERTICAL);
    LayoutInflater.from(context).inflate(R.layout.pin_view, this);
  }

  /**
   * TODO
   *
   * @return TODO
   */
  private boolean isCursorVisible() {
    return cursor.getVisibility() == View.VISIBLE;
  }

  /**
   * TODO
   */
  private void showCursor() {
    if (!isCursorVisible()) {
      if (Utils.isNull(cursorAnimator)) {
        cursorAnimator = ObjectAnimator.ofFloat(cursor, ANIMATION_PROPERTY_ALPHA, 1F, 0F);
        cursorAnimator.setDuration(500L);
        cursorAnimator.setRepeatMode(ValueAnimator.REVERSE);
        cursorAnimator.setRepeatCount(ValueAnimator.INFINITE);
      }
      cursorAnimator.start();
      cursor.setVisibility(View.VISIBLE);
    }
  }

  /**
   * TODO
   */
  private void hideCursor() {
    if (isCursorVisible()) {
      cursor.setVisibility(View.GONE);
      if (Utils.isNotNull(cursorAnimator)) {
        cursorAnimator.cancel();
      }
    }
  }

  /**
   * TODO
   *
   * @return TODO
   */
  private boolean isLoading() {
    return Utils.isNotNull(loadAnimator) && loadAnimator.isRunning();
  }

  /**
   * TODO
   */
  private void startLoading() {
    if (!isLoading() && !isResolving()) {
      mustRestartLoading = true;
      if (Utils.isNull(loadAnimator)) {
        View dot;
        Animator currentFadeInAnimator;
        Animator currentFadeOutAnimator;
        Animator previousFadeInAnimator = null;
        Animator previousFadeOutAnimator = null;
        final AnimatorSet blinkAnimator = new AnimatorSet();
        final AnimatorSet fadeOutAnimator = new AnimatorSet();
        for (int i = 0; i < DEFAULT_MAX_LENGTH; i++) {
          dot = dots[i];
          currentFadeInAnimator = ObjectAnimator.ofFloat(dot, ANIMATION_PROPERTY_ALPHA, 1F);
          currentFadeOutAnimator = ObjectAnimator.ofFloat(dot, ANIMATION_PROPERTY_ALPHA, 0F);
          if (Utils.isNull(previousFadeInAnimator) && Utils.isNull(previousFadeOutAnimator)) {
            blinkAnimator.play(currentFadeInAnimator);
          } else {
            blinkAnimator.play(currentFadeInAnimator).after(previousFadeInAnimator);
            blinkAnimator.play(previousFadeOutAnimator).after(currentFadeInAnimator);
            if ((i + 1) == DEFAULT_MAX_LENGTH) {
              blinkAnimator.play(currentFadeOutAnimator).after(previousFadeOutAnimator);
            }
          }
          if (Utils.isNull(previousFadeOutAnimator)) {
            fadeOutAnimator.play(currentFadeOutAnimator);
          } else {
            fadeOutAnimator.play(currentFadeOutAnimator).after(previousFadeOutAnimator);
          }
          previousFadeInAnimator = currentFadeInAnimator;
          previousFadeOutAnimator = currentFadeOutAnimator;
        }
        blinkAnimator.addListener(new BaseAnimatorListener() {
          @Override
          public void onAnimationEnd(Animator animator) {
            if (mustRestartLoading) {
              blinkAnimator.start();
            }
          }
        });
        loadAnimator = new AnimatorSet();
        loadAnimator.play(fadeOutAnimator);
        loadAnimator.play(blinkAnimator).after(fadeOutAnimator);
        loadAnimator.addListener(new BaseAnimatorListener() {
          @Override
          public void onAnimationStart(Animator animator) {
            Timber.d("PIN confirmation started");
            if (Utils.isNotNull(listener)) {
              listener.onConfirmationStarted(TextUtils.join("", digits));
            }
          }
        });
      }
      loadAnimator.start();
    }
  }

  /**
   * TODO
   */
  private void stopLoading(boolean cancel) {
    if (isLoading() && !isResolving()) {
      mustRestartLoading = false;
      if (cancel) {
        loadAnimator.cancel();
      }
    }
  }

  /**
   * TODO
   */
  private void initializeSuccessResolveAnimator() {
    if (successResolveAnimator == null) {
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
      doneIconAnimator
        .play(ObjectAnimator.ofFloat(doneIconImageView, ANIMATION_PROPERTY_Y, containerBottom,
          ((containerBottom - containerTop) / 2)))
        .with(ObjectAnimator.ofFloat(doneIconImageView, ANIMATION_PROPERTY_ALPHA, 0F, 1F, 1F,
          0F));
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
      successResolveAnimator = new AnimatorSet();
      successResolveAnimator.play(dotsAnimator);
      successResolveAnimator.play(doneIconAnimator).after(dotsAnimator);
    }
  }

  /**
   * TODO
   */
  private void initializeFailureResolveAnimator() {
    if (Utils.isNull(failureResolveAnimator)) {
      int i;
      View dot;
      Animator currentFadeInAnimator;
      Animator previousFadeInAnimator = null;
      final AnimatorSet fadeInAnimator = new AnimatorSet();
      for (i = 0; i < DEFAULT_MAX_LENGTH; i++) {
        dot = dots[i];
        currentFadeInAnimator = ObjectAnimator.ofFloat(dot, ANIMATION_PROPERTY_ALPHA, 1F);
        if (Utils.isNull(previousFadeInAnimator)) {
          fadeInAnimator.play(currentFadeInAnimator);
        } else {
          fadeInAnimator.play(currentFadeInAnimator).after(previousFadeInAnimator);
        }
        previousFadeInAnimator = currentFadeInAnimator;
      }
      int translation;
      ObjectAnimator animator;
      final AnimatorSet wiggleAnimator = new AnimatorSet();
      for (i = 0; i < DEFAULT_MAX_LENGTH; i++) {
        dot = dots[i];
        translation = (dot.getTop() + dot.getHeight()) / 4;
        animator = ObjectAnimator
          .ofFloat(dot, ANIMATION_PROPERTY_TRANSLATION_X, -translation, 0F, translation, 0F);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(2);
        wiggleAnimator.play(ObjectAnimator.ofFloat(dot, ANIMATION_PROPERTY_ALPHA, 1F))
          .with(animator)
          .before(ObjectAnimator.ofFloat(dot, ANIMATION_PROPERTY_ALPHA, 0F));
      }
      wiggleAnimator.addListener(new BaseAnimatorListener() {
        @Override
        public void onAnimationEnd(Animator animator) {
          View dot;
          for (int i = 0; i < DEFAULT_MAX_LENGTH; i++) {
            dot = dots[i];
            dot.setVisibility(View.GONE);
            dot.setAlpha(1F);
          }
        }
      });
      failureResolveAnimator = new AnimatorSet();
      failureResolveAnimator.play(fadeInAnimator).after(loadAnimator);
      failureResolveAnimator.play(wiggleAnimator).after(fadeInAnimator);
    }
  }

  /**
   * TODO
   *
   * @return TODO
   */
  private boolean isResolving() {
    return Utils.isNotNull(resolveAnimator) && resolveAnimator.isRunning();
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
    }, 75L);
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
    // Shows and starts animating the cursor.
    showCursor();
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
    if (!isLoading() && !isResolving()) {
      if (digit >= 0 && digit <= 9 && digits.size() < DEFAULT_MAX_LENGTH) {
        digits.add(digit);
        final int size = digits.size();
        setDotVisibility(dots[size - 1], true);
        if (size == DEFAULT_MAX_LENGTH) {
          hideCursor();
          startLoading();
        }
      }
    }
  }

  /**
   * TODO
   */
  public void pop() {
    if (!isLoading() && !isResolving()) {
      final int last = digits.size() - 1;
      if (last >= 0) {
        digits.remove(last);
        setDotVisibility(dots[last], false);
        if (last < (DEFAULT_MAX_LENGTH - 1)) {
          if (cursor.getVisibility() == View.INVISIBLE) {
            showCursor();
          }
        }
      }
    }
  }

  /**
   * TODO
   */
  public void resolve(final boolean succeeded) {
    if (isLoading() && !isResolving()) {
      if (Utils.isNotNull(resolveAnimator)) {
        if (resolveAnimator.isRunning()) {
          resolveAnimator.cancel();
        }
        resolveAnimator = null;
      }
      if (succeeded) {
        initializeSuccessResolveAnimator();
        resolveAnimator = successResolveAnimator.clone();
      } else {
        initializeFailureResolveAnimator();
        resolveAnimator = failureResolveAnimator.clone();
      }
      resolveAnimator.addListener(new BaseAnimatorListener() {
        @Override
        public void onAnimationEnd(Animator animator) {
          Timber.d("PIN confirmation finished");
          digits.clear();
          if (Utils.isNotNull(listener)) {
            listener.onConfirmationFinished(succeeded);
          }
          if (!succeeded) {
            showCursor();
          }
        }
      });
      stopLoading(succeeded);
      resolveAnimator.start();
    }
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
    void onConfirmationStarted(@NonNull String pin);

    /**
     * TODO
     *
     * @param succeeded
     *   TODO
     */
    void onConfirmationFinished(boolean succeeded);
  }
}
