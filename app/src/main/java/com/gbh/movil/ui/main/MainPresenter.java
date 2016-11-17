package com.gbh.movil.ui.main;

import android.support.annotation.NonNull;

import com.gbh.movil.rx.RxUtils;
import com.gbh.movil.Utils;
import com.gbh.movil.data.MessageHelper;
import com.gbh.movil.domain.BalanceManager;
import com.gbh.movil.domain.util.Event;
import com.gbh.movil.domain.util.EventBus;
import com.gbh.movil.domain.util.EventType;
import com.gbh.movil.ui.Presenter;

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
final class MainPresenter extends Presenter<MainScreen> {
  private final MessageHelper messageHelper;
  private final EventBus eventBus;
  private final BalanceManager balanceManager;

  private Subscription subscription = Subscriptions.unsubscribed();

  MainPresenter(@NonNull MessageHelper messageHelper, @NonNull EventBus eventBus,
    @NonNull BalanceManager balanceManager) {
    this.messageHelper = messageHelper;
    this.eventBus = eventBus;
    this.balanceManager = balanceManager;
  }

  /**
   * TODO
   */
  final void create() {
    assertScreen();
    balanceManager.start();
  }

  /**
   * TODO
   */
  final void start() {
    assertScreen();
    subscription = eventBus.onEventDispatched(EventType.PRODUCT_ADDITION, EventType.PRODUCT_REMOVAL)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<Event>() {
        @Override
        public void call(Event event) {
          String message = null;
          if (event.getType().equals(EventType.PRODUCT_ADDITION)) {
            message = messageHelper.yourAccountHaveBeenAdded();
          } else if (event.getType().equals(EventType.PRODUCT_REMOVAL)) {
            message = messageHelper.yourAccountHaveBeenRemoved();
          }
          eventBus.release(event);
          if (Utils.isNotNull(message)) {
            screen.showAccountAdditionOrRemovalNotification(message);
          }
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable, "Listening to account addition and removal events");
        }
      });
  }

  /**
   * TODO
   */
  final void stop() {
    assertScreen();
    RxUtils.unsubscribe(subscription);
  }

  /**
   * TODO
   */
  final void destroy() {
    assertScreen();
    balanceManager.stop();
  }
}
