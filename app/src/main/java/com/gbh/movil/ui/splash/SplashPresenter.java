package com.gbh.movil.ui.splash;

import android.support.annotation.NonNull;

import com.gbh.movil.RxUtils;
import com.gbh.movil.domain.NetworkHelper;
import com.gbh.movil.ui.Presenter;

import rx.Subscription;
import rx.subscriptions.Subscriptions;

/**
 * TODO
 *
 * @author hecvasro
 */
final class SplashPresenter extends Presenter<SplashScreen> {
  private final NetworkHelper networkHelper;

  private Subscription subscription = Subscriptions.unsubscribed();

  SplashPresenter(@NonNull NetworkHelper networkHelper) {
    this.networkHelper = networkHelper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void start() {
    super.start();
    if (networkHelper.isNetworkAvailable()) {
      // TODO: Load initial data.
    } else {
      screen.terminate();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void stop() {
    super.stop();
    RxUtils.unsubscribe(subscription);
  }
}
