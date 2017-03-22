package com.tpago.movil.d.data.repo;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.tpago.movil.d.misc.rx.RxUtils;
import com.tpago.movil.d.domain.Transaction;
import com.tpago.movil.d.domain.TransactionRepo;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * {@link TransactionRepo Transaction repository} implementation that uses memory as storage.
 *
 * @author hecvasro
 */
class SharedPreferencesTransactionRepo implements TransactionRepo {
  private static final String KEY_INDEX = "index";

  private final SharedPreferences sharedPreferences;
  private final Gson gson;

  private final Set<String> indexSet;

  SharedPreferencesTransactionRepo(SharedPreferences sharedPreferences, Gson gson) {
    this.sharedPreferences = sharedPreferences;
    this.gson = gson;
    this.indexSet = this.sharedPreferences.getStringSet(KEY_INDEX, new HashSet<String>());
  }

  private static String getTransactionKey(Transaction transaction) {
    return Integer.toString(transaction.hashCode());
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<List<Transaction>> saveAll(@NonNull List<Transaction> transactionsToSave) {
    return Observable.just(transactionsToSave)
      .doOnNext(new Action1<List<Transaction>>() {
        @Override
        public void call(List<Transaction> transactions) {
          SharedPreferences.Editor editor;
          editor = sharedPreferences.edit();
          for (String transactionKey : indexSet) {
            editor.remove(transactionKey);
          }
          editor.apply();
          indexSet.clear();
          String transactionKey;
          editor = sharedPreferences.edit();
          for (Transaction t : transactions) {
            transactionKey = getTransactionKey(t);
            indexSet.add(transactionKey);
            editor.putString(transactionKey, gson.toJson(t));
          }
          editor.putStringSet(KEY_INDEX, indexSet);
          editor.apply();
        }
      })
      .compose(RxUtils.<Transaction>fromCollection())
      .toSortedList(new Func2<Transaction, Transaction, Integer>() {
        @Override
        public Integer call(Transaction a, Transaction b) {
          return a.getDate() > b.getDate() ? -1 : (a.getDate() == b.getDate() ? 0 : 1);
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

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<List<Transaction>> getAll() {
    return Observable.from(indexSet)
      .map(new Func1<String, Transaction>() {
        @Override
        public Transaction call(String transactionKey) {
          return gson.fromJson(sharedPreferences.getString(transactionKey, null), Transaction.class);
        }
      })
      .toSortedList(new Func2<Transaction, Transaction, Integer>() {
        @Override
        public Integer call(Transaction a, Transaction b) {
          return a.getDate() > b.getDate() ? -1 : (a.getDate() == b.getDate() ? 0 : 1);
        }
      });
  }
}
