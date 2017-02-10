package com.tpago.movil.d.domain;

import android.support.annotation.NonNull;

import com.tpago.movil.d.domain.api.ApiBridge;
import com.tpago.movil.d.domain.api.ApiUtils;

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
  private final com.tpago.movil.d.domain.session.SessionManager sessionManager;

  public InitialDataLoader(@NonNull ApiBridge apiBridge, @NonNull ProductManager productManager,
    @NonNull RecipientManager recipientManager,
    @NonNull com.tpago.movil.d.domain.session.SessionManager sessionManager) {
    this.apiBridge = apiBridge;
    this.productManager = productManager;
    this.recipientManager = recipientManager;
    this.sessionManager = sessionManager;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final Observable<Object> load() {
    return apiBridge.initialLoad(sessionManager.getSession().getAuthToken())
      .compose(ApiUtils.<InitialData>handleApiResult(true))
      .flatMap(new Func1<InitialData, Observable<Object>>() {
        @Override
        public Observable<Object> call(InitialData data) {
          return productManager.syncProducts(data.getProducts())
            .concatWith(recipientManager.syncRecipients(data.getRecipients()))
            .last();
        }
      });
  }
}
