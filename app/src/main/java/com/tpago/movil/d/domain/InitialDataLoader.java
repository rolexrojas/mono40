package com.tpago.movil.d.domain;

import com.tpago.movil.Session;
import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.api.DepApiBridge;

import rx.Completable;
import rx.functions.Action1;
import timber.log.Timber;

/**
 * @author hecvasro
 */
@Deprecated
public final class InitialDataLoader {
  private final DepApiBridge apiBridge;
  private final ProductManager productManager;
  private final RecipientManager recipientManager;

  public InitialDataLoader(
    DepApiBridge apiBridge,
    ProductManager productManager,
    RecipientManager recipientManager) {
    this.apiBridge = apiBridge;
    this.productManager = productManager;
    this.recipientManager = recipientManager;
  }

  @Deprecated public final Completable load(final Session session) {
    return apiBridge.initialLoad(session.getToken())
      .doOnNext(new Action1<ApiResult<InitialData>>() {
        @Override
        public void call(ApiResult<InitialData> result) {
          if (result.isSuccessful()) {
            final InitialData data = result.getData();
            productManager.syncProducts(session.getToken(), data.getProducts());
            recipientManager.syncRecipients(session.getToken(), data.getRecipients());
          }
        }
      })
      .toCompletable();
  }
}
