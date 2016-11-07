package com.gbh.movil.domain;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.gbh.movil.Utils;
import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.domain.api.ApiCode;
import com.gbh.movil.domain.api.ApiUtils;

import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class DataLoader {
  private final NetworkHelper networkHelper;
  private final ApiBridge apiBridge;
  private final AccountRepo accountRepo;
  private final RecipientRepo recipientRepo;
  private final TransactionRepo transactionRepo;

  /**
   * Constructs a new data loader.
   */
  public DataLoader(@NonNull NetworkHelper networkHelper, @NonNull ApiBridge apiBridge,
    @NonNull AccountRepo accountRepo,
    @NonNull RecipientRepo recipientRepo,
    @NonNull TransactionRepo transactionRepo) {
    this.networkHelper = networkHelper;
    this.apiBridge = apiBridge;
    this.accountRepo = accountRepo;
    this.recipientRepo = recipientRepo;
    this.transactionRepo = transactionRepo;
  }

  /**
   * Creates an {@link Observable observable} that loads all the data required for initialization
   * and emits a {@link Pair pair} of {@link Boolean booleans} that indicates if there're any
   * additions and/or removals compared to what was already saved locally.
   * <p>
   * <em>Note:</em> By default {@link #load()} operates on {@link Schedulers#io()}.
   *
   * @return An {@link Observable observable} that loads all the data required for initialization
   * and emits a {@link Pair pair} of {@link Boolean booleans} that indicates if there're any
   * additions and/or removals compared to what was already saved locally.
   */
  @NonNull
  public final Observable<Result<DomainCode, Pair<Boolean, Boolean>>> load() {
    return apiBridge.initialLoad()
      .flatMap(new Func1<Result<ApiCode, InitialData>, Observable<Result<DomainCode,
        Pair<Boolean, Boolean>>>>() {
        @Override
        public Observable<Result<DomainCode, Pair<Boolean, Boolean>>> call(Result<ApiCode,
          InitialData> apiResult) {
          if (ApiUtils.isSuccessful(apiResult)) {
            final InitialData data = apiResult.getData();
            if (Utils.isNotNull(data)) {
              return accountRepo.saveAll(data.getAccounts())
                .zipWith(recipientRepo.saveAll(data.getRecipients()),
                  new Func2<Pair<Set<Account>, Pair<Boolean, Boolean>>, Set<Recipient>,
                    Result<DomainCode, Pair<Boolean, Boolean>>>() {
                    @Override
                    public Result<DomainCode, Pair<Boolean, Boolean>> call(Pair<Set<Account>,
                      Pair<Boolean, Boolean>> pair, Set<Recipient> recipients) {
                      // TODO: Dispatch account additions and/or removals notifications.
                      return Result.create(DomainCode.SUCCESSFUL, pair.second);
                    }
                  });
            } else {
              return Observable.just(Result.<DomainCode, Pair<Boolean, Boolean>>create(
                DomainCode.FAILURE_UNKNOWN));
            }
          } else {
            return Observable.just(Result.<DomainCode, Pair<Boolean, Boolean>>create(
              DomainCode.FAILURE_UNKNOWN));
          }
        }
      })
      .compose(DomainUtils.<Pair<Boolean, Boolean>>assertNetwork(networkHelper))
      .subscribeOn(Schedulers.io());
  }

  /**
   * Creates an {@link Observable observable} that emits all the {@link Account accounts} saved
   * locally and remotely respectively.
   * <p>
   * <em>Note:</em> By default {@link #accounts()} operates on {@link Schedulers#io()}.
   *
   * @return An {@link Observable observable} that emits all the {@link Account accounts} saved
   * locally and remotely respectively.
   */
  @NonNull
  public final Observable<Result<DomainCode, Set<Account>>> accounts() {
    return accountRepo.getAll()
      .map(new Func1<Set<Account>, Result<DomainCode, Set<Account>>>() {
        @Override
        public Result<DomainCode, Set<Account>> call(Set<Account> accounts) {
          return Result.create(DomainCode.SUCCESSFUL, accounts);
        }
      })
      .concatWith(apiBridge.accounts()
        .flatMap(new Func1<Result<ApiCode, Set<Account>>, Observable<Result<DomainCode,
          Set<Account>>>>() {
          @Override
          public Observable<Result<DomainCode, Set<Account>>> call(Result<ApiCode,
            Set<Account>> apiResult) {
            if (ApiUtils.isSuccessful(apiResult)) {
              final Set<Account> accounts = apiResult.getData();
              if (Utils.isNotNull(accounts)) {
                return accountRepo.saveAll(accounts)
                  .map(new Func1<Pair<Set<Account>, Pair<Boolean, Boolean>>, Result<DomainCode,
                    Set<Account>>>() {
                    @Override
                    public Result<DomainCode, Set<Account>> call(Pair<Set<Account>, Pair<Boolean,
                      Boolean>> pair) {
                      // TODO: Dispatch account additions and/or removals notifications.
                      return Result.create(DomainCode.SUCCESSFUL, pair.first);
                    }
                  });
              } else {
                return Observable.just(Result.<DomainCode, Set<Account>>create(
                  DomainCode.FAILURE_UNKNOWN));
              }
            } else {
              return Observable.just(Result.<DomainCode, Set<Account>>create(
                DomainCode.FAILURE_UNKNOWN));
            }
          }
        }).compose(DomainUtils.<Set<Account>>assertNetwork(networkHelper)))
      .subscribeOn(Schedulers.io());
  }

  /**
   * Creates an {@link Observable observable} that emits all the {@link Recipient recipients} saved
   * locally and remotely.
   * <p>
   * <em>Note:</em> By default {@link #recipients()} operates on {@link Schedulers#io()}.
   *
   * @return An {@link Observable observable} that emits all the {@link Recipient recipients} saved
   * locally and remotely.
   */
  @NonNull
  public final Observable<Result<DomainCode, Set<Recipient>>> recipients() {
    return recipientRepo.getAll()
      .map(new Func1<Set<Recipient>, Result<DomainCode, Set<Recipient>>>() {
        @Override
        public Result<DomainCode, Set<Recipient>> call(Set<Recipient> recipients) {
          return Result.create(DomainCode.SUCCESSFUL, recipients);
        }
      })
      .concatWith(apiBridge.recipients()
        .flatMap(new Func1<Result<ApiCode, Set<Recipient>>, Observable<Result<DomainCode,
          Set<Recipient>>>>() {
          @Override
          public Observable<Result<DomainCode, Set<Recipient>>> call(Result<ApiCode, Set<Recipient>>
            apiResult) {
            if (ApiUtils.isSuccessful(apiResult)) {
              final Set<Recipient> recipients = apiResult.getData();
              if (Utils.isNotNull(recipients)) {
                return recipientRepo.saveAll(recipients)
                  .map(new Func1<Set<Recipient>, Result<DomainCode, Set<Recipient>>>() {
                    @Override
                    public Result<DomainCode, Set<Recipient>> call(Set<Recipient> recipients) {
                      return Result.create(DomainCode.SUCCESSFUL, recipients);
                    }
                  });
              } else {
                return Observable.just(Result.<DomainCode, Set<Recipient>>create(
                  DomainCode.FAILURE_UNKNOWN));
              }
            } else {
              return Observable.just(Result.<DomainCode, Set<Recipient>>create(
                DomainCode.FAILURE_UNKNOWN));
            }
          }
        }).compose(DomainUtils.<Set<Recipient>>assertNetwork(networkHelper)))
      .subscribeOn(Schedulers.io());
  }

  /**
   * Creates an {@link Observable observable} that emits the latest {@link Transaction transactions}
   * made that are saved locally and remotely ordered from newer to older.
   * <p>
   * <em>Note:</em> By default {@link #recentTransactions()} operates on {@link Schedulers#io()}.
   *
   * @return An {@link Observable observable} that emits the latest {@link Transaction transactions}
   * made that are saved locally and remotely ordered from newer to older.
   */
  @NonNull
  public final Observable<Result<DomainCode, List<Transaction>>> recentTransactions() {
    return transactionRepo.getAll()
      .map(new Func1<List<Transaction>, Result<DomainCode, List<Transaction>>>() {
        @Override
        public Result<DomainCode, List<Transaction>> call(List<Transaction> transactions) {
          return Result.create(DomainCode.SUCCESSFUL, transactions);
        }
      })
      .concatWith(apiBridge.recentTransactions()
        .flatMap(new Func1<Result<ApiCode, List<Transaction>>, Observable<Result<DomainCode,
          List<Transaction>>>>() {
          @Override
          public Observable<Result<DomainCode, List<Transaction>>> call(Result<ApiCode,
            List<Transaction>> apiResult) {
            if (ApiUtils.isSuccessful(apiResult)) {
              final List<Transaction> transactions = apiResult.getData();
              if (Utils.isNotNull(transactions)) {
                return transactionRepo.saveAll(transactions)
                  .map(new Func1<List<Transaction>, Result<DomainCode, List<Transaction>>>() {
                    @Override
                    public Result<DomainCode, List<Transaction>> call(List<Transaction>
                      transactions) {
                      return Result.create(DomainCode.SUCCESSFUL, transactions);
                    }
                  });
              } else {
                return Observable.just(Result.<DomainCode, List<Transaction>>create(
                  DomainCode.FAILURE_UNKNOWN));
              }
            } else {
              return Observable.just(Result.<DomainCode, List<Transaction>>create(
                DomainCode.FAILURE_UNKNOWN));
            }
          }
        }).compose(DomainUtils.<List<Transaction>>assertNetwork(networkHelper)))
      .subscribeOn(Schedulers.io());
  }
}
