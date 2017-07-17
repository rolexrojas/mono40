package com.tpago.movil.reactivex.support.v4.view;

import android.support.v4.view.ViewPager;

import io.reactivex.Observable;

/**
 * @author hecvasro
 */
public final class RxViewPager {
  public static Observable<Integer> pageSelections(ViewPager viewPager) {
    return new ViewPagerPageSelectedObservable(viewPager);
  }

  private RxViewPager() {
    throw new AssertionError("Cannot be instantiated");
  }
}
