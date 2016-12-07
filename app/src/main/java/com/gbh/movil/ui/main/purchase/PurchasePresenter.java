package com.gbh.movil.ui.main.purchase;

import android.support.annotation.NonNull;

import com.gbh.movil.data.StringHelper;
import com.gbh.movil.domain.pos.PosBridge;
import com.gbh.movil.domain.util.Event;
import com.gbh.movil.domain.util.EventBus;
import com.gbh.movil.domain.util.EventType;
import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.domain.Product;
import com.gbh.movil.domain.ProductManager;
import com.gbh.movil.misc.rx.RxUtils;
import com.gbh.movil.ui.AppDialog;
import com.gbh.movil.ui.Presenter;

import java.util.List;

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
class PurchasePresenter extends Presenter<PurchaseScreen> {
  private final StringHelper stringHelper;
  private final SchedulerProvider schedulerProvider;
  private final ProductManager productManager;
  private final EventBus eventBus;
  private final AppDialog.Creator screenDialogCreator;
  private final PosBridge posBridge;

  private Subscription productAdditionEventSubscription = Subscriptions.unsubscribed();
  private Subscription paymentOptionsSubscription = Subscriptions.unsubscribed();
  private Subscription activationSubscription = Subscriptions.unsubscribed();

  /**
   * TODO
   */
  private Product selectedProduct;

  PurchasePresenter(@NonNull StringHelper stringHelper,
    @NonNull SchedulerProvider schedulerProvider, @NonNull ProductManager productManager,
    @NonNull EventBus eventBus, @NonNull AppDialog.Creator screenDialogCreator,
    @NonNull PosBridge posBridge) {
    this.stringHelper = stringHelper;
    this.schedulerProvider = schedulerProvider;
    this.productManager = productManager;
    this.eventBus = eventBus;
    this.screenDialogCreator = screenDialogCreator;
    this.posBridge = posBridge;
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
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.ui())
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
    if (selectedProduct.equals(product)) {
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
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.ui())
      .subscribe(new Action1<Object>() {
        @Override
        public void call(Object object) {
          screen.onActivationFinished(true);
//          if (!posBridge.isDefault()) {
//            screenDialogCreator.create(stringHelper.dialogNfcDefaultAssignationTitle())
//              .message(stringHelper.dialogNfcDefaultAssignationMessage())
//              .positiveAction(stringHelper.dialogNfcDefaultAssignationPositiveAction(),
//                new AppDialog.OnActionClickedListener() {
//                  @Override
//                  public void onActionClicked(@NonNull AppDialog.Action action) {
//                    screen.startActivity(posBridge.requestToMakeDefault());
//                  }
//                })
//              .negativeAction(stringHelper.dialogNfcDefaultAssignationNegativeAction())
//              .build()
//              .show();
//          }
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
