package com.tpago.movil.widget;

import android.support.v4.view.ViewPager;

import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
@Deprecated
public final class AutoTabSwitcher extends ViewPager.SimpleOnPageChangeListener {
  private final ViewPager viewPager;

  private Runnable runnable;
  private boolean isStarted = false;

  public AutoTabSwitcher(ViewPager viewPager) {
    this.viewPager = Preconditions.assertNotNull(viewPager, "viewPager == null");
  }

  private void removeRunnable() {
    if (Objects.checkIfNotNull(runnable)) {
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

  private static final class Runnable implements java.lang.Runnable {
    private final ViewPager viewPager;
    private final int position;

    Runnable(ViewPager viewPager, int position) {
      this.viewPager = Preconditions.assertNotNull(viewPager, "viewPager == null");
      this.position = position;
    }

    @Override
    public void run() {
      viewPager.setCurrentItem(position);
    }
  }
}
