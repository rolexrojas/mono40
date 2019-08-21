package com.tpago.movil.dep.reactivex.support.v4.view;

import androidx.viewpager.widget.ViewPager;

import com.tpago.movil.util.ObjectHelper;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

/**
 * @author hecvasro
 */
@Deprecated
final class ViewPagerPageSelectedObservable extends Observable<Integer> {

  private final ViewPager viewPager;

  ViewPagerPageSelectedObservable(ViewPager viewPager) {
    this.viewPager = ObjectHelper.checkNotNull(viewPager, "viewPager");
  }

  @Override
  protected void subscribeActual(Observer<? super Integer> observer) {
    final Listener listener = new Listener(viewPager, observer);
    observer.onSubscribe(listener);
    viewPager.addOnPageChangeListener(listener);
    observer.onNext(viewPager.getCurrentItem());
  }

  private static final class Listener extends MainThreadDisposable
    implements ViewPager.OnPageChangeListener {

    private final ViewPager viewPager;
    private final Observer<? super Integer> observer;

    Listener(ViewPager viewPager, Observer<? super Integer> observer) {
      this.viewPager = ObjectHelper.checkNotNull(viewPager, "viewPager");
      this.observer = ObjectHelper.checkNotNull(observer, "observer");
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
