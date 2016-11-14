package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import com.gbh.movil.Utils;
import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.domain.api.ApiResult;

import rx.Observable;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class InitialDataLoader {
  private final ApiBridge apiBridge;
  private final AccountManager accountManager;
  private final RecipientRepo recipientRepo;

  public InitialDataLoader(@NonNull ApiBridge apiBridge, @NonNull AccountManager accountManager,
    @NonNull RecipientRepo recipientRepo) {
    this.apiBridge = apiBridge;
    this.accountManager = accountManager;
    this.recipientRepo = recipientRepo;
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
              return accountManager.syncAccounts(data.getAccounts())
                .cast(Object.class)
                .concatWith(recipientRepo.saveAll(data.getRecipients()))
                .last();
            } else { // This is no suppose to happen.
              return Observable.error(new NullPointerException("Result's data is not available"));
            }
          } else {
            Timber.d("Initial load failed (%1$s)", result);
            // TODO: Find or create a suitable exception for this case.
            return Observable.error(new Exception());
          }
        }
      });
  }
}
