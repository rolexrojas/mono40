package com.gbh.movil.ui.main;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.BalanceManager;

/**
 * TODO
 *
 * @author hecvasro
 */
final class MainPresenter {
  private final BalanceManager balanceManager;

  MainPresenter(@NonNull BalanceManager balanceManager) {
    this.balanceManager = balanceManager;
  }

  final void start() {
    balanceManager.start();
  }

  final void stop() {
    balanceManager.stop();
  }
}
