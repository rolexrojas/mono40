package com.tpago.movil.data.repo;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.domain.Recipient;
import com.tpago.movil.domain.RecipientRepo;
import com.google.gson.Gson;

import java.util.HashSet;
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
class SharedPreferencesRecipientRepo implements RecipientRepo {
  private static final String KEY_INDEX = "index";

  private final SharedPreferences sharedPreferences;
  private final Gson gson;

  private final Set<String> indexSet;

  SharedPreferencesRecipientRepo(SharedPreferences sharedPreferences, Gson gson) {
    this.sharedPreferences = sharedPreferences;
    this.gson = gson;
    this.indexSet = this.sharedPreferences.getStringSet(KEY_INDEX, new HashSet<String>());
  }

  private static String getRecipientKey(Recipient recipient) {
    return Integer.toString(recipient.hashCode());
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<List<Recipient>> getAll(@Nullable final String query) {
    return Observable.from(indexSet)
      .map(new Func1<String, Recipient>() {
        @Override
        public Recipient call(String recipientKey) {
          return gson.fromJson(sharedPreferences.getString(recipientKey, null), Recipient.class);
        }
      })
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
          final String recipientKey = getRecipientKey(recipient);
          if (!indexSet.contains(recipientKey)) {
            indexSet.add(recipientKey);
          }
          sharedPreferences.edit()
            .putStringSet(KEY_INDEX, indexSet)
            .putString(recipientKey, gson.toJson(recipient))
            .apply();
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

  @Override
  public Observable<Recipient> remove(Recipient recipient) {
    return Observable.just(recipient)
      .doOnNext(new Action1<Recipient>() {
        @Override
        public void call(Recipient recipient) {
          final String recipientKey = getRecipientKey(recipient);
          if (indexSet.contains(recipientKey)) {
            indexSet.remove(recipientKey);
          }
          sharedPreferences.edit()
            .putStringSet(KEY_INDEX, indexSet)
            .remove(recipientKey)
            .apply();
        }
      });
  }

  @Override
  public void clear() {
    indexSet.clear();
    sharedPreferences.edit()
      .clear()
      .apply();
  }
}
