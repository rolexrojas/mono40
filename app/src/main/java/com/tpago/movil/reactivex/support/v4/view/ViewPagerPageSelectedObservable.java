package com.tpago.movil.reactivex.support.v4.view;

import android.support.v4.view.ViewPager;

import com.tpago.movil.util.Preconditions;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

/**
 * @author hecvasro
 */
final class ViewPagerPageSelectedObservable extends Observable<Integer> {
  private final ViewPager viewPager;

  ViewPagerPageSelectedObservable(ViewPager viewPager) {
    this.viewPager = Preconditions.checkNotNull(viewPager, "viewPager == null");
  }

  @Override
  protected void subscribeActual(Observer<? super Integer> observer) {
    final Listener listener = new Listener(viewPager, observer);
    observer.onSubscribe(listener);
    viewPager.addOnPageChangeListener(listener);
    observer.onNext(viewPager.getCurrentItem());
  }

  private static final class Listener extends MainThreadDisposable implements ViewPager.OnPageChangeListener {
    private final ViewPager viewPager;
    private final Observer<? super Integer> observer;

    Listener(ViewPager viewPager, Observer<? super Integer> observer) {
      this.viewPager = Preconditions.checkNotNull(viewPager, "viewPager == null");
      this.observer = Preconditions.checkNotNull(observer, "observer == null");
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
      if (!isDisposed()) {
        observer.onNext(position);
      }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    protected void onDispose() {
      viewPager.removeOnPageChangeListener(this);
    }
  }
}
