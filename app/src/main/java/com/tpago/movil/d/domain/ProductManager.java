package com.tpago.movil.d.domain;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.util.Pair;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tpago.movil.dep.api.ApiImageUriBuilder;
import com.tpago.movil.dep.content.SharedPreferencesCreator;
import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.domain.pos.PosResult;
import com.tpago.movil.d.domain.util.EventBus;
import com.tpago.movil.util.ObjectHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dagger.Lazy;

/**
 * @author hecvasro
 */
@Deprecated
public final class ProductManager {

  private static final String KEY_INDEX_SET = "indexSet";
  private static final String KEY_DEFAULT_PAYMENT_OPTION_ID = "defaultPaymentOption";

  private final Context context;
  private final Lazy<PosBridge> posBridge;
  private final EventBus eventBus;
  private final DepApiBridge apiBridge;

  private final SharedPreferences sharedPreferences;
  private final Gson gson;

  private final Set<String> indexSet;

  private List<Product> productList;

  private Product defaultPaymentOption;
  private List<Product> paymentOptionList;

  public ProductManager(
    SharedPreferencesCreator sharedPreferencesCreator,
    Gson gson,
    Context context,
    EventBus eventBus,
    DepApiBridge apiBridge,
    Lazy<PosBridge> posBridge
  ) {

    this.sharedPreferences = sharedPreferencesCreator
      .create(ProductManager.class.getCanonicalName());
    this.gson = gson;

    this.indexSet = this.sharedPreferences.getStringSet(KEY_INDEX_SET, new HashSet<>());

    this.context = context;
    this.eventBus = eventBus;
    this.apiBridge = apiBridge;
    this.posBridge = posBridge;
  }

  @Deprecated
  final void syncProducts(final List<Product> remoteProductList) {
    if (ObjectHelper.isNull(productList)) {
      productList = new ArrayList<>();
    }
    productList.clear();
    for (String id : indexSet) {
      productList.add(gson.fromJson(sharedPreferences.getString(id, null), Product.class));
    }

    Product rdpo = null;
    final List<Product> ptal = new ArrayList<>();
    final List<Product> ptul = new ArrayList<>();
    final List<Product> ptrl = new ArrayList<>();

    for (Product remoteProduct : remoteProductList) {
      if (!productList.contains(remoteProduct)) {
        ptal.add(remoteProduct);
      } else {
        ptul.add(remoteProduct);
      }
      if (Product.isDefaultPaymentOption(remoteProduct)) {
        rdpo = remoteProduct;
      }
    }

    for (Product localProduct : productList) {
      if (!remoteProductList.contains(localProduct)) {
        ptrl.add(localProduct);
      }
    }

    final SharedPreferences.Editor editor = sharedPreferences.edit();

    productList.clear();
    for (Product p : ptal) {
      productList.add(p);
      indexSet.add(p.getId());
      editor.putString(p.getId(), gson.toJson(p));
    }
    for (Product p : ptul) {
      productList.add(p);
      indexSet.add(p.getId());
      editor.putString(p.getId(), gson.toJson(p));
    }
    if (!ptrl.isEmpty()) {
      final PosBridge bridge = posBridge.get();
      for (Product p : ptrl) {
        if (bridge.isRegistered(p.posBridgeIdentifier())) {
          bridge.removeCard(p.posBridgeIdentifier());
        }
        indexSet.remove(p.getId());
        editor.remove(p.getId());
      }
    }

    editor.putStringSet(KEY_INDEX_SET, indexSet);

    //  if [not remote.default] then
    //    destroySession local.default
    //    destroySession local.temporary
    //  else if [not local.temporary] then
    //    set local.default remote.default
    //  else if [remote.default equals to local.temporary] then
    //    set local.default local.temporary
    //    set remote.default local.temporary
    //    destroySession local.temporary
    if (ObjectHelper.isNull(rdpo)) {
      defaultPaymentOption = null;
      editor.remove(KEY_DEFAULT_PAYMENT_OPTION_ID);
    } else {
      defaultPaymentOption = rdpo;
      editor.putString(KEY_DEFAULT_PAYMENT_OPTION_ID, defaultPaymentOption.getId());
    }

    editor.apply();

    if (!ptal.isEmpty()) {
      eventBus.dispatch(new ProductAdditionEvent());
    }
    if (!ptrl.isEmpty()) {
      eventBus.dispatch(new ProductRemovalEvent());
    }

    Collections.sort(productList, Product.comparator());
    if (ObjectHelper.isNull(paymentOptionList)) {
      paymentOptionList = new ArrayList<>();
    }
    paymentOptionList.clear();
    if (ObjectHelper.isNotNull(defaultPaymentOption)) {
      paymentOptionList.add(defaultPaymentOption);
    }
    for (Product p : productList) {
      if (Product.checkIfCreditCard(p)) {
        Picasso.with(context)
          .load(ApiImageUriBuilder.build(context, p))
          .fetch();
      }
      if (Product.isPaymentOption(p) && !paymentOptionList.contains(p)) {
        paymentOptionList.add(p);
      }
    }
  }

  public final void clear() {
    productList.clear();
    defaultPaymentOption = null;
    paymentOptionList.clear();
    indexSet.clear();
    sharedPreferences.edit()
      .clear()
      .apply();
  }

  public final List<Product> getProductList() {
    return productList;
  }

  public final Product getDefaultPaymentOption() {
    return defaultPaymentOption;
  }

  public final List<Product> getPaymentOptionList() {
    return paymentOptionList;
  }

  public final List<Pair<Product, PosResult>> registerPaymentOptionList(
    final String phoneNumber,
    final String pin
  ) {
    final PosBridge b = posBridge.get();
    final List<Pair<Product, PosResult>> resultList = new ArrayList<>();
    for (Product po : paymentOptionList) {
      resultList.add(Pair.create(po, b.addCard(phoneNumber, pin, po.posBridgeIdentifier())));
    }
    return resultList;
  }

  public final boolean setDefaultPaymentOption(final Product paymentOption) {
    if (paymentOption.equals(defaultPaymentOption)) {
      return true;
    } else {
      final ApiResult<Void> result = apiBridge.setDefaultPaymentOption(paymentOption);
      if (result.isSuccessful()) {
        defaultPaymentOption = paymentOption;
        sharedPreferences.edit()
          .putString(KEY_DEFAULT_PAYMENT_OPTION_ID, defaultPaymentOption.getId())
          .apply();
        return true;
      } else {
        return false;
      }
    }
  }
}
