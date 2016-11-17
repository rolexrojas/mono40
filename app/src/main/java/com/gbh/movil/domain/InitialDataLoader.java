package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import com.gbh.movil.Utils;
import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.domain.api.ApiResult;

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
      .flatMap(new Func1<ApiResult<InitialData>, Observable<Object>>() {
        @Override
        public Observable<Object> call(ApiResult<InitialData> result) {
          if (result.isSuccessful()) {
            final InitialData data = result.getData();
            if (Utils.isNotNull(data)) {
              return productManager.syncAccounts(data.getProducts())
                .cast(Object.class)
                .concatWith(recipientManager.syncRecipients(data.getRecipients()))
                .last();
            } else { // This is no suppose to happen.
              return Observable.error(new NullPointerException("Result's data is missing"));
            }
          } else {
            // TODO: Find or create a suitable exception for this case.
            return Observable.error(new Exception("Failed to load initial data"));
          }
        }
      });
  }
}
