package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import com.gbh.movil.Utils;
import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.domain.api.ApiCode;
import com.gbh.movil.domain.api.ApiUtils;

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
      .flatMap(new Func1<Result<ApiCode, InitialData>, Observable<Object>>() {
        @Override
        public Observable<Object> call(Result<ApiCode, InitialData> result) {
          if (ApiUtils.isSuccessful(result)) {
            final InitialData data = result.getData();
            if (Utils.isNotNull(data)) {
              return accountManager.syncAccounts(data.getAccounts())
                .cast(Object.class)
                .concatWith(recipientRepo.save(data.getRecipients()))
                .last();
            } else { // This is an edge case.
              Timber.d("Initial load result is empty");
              return Observable.just(null);
            }
          } else {
            Timber.d("Initial load failed (%1$s)", result);
            return Observable.just(null);
          }
        }
      });
  }
}
