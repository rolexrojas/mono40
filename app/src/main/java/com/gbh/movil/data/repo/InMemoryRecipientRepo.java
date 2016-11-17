package com.gbh.movil.data.repo;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.rx.RxUtils;
import com.gbh.movil.domain.recipient.Contact;
import com.gbh.movil.domain.recipient.ContactRecipient;
import com.gbh.movil.domain.Recipient;
import com.gbh.movil.domain.recipient.RecipientRepo;

import java.util.HashSet;
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
  private final Set<Recipient> recipients = new HashSet<>();

  InMemoryRecipientRepo() {
    recipients.add(new ContactRecipient(new Contact("Luis Ruiz", "8092817626", Uri.EMPTY)));
    recipients.add(new ContactRecipient(new Contact("Hector Vasquez", "8098829887", Uri.EMPTY)));
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<Set<Recipient>> getAll(@Nullable final String query) {
    return Observable.just(recipients)
      .compose(RxUtils.<Recipient>fromCollection())
      .filter(new Func1<Recipient, Boolean>() {
        @Override
        public Boolean call(Recipient recipient) {
          return recipient.matches(query);
        }
      })
      .compose(RxUtils.<Recipient>toSet());
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
      .compose(RxUtils.<Recipient>toSet());
  }
}
