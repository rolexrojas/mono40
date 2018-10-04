package com.tpago.movil.d.ui.main.recipient.addition.banks;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.company.bank.BankStore;
import com.tpago.movil.d.ui.main.recipient.addition.RecipientCandidateListPresenter;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

import io.reactivex.Observable;


/**
 * @author hecvasro
 */
final class BankListPresenter extends RecipientCandidateListPresenter {

  static BankListPresenter create(BankStore bankStore) {
    return new BankListPresenter(bankStore);
  }

  private final BankStore bankStore;

  private BankListPresenter(BankStore bankStore) {
    this.bankStore = ObjectHelper.checkNotNull(bankStore, "bankStore");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean canStartListeningQueryChangeEvents() {
    return true;
  }

  private boolean isNameContained(Bank bank, String query) {
    final String name = bank.name()
      .toUpperCase();
    return StringHelper.isNullOrEmpty(query) || name.contains(query.toUpperCase());
  }

  @NonNull
  @Override
  protected Observable<Object> search(@Nullable final String query) {
    return this.bankStore.getAll()
      .flatMapObservable(Observable::fromIterable)
      .filter((bank) -> isNameContained(bank, query))
      .cast(Object.class);
  }
}
