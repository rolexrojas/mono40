package com.gbh.movil.ui.main.accounts.transactions;

import android.support.annotation.NonNull;

import com.gbh.movil.RxUtils;
import com.gbh.movil.domain.DataLoader;


import rx.Subscription;
import rx.subscriptions.Subscriptions;

/**
 * TODO
 * @author hecvasro
 */
class RecentTransactionsPresenter {
  private final RecentTransactionsScreen screen;
  private final DataLoader dataLoader;

  private Subscription subscription = Subscriptions.unsubscribed();

  RecentTransactionsPresenter(@NonNull RecentTransactionsScreen screen,
    @NonNull DataLoader dataLoader) {
    this.screen = screen;
    this.dataLoader = dataLoader;
  }

  void start() {
    // TODO
  }

  void stop() {
    RxUtils.unsubscribe(subscription);
  }
}
