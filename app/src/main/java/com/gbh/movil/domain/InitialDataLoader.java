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
                .concatWith(recipientRepo.save(data.getRecipients()))
                .last();
            } else {
              Timber.d("Initial load was empty");
              return Observable.just(null);
            }
          } else {
            Timber.d("Initial load failed");
            return Observable.just(null);
          }
        }
      });
  }
}
