package com.gbh.movil.ui.splash;

import android.support.annotation.NonNull;

import com.gbh.movil.RxUtils;
import com.gbh.movil.domain.InitialDataLoader;
import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.data.net.NetworkHelper;
import com.gbh.movil.ui.Presenter;

import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
final class SplashPresenter extends Presenter<SplashScreen> {
  private final NetworkHelper networkHelper;
  private final SchedulerProvider schedulerProvider;
  private final InitialDataLoader initialDataLoader;

  private Subscription subscription = Subscriptions.unsubscribed();

  SplashPresenter(@NonNull NetworkHelper networkHelper,
    @NonNull SchedulerProvider schedulerProvider, @NonNull InitialDataLoader initialDataLoader) {
    this.networkHelper = networkHelper;
    this.schedulerProvider = schedulerProvider;
    this.initialDataLoader = initialDataLoader;
  }

  /**
   * TODO
   */
  final void start() {
    assertScreen();
    if (networkHelper.isNetworkAvailable()) {
      subscription = initialDataLoader.load()
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.ui())
        .doOnSubscribe(new Action0() {
          @Override
          public void call() {
            Timber.d("Initial load started");
          }
        })
        .doOnUnsubscribe(new Action0() {
          @Override
          public void call() {
            Timber.d("Initial load finished");
          }
        })
        .subscribe(new Action1<Object>() {
          @Override
          public void call(Object notification) {
            Timber.d("Initial load succeeded");
            screen.terminate();
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "Initial load failed");
          }
        });
    } else {
      screen.terminate();
    }
  }

  /**
   * TODO
   */
  final void stop() {
    assertScreen();
    RxUtils.unsubscribe(subscription);
  }
}
