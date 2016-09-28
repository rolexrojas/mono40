package com.gbh.movil.ui.view.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.gbh.movil.R;
import com.gbh.movil.ui.view.BaseAnimatorListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
  private AnimatorSet loadingAnimatorSet;

  /**
   * TODO
   */
  @BindView(R.id.container)
  LinearLayout container;

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
      view = inflater.inflate(R.layout.pin_dot, container, false);
      view.setId(View.generateViewId());
      container.addView(view);
      dots[i] = view;
    }
    // Adds the cursor to the container.
    cursor = inflater.inflate(R.layout.pin_cursor, container, false);
    cursor.setId(View.generateViewId());
    container.addView(cursor);
  }

  /**
   * TODO
   *
   * @param digit
   *   TODO
   */
  public void push(int digit) {
    if (digit >= 0 && digit <= 9 && digits.size() < DEFAULT_MAX_LENGTH) {
      digits.add(digit);
      final int size = digits.size();
      setDotVisibility(dots[size - 1], true);
      if (size == DEFAULT_MAX_LENGTH) {
        if (cursor.getVisibility() == View.VISIBLE) {
          cursor.setVisibility(View.INVISIBLE);
        }
        if (loadingAnimatorSet == null) {
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
          final AnimatorSet fadeOutAnimatorSet = new AnimatorSet();
          for (int i = 0; i < DEFAULT_MAX_LENGTH; i++) {
            if (i == 0) {
              fadeOutAnimatorSet.play(animators.get(i).second);
            } else {
              fadeOutAnimatorSet.play(animators.get(i).second).after(animators.get(i - 1).second);
            }
          }
          final AnimatorSet fadeInFadeOutAnimatorSet = new AnimatorSet();
          for (int i = 0; i < DEFAULT_MAX_LENGTH; i++) {
            if (i == 0) {
              fadeInFadeOutAnimatorSet.play(animators.get(i).first);
            } else {
              fadeInFadeOutAnimatorSet.play(animators.get(i).first)
                .after(animators.get(i - 1).first);
              fadeInFadeOutAnimatorSet.play(animators.get(i - 1).second)
                .after(animators.get(i).first);
              if (i == (DEFAULT_MAX_LENGTH - 1)) {
                fadeInFadeOutAnimatorSet.play(animators.get(i).second)
                  .after(animators.get(i - 1).second);
              }
            }
          }
          fadeInFadeOutAnimatorSet.addListener(new BaseAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
              fadeInFadeOutAnimatorSet.start();
            }
          });
          loadingAnimatorSet = new AnimatorSet();
          loadingAnimatorSet.play(fadeOutAnimatorSet).before(fadeInFadeOutAnimatorSet);
          loadingAnimatorSet.start();
        }
      }
    }
  }

  /**
   * TODO
   */
  public void pop() {
    if (loadingAnimatorSet == null) {
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
}
