package com.gbh.movil.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.domain.api.ApiUtils;
import com.gbh.movil.ui.main.payments.recipients.Contact;

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
   * TODO
   *
   * @param phoneNumber
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  public final Observable<Boolean> checkIfAssociated(@NonNull PhoneNumber phoneNumber) {
    return apiBridge.checkIfAssociated(phoneNumber)
      .compose(ApiUtils.<Boolean>handleApiResult(true));
  }

  /**
   * TODO
   *
   * @param contact
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  public final Observable<Recipient> addRecipient(@NonNull Contact contact) {
    return Observable.just(contact)
      .flatMap(new Func1<Contact, Observable<Recipient>>() {
        @Override
        public Observable<Recipient> call(Contact contact) {
          return recipientRepo.save(new PhoneNumberRecipient(contact.getPhoneNumber(),
            contact.getName()));
        }
      });
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
