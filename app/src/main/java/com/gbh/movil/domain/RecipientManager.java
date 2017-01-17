package com.gbh.movil.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.domain.api.ApiUtils;
import com.gbh.movil.domain.session.SessionManager;
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
  private final com.gbh.movil.domain.session.SessionManager sessionManager;

  public RecipientManager(@NonNull RecipientRepo recipientRepo, @NonNull ApiBridge apiBridge,
    @NonNull SessionManager sessionManager) {
    this.recipientRepo = recipientRepo;
    this.apiBridge = apiBridge;
    this.sessionManager = sessionManager;
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
  public final Observable<Boolean> checkIfAffiliated(@NonNull String phoneNumber) {
    return apiBridge.checkIfAffiliated(sessionManager.getSession().getAuthToken(), phoneNumber)
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
    @NonNull final String phoneNumber, @Nullable final String label) {
    return checkIfAffiliated(phoneNumber)
      .flatMap(new Func1<Boolean, Observable<Pair<Boolean, Recipient>>>() {
        @Override
        public Observable<Pair<Boolean, Recipient>> call(Boolean affiliated) {
          if (affiliated) {
            return recipientRepo.save(new PhoneNumberRecipient(phoneNumber, label))
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
  public final Observable<Pair<Boolean, Recipient>> addRecipient(@NonNull String phoneNumber) {
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
    return addRecipient(contact.getPhoneNumber().toString(), contact.getName());
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
    return apiBridge.recipients(sessionManager.getSession().getAuthToken())
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

  public final Observable<List<Recipient>> remove(List<Recipient> recipients) {
    return Observable.from(recipients)
      .flatMap(new Func1<Recipient, Observable<Recipient>>() {
        @Override
        public Observable<Recipient> call(Recipient recipient) {
          return recipientRepo.remove(recipient);
        }
      })
      .toList();
  }

  public void clear() {
    recipientRepo.clear();
  }
}
