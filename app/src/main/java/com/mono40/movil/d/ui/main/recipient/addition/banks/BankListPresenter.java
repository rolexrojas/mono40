package com.mono40.movil.d.ui.main.recipient.addition.banks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mono40.movil.company.bank.Bank;
import com.mono40.movil.company.bank.BankStore;
import com.mono40.movil.d.ui.main.recipient.addition.RecipientCandidateListPresenter;
import com.mono40.movil.util.ObjectHelper;
import com.mono40.movil.util.StringHelper;

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
