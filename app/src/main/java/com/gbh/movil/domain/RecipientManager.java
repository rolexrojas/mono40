package com.gbh.movil.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.domain.api.ApiUtils;
import com.gbh.movil.ui.main.recipients.Contact;

import java.util.List;

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
  final Observable<List<Recipient>> syncRecipients(@NonNull List<Recipient> recipients) {
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
  public final Observable<Boolean> checkIfAffiliated(@NonNull PhoneNumber phoneNumber) {
    return apiBridge.checkIfAffiliated(phoneNumber)
      .compose(ApiUtils.<Boolean>handleApiResult(true));
  }

  /**
   * TODO
   *
   * @param phoneNumber
   *   TODO
   * @param label
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  public final Observable<Pair<Boolean, Recipient>> addRecipient(
    @NonNull final PhoneNumber phoneNumber, @Nullable final String label) {
    return checkIfAffiliated(phoneNumber)
      .flatMap(new Func1<Boolean, Observable<Pair<Boolean, Recipient>>>() {
        @Override
        public Observable<Pair<Boolean, Recipient>> call(Boolean affiliated) {
          if (affiliated) {
            return recipientRepo.save(new ContactRecipient(phoneNumber, label))
              .map(new Func1<Recipient, Pair<Boolean, Recipient>>() {
                @Override
                public Pair<Boolean, Recipient> call(Recipient recipient) {
                  return Pair.create(true, recipient);
                }
              });
          } else {
            return Observable.just(Pair.<Boolean, Recipient>create(false, null));
          }
        }
      });
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
  public final Observable<Pair<Boolean, Recipient>> addRecipient(@NonNull PhoneNumber phoneNumber) {
    return addRecipient(phoneNumber, null);
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
  public final Observable<Pair<Boolean, Recipient>> addRecipient(@NonNull Contact contact) {
    return addRecipient(contact.getPhoneNumber(), contact.getName());
  }

  /**
   * TODO
   *
   * @param recipient
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  public final Observable<Recipient> updateRecipient(@NonNull Recipient recipient) {
    return recipientRepo.save(recipient);
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<List<Recipient>> getAll(@Nullable final String query) {
    return apiBridge.recipients()
      .compose(ApiUtils.<List<Recipient>>handleApiResult(true))
      .flatMap(new Func1<List<Recipient>, Observable<List<Recipient>>>() {
        @Override
        public Observable<List<Recipient>> call(List<Recipient> recipients) {
          return syncRecipients(recipients)
            .flatMap(new Func1<List<Recipient>, Observable<List<Recipient>>>() {
              @Override
              public Observable<List<Recipient>> call(List<Recipient> recipients) {
                return recipientRepo.getAll(query);
              }
            });
        }
      })
      .onErrorResumeNext(recipientRepo.getAll(query));
  }
}
