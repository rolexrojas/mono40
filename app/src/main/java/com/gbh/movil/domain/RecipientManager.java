package com.gbh.movil.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.Utils;
import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.domain.api.ApiResult;

import java.util.Set;

import rx.Observable;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class RecipientManager implements RecipientProvider {
  private final RecipientRepo recipientRepo;
  private final ApiBridge apiBridge;

  public RecipientManager(@NonNull RecipientRepo recipientRepo, @NonNull ApiBridge apiBridge) {
    this.recipientRepo = recipientRepo;
    this.apiBridge = apiBridge;
  }

  /**
   * TODO
   *
   * @param recipients
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<Set<Recipient>> syncRecipients(@NonNull Set<Recipient> recipients) {
    return recipientRepo.saveAll(recipients);
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<Set<Recipient>> getAll(@Nullable final String query) {
    return recipientRepo.getAll(query)
      .concatWith(apiBridge.recipients()
        .flatMap(new Func1<ApiResult<Set<Recipient>>, Observable<Set<Recipient>>>() {
          @Override
          public Observable<Set<Recipient>> call(ApiResult<Set<Recipient>> result) {
            if (result.isSuccessful()) {
              final Set<Recipient> data = result.getData();
              if (Utils.isNotNull(data)) {
                return syncRecipients(data)
                  .flatMap(new Func1<Set<Recipient>, Observable<Set<Recipient>>>() {
                    @Override
                    public Observable<Set<Recipient>> call(Set<Recipient> recipients) {
                      return recipientRepo.getAll(query);
                    }
                  });
              } else { // This is not supposed to happen.
                return Observable.error(new NullPointerException("Result's data is not available"));
              }
            } else {
              Timber.d("Failed to load all registered accounts (%1$s)", result);
              // TODO: Find or create a suitable exception for this case.
              return Observable.error(new Exception());
            }
          }
        }));
  }
}
