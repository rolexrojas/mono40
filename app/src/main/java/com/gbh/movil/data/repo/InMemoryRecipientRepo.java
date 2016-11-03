package com.gbh.movil.data.repo;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Recipient;
import com.gbh.movil.domain.RecipientRepo;
import com.gbh.movil.domain.RecipientType;

import java.util.HashSet;
import java.util.Set;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author hecvasro
 */
class InMemoryRecipientRepo implements RecipientRepo {
  private final Set<Recipient> recipients = new HashSet<>();

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<Set<Recipient>> saveAll(@NonNull Set<Recipient> recipientsToSave) {
    return Observable.just(recipientsToSave)
      .map(new Func1<Set<Recipient>, Set<Recipient>>() {
        @Override
        public Set<Recipient> call(Set<Recipient> recipientsToSave) {
          for (Recipient recipient : recipients) {
            if (recipient.getType() == RecipientType.CONTACT) {
              recipients.remove(recipient);
            }
          }
          recipients.addAll(recipientsToSave);
          return null;
        }
      });
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<Set<Recipient>> getAll() {
    return Observable.just(recipients);
  }
}
