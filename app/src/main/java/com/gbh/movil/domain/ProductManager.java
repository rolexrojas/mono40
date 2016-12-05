package com.gbh.movil.domain;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.gbh.movil.misc.rx.RxUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class ProductManager implements ProductProvider {
  private final ProductRepo productRepo;

  public ProductManager(@NonNull ProductRepo productRepo) {
    this.productRepo = productRepo;
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
  final Observable<List<Product>> syncProducts(@NonNull List<Product> products) {
    return productRepo.getAll()
      .zipWith(Observable.just(products), new Func2<List<Product>, List<Product>,
        List<Pair<Action, Product>>>() {
        @Override
        public List<Pair<Action, Product>> call(List<Product> localProducts,
          List<Product> remoteProducts) {
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
      .toList();
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final Observable<List<Product>> getAllPaymentOptions() {
    return getAll()
      .compose(RxUtils.<Product>fromCollection())
      .filter(new Func1<Product, Boolean>() {
        @Override
        public Boolean call(Product product) {
          return Product.isPaymentOption(product);
        }
      })
      // TODO: Change order in a way that the primary payment option is the first one.
      .toList();
  }

  @NonNull
  @Override
  public Observable<List<Product>> getAll() {
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
