package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.api.ApiBridge;

import java.util.List;
import java.util.Set;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class DataLoader {
  private final NetworkHelper networkHelper;
  private final ApiBridge apiBridge;

  public DataLoader(@NonNull NetworkHelper networkHelper, @NonNull ApiBridge apiBridge) {
    this.networkHelper = networkHelper;
    this.apiBridge = apiBridge;
  }

  @NonNull
  public final Observable<Result<DomainCode, Void>> load() {
    return Observable.empty();
  }

  @NonNull
  public final Observable<Result<DomainCode, Set<Account>>> accounts() {
    return Observable.empty();
  }

  @NonNull
  public final Observable<Result<DomainCode, Set<Recipient>>> recipients() {
    return Observable.empty();
  }

  @NonNull
  public final Observable<Result<DomainCode, List<Transaction>>> recentTransactions() {
    return Observable.empty();
  }
}
