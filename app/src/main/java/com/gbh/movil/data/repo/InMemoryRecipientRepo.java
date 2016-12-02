package com.gbh.movil.data.repo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.rx.RxUtils;
import com.gbh.movil.domain.Recipient;
import com.gbh.movil.domain.RecipientRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * {@link RecipientRepo Recipient repository} implementation that uses memory as storage.
 *
 * @author hecvasro
 */
class InMemoryRecipientRepo implements RecipientRepo {
  private final List<Recipient> recipients = new ArrayList<>();

  InMemoryRecipientRepo() {
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<List<Recipient>> getAll(@Nullable final String query) {
    return Observable.just(recipients)
      .compose(RxUtils.<Recipient>fromCollection())
      .filter(new Func1<Recipient, Boolean>() {
        @Override
        public Boolean call(Recipient recipient) {
          return recipient.matches(query);
        }
      })
      .compose(Recipient.toSortedListByIdentifier());
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
  public Observable<List<Recipient>> saveAll(@NonNull List<Recipient> recipients) {
    return Observable.from(recipients)
      .flatMap(new Func1<Recipient, Observable<Recipient>>() {
        @Override
        public Observable<Recipient> call(Recipient recipient) {
          return save(recipient);
        }
      })
      .compose(Recipient.toSortedListByIdentifier());
  }
}
