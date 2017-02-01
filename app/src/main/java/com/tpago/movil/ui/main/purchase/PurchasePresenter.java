package com.tpago.movil.ui.main.purchase;

import android.support.annotation.NonNull;

import com.tpago.movil.data.StringHelper;
import com.tpago.movil.domain.util.Event;
import com.tpago.movil.domain.util.EventBus;
import com.tpago.movil.domain.util.EventType;
import com.tpago.movil.domain.Product;
import com.tpago.movil.domain.ProductManager;
import com.tpago.movil.misc.Utils;
import com.tpago.movil.misc.rx.RxUtils;
import com.tpago.movil.ui.AppDialog;
import com.tpago.movil.ui.Presenter;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
class PurchasePresenter extends Presenter<PurchaseScreen> {
  private final StringHelper stringHelper;
  private final ProductManager productManager;
  private final EventBus eventBus;
  private final AppDialog.Creator screenDialogCreator;

  private Subscription productAdditionEventSubscription = Subscriptions.unsubscribed();
  private Subscription paymentOptionsSubscription = Subscriptions.unsubscribed();
  private Subscription activationSubscription = Subscriptions.unsubscribed();

  /**
   * TODO
   */
  private Product selectedProduct;

  PurchasePresenter(@NonNull StringHelper stringHelper,
    @NonNull ProductManager productManager,
    @NonNull EventBus eventBus, @NonNull AppDialog.Creator screenDialogCreator) {
    this.stringHelper = stringHelper;
    this.productManager = productManager;
    this.eventBus = eventBus;
    this.screenDialogCreator = screenDialogCreator;
  }

  /**
   * TODO
   */
  void start() {
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
    paymentOptionsSubscription = productManager.getAllPaymentOptions()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnNext(new Action1<List<Product>>() {
        @Override
        public void call(List<Product> products) {
          screen.clearPaymentOptions();
        }
      })
      .compose(RxUtils.<Product>fromCollection())
      .subscribe(new Action1<Product>() {
        @Override
        public void call(Product product) {
          screen.addPaymentOption(product);
          if (Product.isDefaultPaymentOption(product)) {
            selectedProduct = product;
            screen.markAsSelected(selectedProduct);
          }
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable, "Loading all the payment options");
          // TODO: Let the user know that loading all the payment options failed.
        }
      });
  }

  /**
   * TODO
   */
  void stop() {
    assertScreen();
    RxUtils.unsubscribe(paymentOptionsSubscription);
    RxUtils.unsubscribe(productAdditionEventSubscription);
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  Product getSelectedPaymentOption() {
    return selectedProduct;
  }

  /**
   * TODO
   *
   * @param product
   *   TODO
   */
  void onPaymentOptionSelected(@NonNull Product product) {
    assertScreen();
    if (Utils.isNotNull(selectedProduct) && selectedProduct.equals(product)) {
      screen.openPaymentScreen(selectedProduct);
    } else {
      selectedProduct = product;
      screen.markAsSelected(selectedProduct);
    }
  }

  void activeCards(@NonNull String pin) {
    assertScreen();
    RxUtils.unsubscribe(activationSubscription);
    activationSubscription = productManager.activateAllProducts(pin)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<Boolean>() {
        @Override
        public void call(Boolean flag) {
          screen.onActivationFinished(flag);
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable, "Activating all products");
          screen.onActivationFinished(false);
        }
      });
  }
}
