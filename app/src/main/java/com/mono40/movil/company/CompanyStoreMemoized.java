package com.mono40.movil.company;

import com.mono40.movil.company.bank.Bank;
import com.mono40.movil.util.ObjectHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * @author hecvasro
 */
public final class CompanyStoreMemoized<T extends Company> implements CompanyStore<T> {

  public static <T extends Company> CompanyStoreMemoized<T> create() {
    return new CompanyStoreMemoized<>();
  }

  private final AtomicReference<List<T>> companies = new AtomicReference<>();

  private CompanyStoreMemoized() {
  }

  @Override
  public Completable sync(List<T> companies) {
    ObjectHelper.checkNotNull(companies, "companies");
    return Observable.fromIterable(companies)
      .toSortedList(Company::compareTo)
      .doOnSuccess(this.companies::set)
      .toCompletable();
  }

  @Override
  public Maybe<List<T>> getAll() {
    return Maybe.defer(() -> {
      final List<T> reference = this.companies.get();
      if (ObjectHelper.isNull(reference)) {
        return Maybe.empty();
      }
      List<T> filteredBanks =  new ArrayList<>();
      for (Company bank: reference) {
        if (!bank.id().equalsIgnoreCase("DKT") && !bank.id().equalsIgnoreCase("CTB")) {
          filteredBanks.add((T)bank);
        }
      }
      return Maybe.just(filteredBanks);
    });
  }

  @Override
  public Maybe<T> findByCode(int code) {
    return this.getAll()
      .flatMapObservable(Observable::fromIterable)
      .filter((company) -> company.code() == code)
      .firstElement();
  }

  @Override
  public Completable clear() {
    return Completable.fromAction(() -> this.companies.set(null));
  }
}
