package com.gbh.movil.ui.view.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.gbh.movil.R;

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
  private final List<Integer> digits = new ArrayList<>();

  /**
   * TODO
   */
  private Coordinator coordinator;

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

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    // Binds all the annotated views and methods.
    ButterKnife.bind(this);
    // Initializes the coordinator.
    coordinator = new Coordinator(getContext(), container, DEFAULT_MAX_LENGTH);
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
      coordinator.show();
    }
  }

  /**
   * TODO
   */
  public void pop() {
    final int last = digits.size() - 1;
    if (last >= 0) {
      digits.remove(last);
      coordinator.hide();
    }
  }

  /**
   * TODO
   */
  private static class Dot {
    /**
     * TODO
     */
    private static final long DELAY_VISIBILITY = 75L;

    /**
     * TODO
     */
    protected final View view;

    /**
     * TODO
     *
     * @param view
     *   TODO
     */
    Dot(@NonNull View view) {
      this.view = view;
      this.view.setVisibility(View.GONE);
    }

    /**
     * TODO
     *
     * @param visible
     *   TODO
     */
    private void setVisibility(final boolean visible) {
      view.postDelayed(new Runnable() {
        @Override
        public void run() {
          view.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
      }, DELAY_VISIBILITY);
    }

    /**
     * TODO
     */
    void show() {
      setVisibility(true);
    }

    /**
     * TODO
     */
    void hide() {
      setVisibility(false);
    }
  }

  /**
   * TODO
   */
  private static class Coordinator {
    /**
     * TODO
     */
    private final Dot[] dots;

    /**
     * TODO
     */
    private final View cursor;

    /**
     * TODO
     */
    private Animation cursorAnimation;

    /**
     * TODO
     */
    private int index = 0;

    /**
     * TODO
     *
     * @param context
     *   TODO
     * @param container
     *   TODO
     * @param length
     *   TODO
     */
    Coordinator(@NonNull Context context, @NonNull LinearLayout container, int length) {
      dots = new Dot[length];
      View view;
      final LayoutInflater inflater = LayoutInflater.from(context);
      for (int i = 0; i < length; i++) {
        view = inflater.inflate(R.layout.pin_dot, container, false);
        view.setId(View.generateViewId());
        container.addView(view);
        dots[i] = new Dot(view);
      }
      cursor = inflater.inflate(R.layout.pin_cursor, container, false);
      cursor.setId(View.generateViewId());
      container.addView(cursor);
    }

    /**
     * TODO
     */
    void show() {
      if (index < dots.length) {
        dots[index].show();
        index++;
        if (index == dots.length && cursor.getVisibility() == View.VISIBLE) {
          cursor.setVisibility(View.INVISIBLE);
        }
      }
    }

    /**
     * TODO
     */
    void hide() {
      if (index > 0) {
        dots[index - 1].hide();
        index--;
        if (index < dots.length && cursor.getVisibility() == View.INVISIBLE) {
          cursor.setVisibility(View.VISIBLE);
        }
      }
    }
  }
}
