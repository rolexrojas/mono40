package com.tpago.movil.d.domain;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.tpago.movil.Bank;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.domain.pos.PosResult;
import com.tpago.movil.d.domain.session.Session;
import com.tpago.movil.d.domain.util.EventBus;
import com.tpago.movil.d.misc.rx.RxUtils;
import com.tpago.movil.util.Objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dagger.Lazy;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * @author hecvasro
 */
@Deprecated
public final class ProductManager implements ProductProvider {
  private final ProductRepo productRepo;
  private final Lazy<PosBridge> posBridge;
  private final EventBus eventBus;
  private final com.tpago.movil.d.domain.session.SessionManager sessionManager;

  public ProductManager(
    @NonNull ProductRepo productRepo,
    @NonNull Lazy<PosBridge> posBridge,
    @NonNull EventBus eventBus,
    @NonNull com.tpago.movil.d.domain.session.SessionManager sessionManager) {
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
            observable = productRepo.remove(product).cast(Object.class)
              .concatWith(posBridge.get().removeCard(product.getAlias()));
          }
          return observable;
        }
      })
      .last();
  }

  public final Observable<List<Product>> getAllPaymentOptions() {
    return getAll()
      .map(new Func1<List<Product>, List<Product>>() {
        @Override
        public List<Product> call(List<Product> productList) {
          Product defaultPaymentMethod = null;
          List<Product> paymentMethodList = new ArrayList<>();
          for (Product product : productList) {
            if (Product.isDefaultPaymentOption(product)) {
              defaultPaymentMethod = product;
            } else if (Product.isPaymentOption(product)) {
              paymentMethodList.add(product);
            }
          }
          if (Objects.isNotNull(defaultPaymentMethod)) {
            paymentMethodList.add(0, defaultPaymentMethod);
          }
          return paymentMethodList;
        }
      });
  }

  public final Observable<List<PosResult>> activateAllProducts(final String pin) {
    return getAllPaymentOptions()
      .flatMap(new Func1<List<Product>, Observable<PosResult>>() {
        @Override
        public Observable<PosResult> call(List<Product> productList) {
          final Session s = sessionManager.getSession();
          if (Objects.isNull(s)) {
            return Observable.error(new NullPointerException("s == null"));
          } else {
            final PosBridge b = posBridge.get();
            final String pn = s.getPhoneNumber();
            Observable<PosResult> o = null;
            for (Product p : productList) {
              if (Objects.isNull(o)) {
                o = b.addCard(pn, pin, p.getAlias());
              } else {
                o = o.concatWith(b.addCard(pn, pin, p.getAlias()));
              }
            }
            return o;
          }
        }
      })
      .toList();
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

  @Override
  public Observable<List<Product>> getAll() {
    return productRepo.getAll()
      .compose(RxUtils.<Product>fromCollection())
      .toSortedList(new Func2<Product, Product, Integer>() {
        @Override
        public Integer call(Product pa, Product pb) {
          final int r = Bank.getName(pa.getBank()).compareTo(Bank.getName(pb.getBank()));
          if (r == 0) {
            return pa.getAlias().compareTo(pb.getAlias());
          } else {
            return r;
          }
        }
      });
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
