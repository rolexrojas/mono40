package com.tpago.movil.data.repo;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.tpago.movil.domain.Product;
import com.tpago.movil.domain.ProductRepo;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author hecvasro
 */
final class SharedPreferencesProductRepo implements ProductRepo {
  private static final String KEY_INDEX = "index";

  private final SharedPreferences sharedPreferences;
  private final Gson gson;

  private final Set<String> indexSet;

  SharedPreferencesProductRepo(SharedPreferences sharedPreferences, Gson gson) {
    this.sharedPreferences = sharedPreferences;
    this.gson = gson;
    this.indexSet = this.sharedPreferences.getStringSet(KEY_INDEX, new HashSet<String>());
  }

  private static String getProductKey(Product product) {
    return Integer.toString(product.hashCode());
  }

  @NonNull
  @Override
  public Observable<Product> save(@NonNull Product product) {
    return Observable.just(product)
      .doOnNext(new Action1<Product>() {
        @Override
        public void call(Product product) {
          final String productKey = getProductKey(product);
          if (!indexSet.contains(productKey)) {
            indexSet.add(productKey);
          }
          sharedPreferences.edit()
            .putStringSet(KEY_INDEX, indexSet)
            .putString(productKey, gson.toJson(product))
            .apply();
        }
      });
  }

  @NonNull
  @Override
  public Observable<Product> remove(@NonNull Product product) {
    return Observable.just(product)
      .doOnNext(new Action1<Product>() {
        @Override
        public void call(Product product) {
          final String productKey = getProductKey(product);
          if (indexSet.contains(productKey)) {
            indexSet.remove(productKey);
          }
          sharedPreferences.edit()
            .putStringSet(KEY_INDEX, indexSet)
            .remove(productKey)
            .apply();
        }
      });
  }

  @Override
  public void clear() {
    indexSet.clear();
    sharedPreferences.edit()
      .clear()
      .apply();
  }

  @NonNull
  @Override
  public Observable<List<Product>> getAll() {
    return Observable.from(indexSet)
      .map(new Func1<String, Product>() {
        @Override
        public Product call(String productKey) {
          return gson.fromJson(sharedPreferences.getString(productKey, null), Product.class);
        }
      })
      .toList();
  }
}
