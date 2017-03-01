package com.tpago.movil.widget;

import android.support.v4.view.ViewPager;

import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class AutoTabSwitcher extends ViewPager.SimpleOnPageChangeListener {
  private final ViewPager viewPager;

  private Runnable runnable;
  private boolean isStarted = false;

  public AutoTabSwitcher(ViewPager viewPager) {
    this.viewPager = Preconditions.checkNotNull(viewPager, "viewPager == null");
  }

  private void removeRunnable() {
    if (Objects.isNotNull(runnable)) {
      viewPager.removeCallbacks(runnable);
      runnable = null;
    }
  }

  public final void start() {
    if (!isStarted) {
      isStarted = true;
      viewPager.addOnPageChangeListener(this);
      onPageScrollStateChanged(ViewPager.SCROLL_STATE_IDLE);
    }
  }

  public final void stop() {
    if (isStarted) {
      removeRunnable();
      viewPager.removeOnPageChangeListener(this);
      isStarted = false;
    }
  }

  @Override
  public void onPageScrollStateChanged(int state) {
    if (isStarted) {
      if (state == ViewPager.SCROLL_STATE_IDLE) {
        final int position = viewPager.getCurrentItem();
        final int count = viewPager.getAdapter().getCount();
        runnable = new Runnable(viewPager,  (position + 1) % count);
        viewPager.postDelayed(runnable, 4000L);
      } else {
        removeRunnable();
      }
    }
  }

  @Override
  public void onPageSelected(final int position) {
    Timber.d("onPageSelected(%1$d)", position);
  }

  private static final class Runnable implements java.lang.Runnable {
    private final ViewPager viewPager;
    private final int position;

    Runnable(ViewPager viewPager, int position) {
      this.viewPager = Preconditions.checkNotNull(viewPager, "viewPager == null");
      this.position = position;
    }

    @Override
    public void run() {
      viewPager.setCurrentItem(position);
    }
  }
}
