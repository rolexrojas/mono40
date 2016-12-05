package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.domain.api.ApiUtils;

import rx.Observable;
import rx.functions.Func1;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class InitialDataLoader {
  private final ApiBridge apiBridge;
  private final ProductManager productManager;
  private final RecipientManager recipientManager;

  public InitialDataLoader(@NonNull ApiBridge apiBridge, @NonNull ProductManager productManager,
    @NonNull RecipientManager recipientManager) {
    this.apiBridge = apiBridge;
    this.productManager = productManager;
    this.recipientManager = recipientManager;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final Observable<Object> load() {
    return apiBridge.initialLoad()
      .compose(ApiUtils.<InitialData>handleApiResult(true))
      .flatMap(new Func1<InitialData, Observable<Object>>() {
        @Override
        public Observable<Object> call(InitialData data) {
          return productManager.syncProducts(data.getProducts())
            .cast(Object.class)
            .concatWith(recipientManager.syncRecipients(data.getRecipients()))
            .last();
        }
      });
  }
}
