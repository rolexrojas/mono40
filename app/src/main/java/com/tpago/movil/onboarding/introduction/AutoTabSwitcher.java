package com.tpago.movil.onboarding.introduction;

import android.support.v4.view.ViewPager;

import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
final class AutoTabSwitcher extends ViewPager.SimpleOnPageChangeListener {
  private final ViewPager viewPager;

  private Runnable runnable;

  AutoTabSwitcher(ViewPager viewPager) {
    this.viewPager = Preconditions.checkNotNull(viewPager, "viewPager == null");
  }

  private void removeRunnable() {
    if (Objects.isNotNull(runnable)) {
      viewPager.removeCallbacks(runnable);
      runnable = null;
    }
  }

  final void start() {
    viewPager.addOnPageChangeListener(this);
    onPageSelected(viewPager.getCurrentItem());
  }

  final void stop() {
    removeRunnable();
    viewPager.removeOnPageChangeListener(this);
  }

  @Override
  public void onPageSelected(final int position) {
    removeRunnable();
    final int count = viewPager.getAdapter().getCount();
    viewPager.postDelayed(new Runnable(viewPager, (position + 1) % count), 4000L);
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
