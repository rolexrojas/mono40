package com.tpago.movil.dep.widget;

import androidx.viewpager.widget.ViewPager;

import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
@Deprecated
public final class AutoTabSwitcher extends ViewPager.SimpleOnPageChangeListener {

  private final ViewPager viewPager;

  private Runnable runnable;
  private boolean isStarted = false;

  public AutoTabSwitcher(ViewPager viewPager) {
    this.viewPager = ObjectHelper.checkNotNull(viewPager, "viewPager");
  }

  private void removeRunnable() {
    if (ObjectHelper.isNotNull(runnable)) {
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
        final int count = viewPager.getAdapter()
          .getCount();
        runnable = new Runnable(viewPager, (position + 1) % count);
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
      this.viewPager = ObjectHelper.checkNotNull(viewPager, "viewPager");
      this.position = position;
    }

    @Override
    public void run() {
      viewPager.setCurrentItem(position);
    }
  }
}
