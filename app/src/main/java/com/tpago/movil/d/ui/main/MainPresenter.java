package com.tpago.movil.d.ui.main;

import android.support.annotation.NonNull;

import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.misc.rx.RxUtils;
import com.tpago.movil.d.domain.BalanceManager;
import com.tpago.movil.d.domain.util.Event;
import com.tpago.movil.d.domain.util.EventBus;
import com.tpago.movil.d.domain.util.EventType;
import com.tpago.movil.d.ui.AppDialog;
import com.tpago.movil.d.ui.Presenter;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * @author hecvasro
 */
@Deprecated
final class MainPresenter extends Presenter<MainScreen> {

  private final StringHelper stringHelper;
  private final EventBus eventBus;
  private final BalanceManager balanceManager;
  private final AppDialog.Creator screenDialogCreator;
  private final PosBridge posBridge;

  private Subscription subscription = Subscriptions.unsubscribed();

  private boolean alreadyAskedForActivation = false;

  MainPresenter(
    StringHelper stringHelper,
    EventBus eventBus,
    BalanceManager balanceManager,
    AppDialog.Creator screenDialogCreator,
    PosBridge posBridge
  ) {
    this.stringHelper = stringHelper;
    this.eventBus = eventBus;
    this.balanceManager = balanceManager;
    this.screenDialogCreator = screenDialogCreator;
    this.posBridge = posBridge;
  }

  final void create() {
    assertScreen();
    balanceManager.start();
  }

  final void start() {
    assertScreen();
    if (posBridge.isAvailable()) {
      if (!alreadyAskedForActivation) {
        subscription = eventBus.onEventDispatched(EventType.PRODUCT_ADDITION)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<Event>() {
            @Override
            public void call(final Event event) {
              alreadyAskedForActivation = true;
              screenDialogCreator.create(stringHelper.dialogProductAdditionTitle())
                .message(stringHelper.dialogProductAdditionMessage())
                .positiveAction(
                  stringHelper.dialogProductAdditionPositiveAction(),
                  new AppDialog.OnActionClickedListener() {
                    @Override
                    public void onActionClicked(@NonNull AppDialog.Action action) {
                      eventBus.release(event);
                      screen.openPurchaseScreen();
                    }
                  }
                )
                .negativeAction(stringHelper.dialogProductAdditionNegativeAction())
                .build()
                .show();
            }
          }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
              Timber.e(throwable, "Listening to creditCard addition events");
            }
          });
      }
    }
  }

  final void stop() {
    assertScreen();
    RxUtils.unsubscribe(subscription);
  }

  final void destroy() {
    assertScreen();
    balanceManager.stop();
  }
}
