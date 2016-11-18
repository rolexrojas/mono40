package com.gbh.movil.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.domain.api.ApiUtils;

import java.util.Set;

import rx.Observable;
import rx.functions.Func1;

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
  final Observable<Set<Recipient>> syncRecipients(@NonNull Set<Recipient> recipients) {
    return recipientRepo.saveAll(recipients);
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<Set<Recipient>> getAll(@Nullable final String query) {
    return apiBridge.recipients()
      .compose(ApiUtils.<Set<Recipient>>handleApiResult(true))
      .flatMap(new Func1<Set<Recipient>, Observable<Set<Recipient>>>() {
        @Override
        public Observable<Set<Recipient>> call(Set<Recipient> recipients) {
          return syncRecipients(recipients)
            .flatMap(new Func1<Set<Recipient>, Observable<Set<Recipient>>>() {
              @Override
              public Observable<Set<Recipient>> call(Set<Recipient> recipients) {
                return recipientRepo.getAll(query);
              }
            });
        }
      })
      .onErrorResumeNext(recipientRepo.getAll(query));
  }
}
