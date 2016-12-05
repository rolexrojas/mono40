package com.gbh.movil.ui.main.purchase;

import android.support.annotation.NonNull;

import com.gbh.movil.misc.Utils;
import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.domain.Product;
import com.gbh.movil.domain.ProductManager;
import com.gbh.movil.misc.rx.RxUtils;
import com.gbh.movil.ui.Presenter;

import java.util.List;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
class PurchasePresenter extends Presenter<PurchaseScreen> {
  /**
   * TODO
   */
  private Subscription subscription = Subscriptions.unsubscribed();

  private final SchedulerProvider schedulerProvider;
  private final ProductManager productManager;

  /**
   * TODO
   */
  private Product selectedProduct;

  PurchasePresenter(@NonNull SchedulerProvider schedulerProvider,
    @NonNull ProductManager productManager) {
    this.schedulerProvider = schedulerProvider;
    this.productManager = productManager;
  }

  /**
   * TODO
   */
  void start() {
    assertScreen();
    subscription = productManager.getAllPaymentOptions()
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
          if (Utils.isNull(selectedProduct) && Product.isDefaultPaymentOption(product)) {
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
    RxUtils.unsubscribe(subscription);
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
}
