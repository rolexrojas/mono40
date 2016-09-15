package com.tpago.movil.ui;

import android.support.annotation.NonNull;
import android.util.Log;

import com.tpago.movil.data.MessageHelper;
import com.tpago.movil.data.net.NetworkManager;
import com.tpago.movil.domain.BalanceManager;
import com.tpago.movil.domain.DataLoader;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class MainPresenter {
  private static final String TAG = MainPresenter.class.getSimpleName();

  private final MainScreen screen;

  private final BalanceManager balanceManager;

  private final NetworkManager networkManager;

  private final DataLoader dataLoader;

  private final MessageHelper messageHelper;

  /**
   * TODO
   */
  private Subscription networkStatusSubscription = Subscriptions.unsubscribed();

  /**
   * TODO
   */
  private Subscription dataLoadSubscription = Subscriptions.unsubscribed();

  /**
   * TODO
   */
  private MainScreen.OnOptionClickedListener goToAccountsListener =
    new MainScreen.OnOptionClickedListener() {
      @Override
      public void onClick() {
        screen.showAccountsScreen();
      }
    };

  /**
   * Indicates whether the splash screen has been shown or not.
   */
  private boolean splashScreenShowed = false;

  /**
   * Indicates whether the data has been loaded or not.
   */
  private boolean dataLoaded = false;

  /**
   * TODO
   *
   * @param screen
   *   TODO
   */
  public MainPresenter(@NonNull MainScreen screen, @NonNull BalanceManager balanceManager,
    @NonNull NetworkManager networkManager, @NonNull DataLoader dataLoader,
    @NonNull MessageHelper messageHelper) {
    this.screen = screen;
    this.balanceManager = balanceManager;
    this.networkManager = networkManager;
    this.dataLoader = dataLoader;
    this.messageHelper = messageHelper;
  }

  /**
   * TODO
   */
  public final void start() {
    balanceManager.start();
    networkStatusSubscription = networkManager.status()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<Boolean>() {
        @Override
        public void call(Boolean available) {
          if (available) {
            if (!dataLoaded) {
              dataLoadSubscription = dataLoader.load()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                  @Override
                  public void call() {
                    if (!splashScreenShowed) {
                      screen.showSplashScreen();
                      splashScreenShowed = true;
                    }
                  }
                })
                .doOnUnsubscribe(new Action0() {
                  @Override
                  public void call() {
                    screen.hideSplashScreen();
                  }
                })
                .subscribe(new Action1<DataLoader.Result>() {
                  @Override
                  public void call(DataLoader.Result result) {
                    if (result == DataLoader.Result.FAILED) {
                      dataLoaded = false;
                    } else {
                      dataLoaded = true;
                      if (result == DataLoader.Result.SUCCESS_WITH_ACCOUNT_REMOVALS ||
                        result == DataLoader.Result.SUCCESS_WITH_ACCOUNT_ADDITIONS_AND_REMOVALS) {
                        screen.showDialog(messageHelper.doneWithExclamationMark(),
                          messageHelper.yourAccountHaveBeenRemoved(), messageHelper.ok(), null,
                          messageHelper.goToAccounts(), goToAccountsListener);
                      }
                      if (result == DataLoader.Result.SUCCESS_WITH_ACCOUNT_ADDITIONS ||
                        result == DataLoader.Result.SUCCESS_WITH_ACCOUNT_ADDITIONS_AND_REMOVALS) {
                        screen.showDialog(messageHelper.doneWithExclamationMark(),
                          messageHelper.yourAccountHaveBeenAdded(), messageHelper.ok(), null,
                          messageHelper.goToAccounts(), goToAccountsListener);
                      }
                    }
                  }
                }, new Action1<Throwable>() {
                  @Override
                  public void call(Throwable throwable) {
                    Log.e(TAG, "Loading data", throwable);
                  }
                });
            }
          } else {
            screen.showMessage(messageHelper.noInternetConnection());
          }
        }
      });
  }

  /**
   * TODO
   */
  public final void stop() {
    dataLoaded = false;
    splashScreenShowed = false;
    if (!dataLoadSubscription.isUnsubscribed()) {
      dataLoadSubscription.unsubscribe();
    }
    if (!networkStatusSubscription.isUnsubscribed()) {
      networkStatusSubscription.unsubscribe();
    }
    balanceManager.stop();
  }
}
