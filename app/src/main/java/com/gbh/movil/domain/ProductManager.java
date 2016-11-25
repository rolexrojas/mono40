package com.gbh.movil.domain;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.gbh.movil.domain.util.EventBus;
import com.gbh.movil.rx.RxUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class ProductManager implements ProductProvider {
  private final EventBus eventBus;
  private final ProductRepo productRepo;

  public ProductManager(@NonNull EventBus eventBus, @NonNull ProductRepo productRepo) {
    this.eventBus = eventBus;
    this.productRepo = productRepo;
  }

  /**
   * TODO
   *
   * @param products
   *   TODO
   * @param mustEmitAdditionAndRemovalNotifications
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  private Observable<Set<Product>> syncAccounts(@NonNull Set<Product> products,
    final boolean mustEmitAdditionAndRemovalNotifications) {
    return productRepo.getAll()
      .zipWith(Observable.just(products), new Func2<Set<Product>, Set<Product>,
        List<Pair<Action, Product>>>() {
        @Override
        public List<Pair<Action, Product>> call(Set<Product> localProducts,
          Set<Product> remoteProducts) {
          final List<Pair<Action, Product>> actions = new ArrayList<>();
          for (Product product : remoteProducts) {
            if (!localProducts.contains(product)) {
              actions.add(Pair.create(Action.ADD, product));
            } else {
              actions.add(Pair.create(Action.UPDATE, product));
            }
          }
          for (Product product : localProducts) {
            if (!remoteProducts.contains(product)) {
              actions.add(Pair.create(Action.REMOVE, product));
            }
          }
          return actions;
        }
      })
      .compose(RxUtils.<Pair<Action, Product>>fromCollection())
      .doOnNext(new Action1<Pair<Action, Product>>() {
        @Override
        public void call(Pair<Action, Product> pair) {
          if (mustEmitAdditionAndRemovalNotifications) {
            final Action action = pair.first;
            final Product product = pair.second;
            if (action == Action.ADD) {
              eventBus.dispatch(new ProductAdditionEvent());
            } else if (action == Action.REMOVE) {
              eventBus.dispatch(new ProductRemovalEvent());
            }
            Timber.d("%1$s %2$s", product, action);
          }
        }
      })
      .flatMap(new Func1<Pair<Action, Product>, Observable<Product>>() {
        @Override
        public Observable<Product> call(Pair<Action, Product> pair) {
          final Action action = pair.first;
          final Product product = pair.second;
          final Observable<Product> observable;
          if (action == Action.ADD || action == Action.UPDATE) {
            observable = productRepo.save(product);
          } else {
            observable = productRepo.remove(product);
          }
          return observable;
        }
      })
      .compose(RxUtils.<Product>toSet());
  }

  /**
   * TODO
   *
   * @param products
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  final Observable<Set<Product>> syncAccounts(@NonNull Set<Product> products) {
    return syncAccounts(products, true);
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final Observable<Set<Product>> getAllPaymentOptions() {
    return getAll()
      .compose(RxUtils.<Product>fromCollection())
      .filter(new Func1<Product, Boolean>() {
        @Override
        public Boolean call(Product product) {
          return Product.checkPaymentOption(product);
        }
      })
      .compose(RxUtils.<Product>toSet());
  }

  @NonNull
  @Override
  public Observable<Set<Product>> getAll() {
    return productRepo.getAll();
  }

  /**
   * TODO
   */
  private enum Action {
    ADD,
    UPDATE,
    REMOVE
  }
}
