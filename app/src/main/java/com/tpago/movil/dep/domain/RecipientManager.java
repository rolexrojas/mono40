package com.tpago.movil.dep.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.tpago.movil.dep.domain.api.DepApiBridge;
import com.tpago.movil.dep.domain.api.ApiUtils;
import com.tpago.movil.dep.domain.session.SessionManager;
import com.tpago.movil.dep.misc.rx.RxUtils;
import com.tpago.movil.dep.ui.main.recipients.Contact;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * TODO
 *
 * @author hecvasro
 */
@Deprecated
public final class RecipientManager implements RecipientProvider {
  private final RecipientRepo recipientRepo;
  private final DepApiBridge apiBridge;
  private final com.tpago.movil.dep.domain.session.SessionManager sessionManager;

  public RecipientManager(
    @NonNull RecipientRepo recipientRepo,
    @NonNull DepApiBridge apiBridge,
    @NonNull SessionManager sessionManager) {
    this.recipientRepo = recipientRepo;
    this.apiBridge = apiBridge;
    this.sessionManager = sessionManager;
  }

  @NonNull
  final Observable<List<Recipient>> syncRecipients(@NonNull List<Recipient> recipients) {
    return recipientRepo.saveAll(recipients);
  }

  @NonNull
  public final Observable<Boolean> checkIfAffiliated(@NonNull String phoneNumber) {
    return apiBridge.checkIfAffiliated(sessionManager.getSession().getAuthToken(), phoneNumber)
      .compose(ApiUtils.<Boolean>handleApiResult(true));
  }

  @NonNull
  public final Observable<Pair<Boolean, Recipient>> addRecipient(
    @NonNull final String phoneNumber,
    @Nullable final String label) {
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
    return Observable.concat(
      recipientRepo.getAll(query)
        .compose(RxUtils.<Recipient>fromCollection()),
      apiBridge.recipients(sessionManager.getSession().getAuthToken())
        .compose(ApiUtils.<List<Recipient>>handleApiResult(true))
        .compose(RxUtils.<Recipient>fromCollection())
        .flatMap(new Func1<Recipient, Observable<Recipient>>() {
          @Override
          public Observable<Recipient> call(Recipient recipient) {
            return recipientRepo.save(recipient);
          }
        }))
      .onErrorResumeNext(recipientRepo.getAll(query).compose(RxUtils.<Recipient>fromCollection()))
      .distinct()
      .compose(Recipient.toSortedListByIdentifier());
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
