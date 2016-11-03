package com.gbh.movil.ui;

import android.support.annotation.NonNull;

import com.gbh.movil.RxUtils;
import com.gbh.movil.domain.DataLoader;
import com.gbh.movil.domain.DomainCode;
import com.gbh.movil.domain.Result;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
final class SplashPresenter {
  private final SplashScreen screen;
  private final DataLoader dataLoader;

  private Subscription subscription = Subscriptions.unsubscribed();

  SplashPresenter(@NonNull SplashScreen screen, @NonNull DataLoader dataLoader) {
    this.screen = screen;
    this.dataLoader = dataLoader;
  }

  final void start() {
    subscription = dataLoader.load()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<Result<DomainCode, Void>>() {
        @Override
        public void call(Result<DomainCode, Void> result) {
          screen.finish();
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
