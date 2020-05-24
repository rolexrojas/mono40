package com.mono40.movil.app.ui.main.settings.help;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mono40.movil.company.bank.Bank;
import com.mono40.movil.company.bank.BankStore;
import com.mono40.movil.util.ObjectHelper;
import com.mono40.movil.util.StringHelper;

import io.reactivex.Observable;

public class HelpPresenter {

    static HelpPresenter create(BankStore bankStore) {
        return new HelpPresenter(bankStore);
    }

    private HelpPresenter(BankStore bankStore) {
        this.bankStore = ObjectHelper.checkNotNull(bankStore, "bankStore");
    }
    private final BankStore bankStore;

    private boolean isNameContained(Bank bank, String query) {
        final String name = bank.name()
                .toUpperCase();
        return StringHelper.isNullOrEmpty(query) || name.contains(query.toUpperCase());
    }

    @NonNull
    protected Observable<Object> search(@Nullable final String query) {
        return this.bankStore.getAll()
                .flatMapObservable(Observable::fromIterable)
                .filter((bank) -> isNameContained(bank, query))
                .cast(Object.class);
    }
}
