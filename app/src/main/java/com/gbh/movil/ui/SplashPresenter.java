package com.gbh.movil.ui;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.gbh.movil.RxUtils;
import com.gbh.movil.Utils;
import com.gbh.movil.domain.DataLoader;
import com.gbh.movil.domain.DomainCode;
import com.gbh.movil.domain.DomainUtils;
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
      .subscribe(new Action1<Result<DomainCode, Pair<Boolean, Boolean>>>() {
        @Override
        public void call(Result<DomainCode, Pair<Boolean, Boolean>> result) {
          boolean additions = false;
          boolean removals = false;
          if (DomainUtils.isSuccessful(result)) {
            final Pair<Boolean, Boolean> pair = result.getData();
            if (Utils.isNotNull(pair)) {
              additions = pair.first;
              removals = pair.second;
            }
          } else {
            // TODO: Let the user know that the initial load failed.
          }
          screen.finish(additions, removals);
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
