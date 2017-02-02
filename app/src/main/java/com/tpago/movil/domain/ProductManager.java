package com.tpago.movil.domain;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.tpago.movil.domain.pos.PosBridge;
import com.tpago.movil.domain.pos.PosResult;
import com.tpago.movil.domain.util.EventBus;
import com.tpago.movil.misc.rx.RxUtils;

import java.util.ArrayList;
import java.util.List;

import dagger.Lazy;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * @author hecvasro
 */
public final class ProductManager implements ProductProvider {
  private final ProductRepo productRepo;
  private final Lazy<PosBridge> posBridge;
  private final EventBus eventBus;
  private final com.tpago.movil.domain.session.SessionManager sessionManager;

  public ProductManager(@NonNull ProductRepo productRepo, @NonNull Lazy<PosBridge> posBridge,
    @NonNull EventBus eventBus,
    @NonNull com.tpago.movil.domain.session.SessionManager sessionManager) {
    this.productRepo = productRepo;
    this.posBridge = posBridge;
    this.eventBus = eventBus;
    this.sessionManager = sessionManager;
  }

  @NonNull
  final Observable<Object> syncProducts(@NonNull List<Product> products) {
    return getAll().zipWith(Observable.just(products),
      new Func2<List<Product>, List<Product>, List<Pair<Action, Product>>>() {
        @Override
        public List<Pair<Action, Product>> call(List<Product> local, List<Product> remote) {
          final List<Pair<Action, Product>> actions = new ArrayList<>();
          for (Product product : remote) {
            if (!local.contains(product)) {
              actions.add(Pair.create(Action.ADD, product));
            } else {
              actions.add(Pair.create(Action.UPDATE, product));
            }
          }
          for (Product product : local) {
            if (!remote.contains(product)) {
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
          final Action action = pair.first;
          if (action == Action.ADD) {
            eventBus.dispatch(new ProductAdditionEvent());
          } else if (action == Action.REMOVE) {
            eventBus.dispatch(new ProductRemovalEvent());
          }
        }
      })
      .flatMap(new Func1<Pair<Action, Product>, Observable<Object>>() {
        @Override
        public Observable<Object> call(Pair<Action, Product> pair) {
          final Action action = pair.first;
          final Product product = pair.second;
          final Observable<Object> observable;
          if (action == Action.ADD || action == Action.UPDATE) {
            observable = productRepo.save(product)
              .cast(Object.class);
          } else {
            observable = Observable
              .concat(productRepo.remove(product), posBridge.get().removeCard(product.getAlias()));
          }
          return observable;
        }
      })
      .last();
  }

  @NonNull
  public final Observable<List<Product>> getAllPaymentOptions() {
    // TODO: Change order in a way that the primary payment option is the first one.
    return getAll()
      .compose(RxUtils.<Product>fromCollection())
      .filter(new Func1<Product, Boolean>() {
        @Override
        public Boolean call(Product product) {
          return Product.isPaymentOption(product);
        }
      })
      .toList();
  }

  public final Observable<Boolean> activateAllProducts(final String pin) {
    return getAllPaymentOptions()
      .compose(RxUtils.<Product>fromCollection())
      .filter(new Func1<Product, Boolean>() {
        @Override
        public Boolean call(Product product) {
          return !posBridge.get().isActive(product.getAlias());
        }
      })
      .concatMap(new Func1<Product, Observable<PosResult<String>>>() {
        @Override
        public Observable<PosResult<String>> call(Product product) {
          return posBridge.get()
            .addCard(sessionManager.getSession().getPhoneNumber(), pin, product.getAlias());
        }
      })
      .map(new Func1<PosResult<String>, Boolean>() {
        @Override
        public Boolean call(PosResult<String> result) {
          return result.isSuccessful();
        }
      })
      .reduce(false, new Func2<Boolean, Boolean, Boolean>() {
        @Override
        public Boolean call(Boolean flag, Boolean result) {
          return flag || result;
        }
      });
  }

  @NonNull
  public final Observable<Product> getDefaultPaymentOption() {
    return getAllPaymentOptions()
      .compose(RxUtils.<Product>fromCollection())
      .filter(new Func1<Product, Boolean>() {
        @Override
        public Boolean call(Product product) {
          return Product.isDefaultPaymentOption(product);
        }
      })
      .switchIfEmpty(Observable.just((Product) null));
  }

  @NonNull
  @Override
  public Observable<List<Product>> getAll() {
    return productRepo.getAll();
  }

  public void clear() {
    productRepo.clear();
  }

  private enum Action {
    ADD,
    UPDATE,
    REMOVE
  }
}
