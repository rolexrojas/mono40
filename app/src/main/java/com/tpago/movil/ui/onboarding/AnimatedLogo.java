package com.tpago.movil.ui.onboarding;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
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

import com.google.auto.value.AutoValue;
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
public final class AnimatedLogo extends FrameLayout {
  private static final long FRAME_DURATION = 2000L;

  private static final long FRAME_DURATION_CROSS = Math.round(FRAME_DURATION * 0.33F);
  private static final long FRAME_DURATION_DELAY = FRAME_DURATION - (FRAME_DURATION_CROSS * 2L);

  private FrameSwitcher frameSwitcher;

  public AnimatedLogo(Context context) {
    super(context);
    initializeAnimatedLogo();
  }

  public AnimatedLogo(Context context, AttributeSet attrs) {
    super(context, attrs);
    initializeAnimatedLogo();
  }

  public AnimatedLogo(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initializeAnimatedLogo();
  }

  private void initializeAnimatedLogo() {
    LayoutInflater.from(getContext()).inflate(R.layout.animated_logo, this);
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
    final Frame coverFrame = Frame.create(context, R.drawable.logo_white);
    final FrameIterator frameIterator = new FrameIterator.Builder()
      .push(Frame.create(context, R.drawable.logo_white_state_0))
      .push(Frame.create(context, R.drawable.logo_white_state_1))
      .push(Frame.create(context, R.drawable.logo_white_state_2))
      .push(Frame.create(context, R.drawable.logo_white_state_3))
      .build();
    frameSwitcher = new FrameSwitcher(imageSwitcher, coverFrame, frameIterator);
  }

  public final void start() {
    frameSwitcher.start();
  }

  public final void stop() {
    frameSwitcher.stop();
  }

  public final void toggle() {
    frameSwitcher.toggle();
  }

  @AutoValue
  static abstract class Frame {
    static Frame create(Drawable drawable) {
      return new AutoValue_AnimatedLogo_Frame(drawable);
    }

    static Frame create(Context context, @DrawableRes int drawableId) {
      if (Objects.isNull(context)) {
        throw new NullPointerException("Null context");
      }
      return create(ContextCompat.getDrawable(context, drawableId));
    }

    abstract Drawable getDrawable();
  }

  private static final class FrameIterator {
    private final Frame[] frames;

    private int current = 0;

    private FrameIterator(Frame[] frames) {
      if (Objects.isNull(frames)) {
        throw new NullPointerException("frames == null");
      }
      if (frames.length < 2) {
        throw new IllegalArgumentException("frames.length < 2");
      }
      this.frames = frames;
    }

    final Frame getCurrent() {
      return frames[current];
    }

    final Frame moveToStart() {
      current = 0;
      return getCurrent();
    }

    final Frame moveToNext() {
      current = (current + 1) % frames.length;
      return getCurrent();
    }

    static final class Builder {
      private final List<Frame> frames;

      Builder() {
        frames = new ArrayList<>();
      }

      final Builder push(Frame frame) {
        if (!frames.contains(frame)) {
          frames.add(frame);
        }
        return this;
      }

      final FrameIterator build() {
        return new FrameIterator(frames.toArray(new Frame[frames.size()]));
      }
    }
  }

  private static final class FrameSwitcher {
    private final Frame cover;
    private final FrameIterator iterator;

    private final ImageSwitcher switcher;

    private Disposable disposable = Disposables.disposed();

    FrameSwitcher(ImageSwitcher switcher, Frame cover, FrameIterator iterator) {
      this.cover = cover;
      this.iterator = iterator;
      this.switcher = switcher;
      this.switcher.setImageDrawable(this.cover.getDrawable());
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
              switcher.setImageDrawable(cover.getDrawable());
            }
          })
          .subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
              switcher.setImageDrawable(iterator.moveToNext().getDrawable());
            }
          });
      }
    }

    final void stop() {
      if (isRunning()) {
        disposable.dispose();
      }
    }

    final void toggle() {
      if (isRunning()) {
        stop();
      } else {
        start();
      }
    }
  }
}
