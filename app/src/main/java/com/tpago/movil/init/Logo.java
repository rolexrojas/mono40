package com.tpago.movil.init;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.tpago.movil.R;
import com.tpago.movil.util.Objects;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hecvasro
 */
public final class Logo extends FrameLayout {
  private static final long FRAME_DURATION = 2000L;

  private static final long FRAME_DURATION_CROSS = Math.round(FRAME_DURATION * 0.33F);
  private static final long FRAME_DURATION_DELAY = FRAME_DURATION - (FRAME_DURATION_CROSS * 2L);

  private DrawableSwitcher drawableSwitcher;

  public Logo(Context context) {
    super(context);
    initializeAnimatedLogo();
  }

  public Logo(Context context, AttributeSet attrs) {
    super(context, attrs);
    initializeAnimatedLogo();
  }

  public Logo(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initializeAnimatedLogo();
  }

  private void initializeAnimatedLogo() {
    LayoutInflater.from(getContext()).inflate(R.layout.d_widget_logo, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    final Context context = getContext();
    // Initializes the image switcher.
    final ImageSwitcher imageSwitcher = ButterKnife.findById(this, R.id.image_switcher);
    imageSwitcher.setAnimateFirstView(false);
    imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
      @Override
      public View makeView() {
        return new ImageView(context);
      }
    });
    final Animation inAnimation = new AlphaAnimation(0.0F, 1.0F);
    inAnimation.setDuration(FRAME_DURATION_CROSS);
    imageSwitcher.setInAnimation(inAnimation);
    final Animation outAnimation = new AlphaAnimation(1.0F, 0.0F);
    outAnimation.setDuration(FRAME_DURATION_CROSS);
    imageSwitcher.setOutAnimation(outAnimation);
    // Initializes the frame switcher.
    final DrawableIdIterator drawableIterator = new DrawableIdIterator.Builder()
      .push(R.drawable.logo_state_0)
      .push(R.drawable.logo_state_1)
      .push(R.drawable.logo_state_2)
      .push(R.drawable.logo_state_3)
      .build();
    drawableSwitcher = new DrawableSwitcher(imageSwitcher, R.drawable.logo, drawableIterator);
  }

  public final void start() {
    drawableSwitcher.start();
  }

  public final void stop() {
    drawableSwitcher.stop();
  }

  private static final class DrawableIdIterator {
    private final Integer[] drawableIdArray;

    private int current = 0;

    private DrawableIdIterator(Integer[] drawableIdArray) {
      if (Objects.checkIfNull(drawableIdArray)) {
        throw new NullPointerException("drawable == null");
      }
      if (drawableIdArray.length < 2) {
        throw new IllegalArgumentException("drawable.length < 2");
      }
      this.drawableIdArray = drawableIdArray;
    }

    final int getCurrent() {
      return drawableIdArray[current];
    }

    final int moveToStart() {
      current = 0;
      return getCurrent();
    }

    final int moveToNext() {
      current = (current + 1) % drawableIdArray.length;
      return getCurrent();
    }

    static final class Builder {
      private final List<Integer> drawableIdList;

      Builder() {
        drawableIdList = new ArrayList<>();
      }

      final Builder push(int drawableId) {
        if (!drawableIdList.contains(drawableId)) {
          drawableIdList.add(drawableId);
        }
        return this;
      }

      final DrawableIdIterator build() {
        return new DrawableIdIterator(drawableIdList.toArray(new Integer[drawableIdList.size()]));
      }
    }
  }

  private static final class DrawableSwitcher {
    private final int coverId;
    private final DrawableIdIterator iterator;

    private final ImageSwitcher switcher;

    private Disposable disposable = Disposables.disposed();

    DrawableSwitcher(ImageSwitcher switcher, int coverId, DrawableIdIterator iterator) {
      this.coverId = coverId;
      this.iterator = iterator;
      this.switcher = switcher;
      this.switcher.setImageResource(this.coverId);
    }

    final boolean isRunning() {
      return !disposable.isDisposed();
    }

    final void start() {
      if (!isRunning()) {
        disposable = Observable
          .interval(FRAME_DURATION_DELAY, FRAME_DURATION_DELAY, TimeUnit.MILLISECONDS)
          .observeOn(AndroidSchedulers.mainThread())
          .doOnDispose(new Action() {
            @Override
            public void run() throws Exception {
              iterator.moveToStart();
              switcher.setImageResource(coverId);
            }
          })
          .subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
              switcher.setImageResource(iterator.moveToNext());
            }
          });
      }
    }

    final void stop() {
      if (isRunning()) {
        disposable.dispose();
      }
    }
  }
}
