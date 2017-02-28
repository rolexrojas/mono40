package com.tpago.movil.dep.ui.main.purchase;

import android.support.annotation.NonNull;

import com.tpago.movil.dep.data.StringHelper;
import com.tpago.movil.dep.domain.pos.PosBridge;
import com.tpago.movil.dep.domain.util.Event;
import com.tpago.movil.dep.domain.util.EventBus;
import com.tpago.movil.dep.domain.util.EventType;
import com.tpago.movil.dep.domain.Product;
import com.tpago.movil.dep.domain.ProductManager;
import com.tpago.movil.dep.misc.Utils;
import com.tpago.movil.dep.misc.rx.RxUtils;
import com.tpago.movil.dep.ui.AppDialog;
import com.tpago.movil.dep.ui.Presenter;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * @author hecvasro
 */
class PurchasePresenter extends Presenter<PurchaseScreen> {
  private final StringHelper stringHelper;
  private final ProductManager productManager;
  private final EventBus eventBus;
  private final AppDialog.Creator screenDialogCreator;
  private final PosBridge posBridge;

  private Subscription productAdditionEventSubscription = Subscriptions.unsubscribed();
  private Subscription paymentOptionsSubscription = Subscriptions.unsubscribed();
  private Subscription activationSubscription = Subscriptions.unsubscribed();

  private Product selectedProduct;

  PurchasePresenter(
    StringHelper stringHelper,
    ProductManager productManager,
    EventBus eventBus,
    AppDialog.Creator screenDialogCreator,
    PosBridge posBridge) {
    this.stringHelper = stringHelper;
    this.productManager = productManager;
    this.eventBus = eventBus;
    this.screenDialogCreator = screenDialogCreator;
    this.posBridge = posBridge;
  }

  private void loadPaymentOptions() {
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
      .filter(new Func1<Product, Boolean>() {
        @Override
        public Boolean call(Product product) {
          return posBridge.isRegistered(product.getAlias());
        }
      })
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
    loadPaymentOptions();
  }

  void stop() {
    assertScreen();
    RxUtils.unsubscribe(paymentOptionsSubscription);
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

  void activateCards(@NonNull String pin) {
    assertScreen();
    if (activationSubscription.isUnsubscribed()) {
      activationSubscription = productManager.activateAllProducts(pin)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Boolean>() {
          @Override
          public void call(Boolean flag) {
            screen.onActivationFinished(flag);
            if (flag) {
              loadPaymentOptions();
            }
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "Activating all payment options");
            screen.onActivationFinished(false);
          }
        });
    }
  }
}
