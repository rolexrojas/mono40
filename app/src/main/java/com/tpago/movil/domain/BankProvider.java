package com.tpago.movil.domain;

import com.tpago.movil.domain.api.ApiService;

import java.util.Set;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;

import static com.tpago.movil.util.Preconditions.assertNotNull;

/**
 * @author hecvasro
 */
public final class BankProvider implements Provider<Bank> {
  private final BankRepo bankRepo;
  private final NetworkService networkService;
  private final ApiService apiService;

  private boolean fetchedSet = false;

  BankProvider(BankRepo bankRepo, NetworkService networkService, ApiService apiService) {
    this.bankRepo = assertNotNull(bankRepo, "bankRepo == null");
    this.networkService = assertNotNull(networkService, "networkService == null");
    this.apiService = assertNotNull(apiService, "apiService == null");
  }

  @Override
  public Observable<Result<Set<Bank>, ProviderCode>> getAll() {
    final Observable<Result<Set<Bank>, ProviderCode>> localObservable = Observable
      .defer(new Callable<ObservableSource<Result<Set<Bank>, ProviderCode>>>() {
        @Override
        public ObservableSource<Result<Set<Bank>, ProviderCode>> call() throws Exception {
          return Observable.just(Result.<Set<Bank>, ProviderCode>create(bankRepo.getAll()));
        }
      });
    if (fetchedSet) {
      return localObservable;
    } else {
      final Observable<Result<Set<Bank>, ProviderCode>> remoteObservable = Observable
        .defer(new Callable<ObservableSource<Result<Set<Bank>, ProviderCode>>>() {
        @Override
        public ObservableSource<Result<Set<Bank>, ProviderCode>> call() throws Exception {
          if (networkService.checkIfAvailable()) {
            return apiService.fetchBankSet()
              .map(Providers.<Bank>resultMapper())
              .doOnSuccess(new Consumer<Result<Set<Bank>, ProviderCode>>() {
                @Override
                public void accept(Result<Set<Bank>, ProviderCode> result) throws Exception {
                  if (result.isSuccessful()) {
                    fetchedSet = true;
                    bankRepo.saveAll(result.getSuccessData());
                  }
                }
              })
              .toObservable();
          } else {
            final Result<Set<Bank>, ProviderCode> result = Result
              .create(FailureData.create(ProviderCode.UNAVAILABLE_NETWORK));
            return Observable.just(result);
          }
        }
      });
      return localObservable.concatWith(remoteObservable);
    }
  }
}
