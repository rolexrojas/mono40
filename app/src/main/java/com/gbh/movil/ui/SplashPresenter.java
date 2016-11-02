package com.gbh.movil.ui;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.gbh.movil.data.RxUtils;
import com.gbh.movil.data.net.NetworkHelper;
import com.gbh.movil.domain.DataLoader;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
final class SplashPresenter {
  private final SplashScreen screen;
  private final NetworkHelper networkHelper;
  private final DataLoader dataLoader;

  private Subscription subscription = Subscriptions.unsubscribed();

  SplashPresenter(@NonNull SplashScreen screen, @NonNull NetworkHelper networkHelper,
    @NonNull DataLoader dataLoader) {
    this.screen = screen;
    this.networkHelper = networkHelper;
    this.dataLoader = dataLoader;
  }

  final void start() {
    subscription = networkHelper.status()
      .flatMap(new Func1<Boolean, Observable<Pair<Integer, Integer>>>() {
        @Override
        public Observable<Pair<Integer, Integer>> call(Boolean available) {
          if (available) {
            return dataLoader.load();
          } else {
            return Observable.just(Pair.create(0, 0));
          }
        }
      })
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<Pair<Integer, Integer>>() {
        @Override
        public void call(Pair<Integer, Integer> pair) {
          screen.finish(pair.first, pair.second);
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable, "Loading initial data");
        }
      });
  }

  final void stop() {
    RxUtils.unsubscribe(subscription);
  }
}
