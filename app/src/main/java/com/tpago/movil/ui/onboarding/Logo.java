package com.tpago.movil.ui.onboarding;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
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
    LayoutInflater.from(getContext()).inflate(R.layout.widget_logo, this);
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
    final DrawableIterator drawableIterator = new DrawableIterator.Builder()
      .push(ContextCompat.getDrawable(context, R.drawable.logo_white_state_0))
      .push(ContextCompat.getDrawable(context, R.drawable.logo_white_state_1))
      .push(ContextCompat.getDrawable(context, R.drawable.logo_white_state_2))
      .push(ContextCompat.getDrawable(context, R.drawable.logo_white_state_3))
      .build();
    drawableSwitcher = new DrawableSwitcher(
      imageSwitcher,
      ContextCompat.getDrawable(context, R.drawable.logo_white),
      drawableIterator);
  }

  public final void start() {
    drawableSwitcher.start();
  }

  public final void stop() {
    drawableSwitcher.stop();
  }

  private static final class DrawableIterator {
    private final Drawable[] drawables;

    private int current = 0;

    private DrawableIterator(Drawable[] drawables) {
      if (Objects.isNull(drawables)) {
        throw new NullPointerException("drawable == null");
      }
      if (drawables.length < 2) {
        throw new IllegalArgumentException("drawable.length < 2");
      }
      this.drawables = drawables;
    }

    final Drawable getCurrent() {
      return drawables[current];
    }

    final Drawable moveToStart() {
      current = 0;
      return getCurrent();
    }

    final Drawable moveToNext() {
      current = (current + 1) % drawables.length;
      return getCurrent();
    }

    static final class Builder {
      private final List<Drawable> drawables;

      Builder() {
        drawables = new ArrayList<>();
      }

      final Builder push(Drawable drawable) {
        if (!drawables.contains(drawable)) {
          drawables.add(drawable);
        }
        return this;
      }

      final DrawableIterator build() {
        return new DrawableIterator(drawables.toArray(new Drawable[drawables.size()]));
      }
    }
  }

  private static final class DrawableSwitcher {
    private final Drawable cover;
    private final DrawableIterator iterator;

    private final ImageSwitcher switcher;

    private Disposable disposable = Disposables.disposed();

    DrawableSwitcher(ImageSwitcher switcher, Drawable cover, DrawableIterator iterator) {
      this.cover = cover;
      this.iterator = iterator;
      this.switcher = switcher;
      this.switcher.setImageDrawable(this.cover);
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
              switcher.setImageDrawable(cover);
            }
          })
          .subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
              switcher.setImageDrawable(iterator.moveToNext());
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
