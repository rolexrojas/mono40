package com.tpago.movil.d.ui.main.purchase;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.tpago.movil.User;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.domain.pos.PosResult;
import com.tpago.movil.d.domain.util.Event;
import com.tpago.movil.d.domain.util.EventBus;
import com.tpago.movil.d.domain.util.EventType;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.misc.Utils;
import com.tpago.movil.d.misc.rx.RxUtils;
import com.tpago.movil.d.ui.AppDialog;
import com.tpago.movil.d.ui.Presenter;
import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

import java.util.List;
import java.util.concurrent.Callable;

import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * @author hecvasro
 */
@Deprecated
final class PurchasePresenter extends Presenter<PurchaseScreen> {
  private final StringHelper stringHelper;
  private final ProductManager productManager;
  private final EventBus eventBus;
  private final AppDialog.Creator screenDialogCreator;
  private final PosBridge posBridge;

  private final User user;

  private Subscription productAdditionEventSubscription = Subscriptions.unsubscribed();
  private Subscription activationSubscription = Subscriptions.unsubscribed();

  private Product selectedProduct;

  PurchasePresenter(
    StringHelper stringHelper,
    ProductManager productManager,
    EventBus eventBus,
    AppDialog.Creator screenDialogCreator,
    PosBridge posBridge,
    User user) {
    this.stringHelper = stringHelper;
    this.productManager = productManager;
    this.eventBus = eventBus;
    this.screenDialogCreator = screenDialogCreator;
    this.posBridge = posBridge;

    this.user = Preconditions.checkNotNull(user, "user == null");
  }

  final void start() {
    assertScreen();
    productAdditionEventSubscription = eventBus.onEventDispatched(EventType.PRODUCT_ADDITION)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<Event>() {
        @Override
        public void call(final Event event) {
          screenDialogCreator.create(stringHelper.dialogProductAdditionTitle())
            .message(stringHelper.dialogProductAdditionMessage())
            .positiveAction(stringHelper.dialogProductAdditionPositiveAction(),
              new AppDialog.OnActionClickedListener() {
                @Override
                public void onActionClicked(@NonNull AppDialog.Action action) {
                  eventBus.release(event);
                  screen.requestPin();
                }
              })
            .negativeAction(stringHelper.dialogProductAdditionNegativeAction())
            .build()
            .show();
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable, "Listening to product addition events");
        }
      });
  }

  final void resume() {
    screen.clearPaymentOptions();
    for (Product paymentOption : productManager.getPaymentOptionList()) {
      if (posBridge.isRegistered(paymentOption.getSanitizedNumber())) {
        screen.addPaymentOption(paymentOption);
      }
    }
    selectedProduct = productManager.getDefaultPaymentOption();
    if (Objects.isNotNull(selectedProduct)) {
      screen.markAsSelected(selectedProduct);
    }
  }

  final void stop() {
    assertScreen();
    RxUtils.unsubscribe(productAdditionEventSubscription);
  }

  @NonNull
  Product getSelectedPaymentOption() {
    return selectedProduct;
  }

  void onPaymentOptionSelected(@NonNull Product product) {
    assertScreen();
    if (Utils.isNotNull(selectedProduct) && selectedProduct.equals(product)) {
      screen.openPaymentScreen(selectedProduct);
    } else {
      selectedProduct = product;
      screen.markAsSelected(selectedProduct);
    }
  }

  final void activateCards(final String pin) {
    assertScreen();
    if (activationSubscription.isUnsubscribed()) {
      activationSubscription = Single.defer(new Callable<Single<List<Pair<Product, PosResult>>>>() {
        @Override
        public Single<List<Pair<Product, PosResult>>> call() throws Exception {
          final List<Pair<Product, PosResult>> resultList = productManager
            .registerPaymentOptionList(user.getPhoneNumber().getValue(), pin);
          return Single.just(resultList);
        }
      })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<List<Pair<Product, PosResult>>>() {
          @Override
          public void call(List<Pair<Product, PosResult>> resultList) {
            boolean flag = false;
            final StringBuilder builder = new StringBuilder();
            for (Pair<Product, PosResult> result : resultList) {
              flag |= result.second.isSuccessful();
              builder.append(result.second.getData());
              builder.append("\n");
            }
            final String resultMessage = builder.toString();
            screen.onActivationFinished(flag);
            if (flag) {
              resume();
            } else {
              screen.showGenericErrorDialog(resultMessage);
            }
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable);
            screen.onActivationFinished(false);
            screen.showGenericErrorDialog();
          }
        });
    }
  }
}
