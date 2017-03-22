package com.tpago.movil.d.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.api.ApiUtils;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.misc.rx.RxUtils;
import com.tpago.movil.d.ui.main.recipients.Contact;

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
  private final com.tpago.movil.d.domain.session.SessionManager sessionManager;

  public RecipientManager(
    @NonNull RecipientRepo recipientRepo,
    @NonNull DepApiBridge apiBridge,
    @NonNull SessionManager sessionManager) {
    this.recipientRepo = recipientRepo;
    this.apiBridge = apiBridge;
    this.sessionManager = sessionManager;
  }

  private Observable<Recipient> queryBalance(Recipient recipient) {
    if (recipient instanceof BillRecipient) {
      return apiBridge.queryBalance(
        sessionManager.getSession().getAuthToken(),
        (BillRecipient) recipient);
    } else {
      return Observable.just(recipient);
    }
  }

  private Observable<Pair<Boolean, Recipient>> addRecipient(
    final String phoneNumber,
    final String label) {
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

  @NonNull
  final Observable<List<Recipient>> syncRecipients(@NonNull List<Recipient> recipients) {
    return Observable.from(recipients)
      .flatMap(new Func1<Recipient, Observable<Recipient>>() {
        @Override
        public Observable<Recipient> call(Recipient recipient) {
          return queryBalance(recipient)
            .onErrorResumeNext(Observable.just(recipient));
        }
      })
      .flatMap(new Func1<Recipient, Observable<Recipient>>() {
        @Override
        public Observable<Recipient> call(Recipient recipient) {
          return recipientRepo.save(recipient);

        }
      })
      .compose(Recipient.toSortedListByIdentifier());
  }

  @NonNull
  public final Observable<Boolean> checkIfAffiliated(@NonNull String phoneNumber) {
    return apiBridge.checkIfAffiliated(sessionManager.getSession().getAuthToken(), phoneNumber)
      .compose(ApiUtils.<Boolean>handleApiResult(true));
  }

  @NonNull
  public final Observable<Pair<Boolean, Recipient>> addRecipient(@NonNull String phoneNumber) {
    return addRecipient(phoneNumber, null);
  }

  @NonNull
  public final Observable<Pair<Boolean, Recipient>> addRecipient(@NonNull Contact contact) {
    return addRecipient(contact.getPhoneNumber().toString(), contact.getName());
  }

  @NonNull
  public final Observable<Recipient> updateRecipient(@NonNull Recipient recipient) {
    return queryBalance(recipient)
      .flatMap(new Func1<Recipient, Observable<Recipient>>() {
        @Override
        public Observable<Recipient> call(Recipient recipient) {
          return recipientRepo.save(recipient);
        }
      });
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<List<Recipient>> getAll(@Nullable final String query) {
    return recipientRepo.getAll(query)
      .compose(RxUtils.<Recipient>fromCollection())
      .filter(new Func1<Recipient, Boolean>() {
        @Override
        public Boolean call(Recipient recipient) {
          return recipient.matches(query);
        }
      })
      .compose(Recipient.toSortedListByIdentifier());
//    return Observable.concat(
//      recipientRepo.getAll(query)
//        .compose(RxUtils.<Recipient>fromCollection()),
//      apiBridge.recipients(sessionManager.getSession().getAuthToken())
//        .compose(ApiUtils.<List<Recipient>>handleApiResult(true))
//        .compose(RxUtils.<Recipient>fromCollection())
//        .flatMap(new Func1<Recipient, Observable<Recipient>>() {
//          @Override
//          public Observable<Recipient> call(Recipient recipient) {
//            return recipientRepo.save(recipient);
//          }
//        })
//        .filter(new Func1<Recipient, Boolean>() {
//          @Override
//          public Boolean call(Recipient recipient) {
//            return recipient.matches(query);
//          }
//        }))
//      .onErrorResumeNext(recipientRepo.getAll(query).compose(RxUtils.<Recipient>fromCollection()))
//      .distinct()
//      .compose(Recipient.toSortedListByIdentifier());
  }

  public final Observable<List<Recipient>> remove(List<Recipient> recipients, final String pin) {
    return Observable.from(recipients)
      .flatMap(new Func1<Recipient, Observable<Recipient>>() {
        @Override
        public Observable<Recipient> call(final Recipient recipient) {
          if (recipient.getType().equals(RecipientType.BILL)) {
            return apiBridge.removeBill(
              sessionManager.getSession().getAuthToken(),
              (BillRecipient) recipient,
              pin)
              .flatMap(new Func1<ApiResult<Recipient>, Observable<Recipient>>() {
                @Override
                public Observable<Recipient> call(ApiResult<Recipient> result) {
                  if (result.isSuccessful()) {
                    return recipientRepo.remove(result.getData());
                  } else {
                    return Observable.just(result.getData());
                  }
                }
              });
          } else {
            return recipientRepo.remove(recipient);
          }
        }
      })
      .toList();
  }

  public void clear() {
    recipientRepo.clear();
  }

  public final boolean checkIfExists(Recipient recipient) {
    return recipientRepo.checkIfExists(recipient);
  }

  public final void addSync(Recipient recipient) {
    recipientRepo.saveSync(recipient);
  }
}
