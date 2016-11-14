package com.gbh.movil.data.repo;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Recipient;
import com.gbh.movil.domain.RecipientRepo;

import java.util.HashSet;
import java.util.Set;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;

/**
 * {@link RecipientRepo Recipient repository} implementation that uses memory as storage.
 *
 * @author hecvasro
 */
class InMemoryRecipientRepo implements RecipientRepo {
  private final Set<Recipient> recipients = new HashSet<>();

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<Set<Recipient>> getAll() {
    return Observable.just(recipients);
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<Recipient> save(@NonNull Recipient recipient) {
    return Observable.just(recipient)
      .doOnNext(new Action1<Recipient>() {
        @Override
        public void call(Recipient recipient) {
          if (recipients.contains(recipient)) {
            recipients.remove(recipient);
          }
          recipients.add(recipient);
        }
      });
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<Set<Recipient>> saveAll(@NonNull Set<Recipient> recipients) {
    return Observable.from(recipients)
      .flatMap(new Func1<Recipient, Observable<Recipient>>() {
        @Override
        public Observable<Recipient> call(Recipient recipient) {
          return save(recipient);
        }
      })
      .collect(new Func0<Set<Recipient>>() {
        @Override
        public Set<Recipient> call() {
          return new HashSet<>();
        }
      }, new Action2<Set<Recipient>, Recipient>() {
        @Override
        public void call(Set<Recipient> recipients, Recipient recipient) {
          recipients.add(recipient);
        }
      });
  }
}
