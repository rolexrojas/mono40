package com.tpago.movil.d.domain;

import com.squareup.picasso.Picasso;
import com.tpago.movil.dep.api.ApiCode;
import com.tpago.movil.dep.api.ApiService;
import com.tpago.movil.dep.net.NetworkService;
import com.tpago.movil.dep.Sets;

import java.util.Set;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

import static com.tpago.movil.dep.Preconditions.assertNotNull;

/**
 * @author hecvasro
 */
@Deprecated
public final class BankProvider implements Provider<Bank> {
  private final BankRepo bankRepo;
  private final NetworkService networkService;
  private final ApiService apiService;
  private final Picasso picasso;

  private boolean fetchedSet = false;

  BankProvider(
    BankRepo bankRepo,
    NetworkService networkService,
    ApiService apiService,
    Picasso picasso) {
    this.bankRepo = assertNotNull(bankRepo, "bankRepo == null");
    this.networkService = assertNotNull(networkService, "networkService == null");
    this.apiService = assertNotNull(apiService, "apiService == null");
    this.picasso = assertNotNull(picasso, "picasso == null");
  }

  @Override
  public Observable<Result<Set<Bank>, ErrorCode>> getAll() {
    final Observable<Result<Set<Bank>, ErrorCode>> localObservable = Observable.defer(
      new Callable<ObservableSource<Result<Set<Bank>, ErrorCode>>>() {
        @Override
        public ObservableSource<Result<Set<Bank>, ErrorCode>> call() throws Exception {
          return Observable.just(Result.<Set<Bank>, ErrorCode>create(bankRepo.getAll()));
        }
      });
    if (fetchedSet) {
      return localObservable;
    } else {
      final Observable<Result<Set<Bank>, ErrorCode>> remoteObservable;
      if (networkService.checkIfAvailable()) {
        remoteObservable = apiService.fetchBankSet()
          .map(new Function<Result<Set<Bank>, ApiCode>, Result<Set<Bank>, ErrorCode>>() {
            @Override
            public Result<Set<Bank>, ErrorCode> apply(
              final Result<Set<Bank>, ApiCode> apiResult) throws Exception {
              final Result<Set<Bank>, ErrorCode> result;
              fetchedSet = apiResult.isSuccessful();
              if (fetchedSet) {
                final Set<Bank> bankSet = Sets.createSortedSet(apiResult.getSuccessData());
                bankRepo.saveAll(bankSet);
                for (Bank bank : bankSet) {
                  picasso.load(bank.getLogoUri(LogoStyle.GRAY_20)).fetch();
                  picasso.load(bank.getLogoUri(LogoStyle.GRAY_36)).fetch();
                  picasso.load(bank.getLogoUri(LogoStyle.PRIMARY_24)).fetch();
                  picasso.load(bank.getLogoUri(LogoStyle.WHITE_36)).fetch();
                }
                result = Result.create(bankSet);
              } else {
                result = Result.create(
                  FailureData.create(
                    ErrorCode.UNEXPECTED,
                    apiResult.getFailureData().getDescription()));
              }
              return result;
            }
          })
          .toObservable();
      } else {
        remoteObservable = Observable.just(
          Result.<Set<Bank>, ErrorCode>create(
            FailureData.create(ErrorCode.UNAVAILABLE_NETWORK)));
      }
      return localObservable.concatWith(remoteObservable);
    }
  }
}
