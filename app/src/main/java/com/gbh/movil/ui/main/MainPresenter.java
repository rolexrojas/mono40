package com.gbh.movil.ui.main;

import android.support.annotation.NonNull;

import com.gbh.movil.data.StringHelper;
import com.gbh.movil.misc.rx.RxUtils;
import com.gbh.movil.domain.BalanceManager;
import com.gbh.movil.domain.util.Event;
import com.gbh.movil.domain.util.EventBus;
import com.gbh.movil.domain.util.EventType;
import com.gbh.movil.ui.Presenter;
import com.gbh.movil.ui.ScreenDialog;

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
  private final StringHelper stringHelper;
  private final EventBus eventBus;
  private final BalanceManager balanceManager;
  private final ScreenDialog.Creator screenDialogCreator;

  private Subscription subscription = Subscriptions.unsubscribed();

  MainPresenter(@NonNull StringHelper stringHelper, @NonNull EventBus eventBus,
    @NonNull BalanceManager balanceManager, @NonNull ScreenDialog.Creator screenDialogCreator) {
    this.stringHelper = stringHelper;
    this.eventBus = eventBus;
    this.balanceManager = balanceManager;
    this.screenDialogCreator = screenDialogCreator;
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
    subscription = eventBus.onEventDispatched(EventType.PRODUCT_ADDITION)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<Event>() {
        @Override
        public void call(final Event event) {
          if (event.getType().equals(EventType.PRODUCT_ADDITION)) {
            screenDialogCreator.create(stringHelper.dialogProductAdditionTitle())
              .message(stringHelper.dialogProductAdditionMessage())
              .positiveAction(stringHelper.dialogProductAdditionPositiveAction(),
                new ScreenDialog.OnActionClickedListener() {
                  @Override
                  public void onActionClicked(@NonNull ScreenDialog.Action action) {
                    eventBus.release(event);
                    // TODO
                  }
                })
              .negativeAction(stringHelper.dialogProductAdditionNegativeAction())
              .build()
              .show();
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
