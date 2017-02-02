package com.tpago.movil.domain;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.tpago.movil.domain.pos.PosBridge;
import com.tpago.movil.domain.pos.PosResult;
import com.tpago.movil.domain.session.Session;
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
            observable = productRepo.save(product).cast(Object.class);
          } else {
            observable = Observable.concat(productRepo.remove(product),
              posBridge.get().removeCard(product.getAlias()));
          }
          return observable;
        }
      })
      .last();
  }

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
  public final Observable<Boolean> activateAllProducts(@NonNull final String pin) {
    return getAllPaymentOptions()
      .flatMap(new Func1<List<Product>, Observable<Boolean>>() {
        @Override
        public Observable<Boolean> call(final List<Product> products) {
          final Session session = sessionManager.getSession();
          if (session == null) {
            return Observable.error(new NullPointerException("session == null"));
          } else {
            final PosBridge bridge = posBridge.get();
            final String phoneNumber = session.getPhoneNumber();
            final Func1<PosResult<String>, Boolean> mapFunc1 = new Func1<PosResult<String>, Boolean>() {
              @Override
              public Boolean call(PosResult<String> result) {
                return result.isSuccessful();
              }
            };
            final List<Observable<Boolean>> observables = new ArrayList<>();
            for (Product product : products) {
              observables.add(bridge.addCard(phoneNumber, pin, product.getAlias()).map(mapFunc1));
            }
            return Observable.concat(observables);
          }
        }
      })
      .reduce(true, new Func2<Boolean, Boolean, Boolean>() {
        @Override
        public Boolean call(Boolean flag, Boolean result) {
          return flag && result;
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
