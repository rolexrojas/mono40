package com.tpago.movil.dep.reactivex.support.v4.view;

import androidx.viewpager.widget.ViewPager;

import io.reactivex.Observable;

/**
 * @author hecvasro
 */
@Deprecated
public final class RxViewPager {
  public static Observable<Integer> pageSelections(ViewPager viewPager) {
    return new ViewPagerPageSelectedObservable(viewPager);
  }

  private RxViewPager() {
    throw new AssertionError("Cannot be instantiated");
  }
}
