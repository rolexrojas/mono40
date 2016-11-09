package com.gbh.movil.data.api;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Account;
import com.gbh.movil.domain.Balance;
import com.gbh.movil.domain.Bank;
import com.gbh.movil.domain.SavingsAccount;
import com.gbh.movil.domain.CreditCard;
import com.gbh.movil.domain.InitialData;
import com.gbh.movil.domain.Recipient;
import com.gbh.movil.domain.Result;
import com.gbh.movil.domain.Transaction;
import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.domain.api.ApiCode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * @author hecvasro
 */
class FakeApiBridge implements ApiBridge {
  private final List<Transaction> transactions = new ArrayList<>();
  private final Map<Account, Balance> balances = new HashMap<>();
  private final Set<Account> accounts = new HashSet<>();
  private final Set<Bank> banks = new HashSet<>();
  private final Set<Recipient> recipients = new HashSet<>();

  FakeApiBridge() {
    Bank bank;
    Account account;
    Balance balance;
    bank = new Bank("BPD", "Banco Popular Dominicano");
    account = new SavingsAccount("1234", "DOP", bank, 5.0);
    accounts.add(account);
    balance = new Balance(87645, "87645.23");
    balances.put(account, balance);
    bank = new Bank("BANRESERVAS", "Banreservas");
    banks.add(bank);
    account = new CreditCard("2134", "USD", bank, 3.0);
    accounts.add(account);
    balance = new Balance(2645, "2645.49");
    balances.put(account, balance);
    bank = new Bank("BDI", "Banco BDI");
    banks.add(bank);
    account = new SavingsAccount("3124", "DOP", bank, 4.0);
    accounts.add(account);
    balance = new Balance(99, "99.12");
    balances.put(account, balance);
    final Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, 2016);
    calendar.set(Calendar.MONTH, Calendar.AUGUST);
    calendar.set(Calendar.DAY_OF_MONTH, 3);
    calendar.set(Calendar.HOUR, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    transactions.add(new Transaction("Transferencia", "Arturo Gaitan", calendar.getTimeInMillis(),
      Transaction.RequestType.CREDIT, "USD", 100));
    transactions.add(new Transaction("Recarga", "809-586-5832", calendar.getTimeInMillis(),
      Transaction.RequestType.DEBIT, "DOP", 340));
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    transactions.add(new Transaction("Pago de factura", "Orange", calendar.getTimeInMillis(),
      Transaction.RequestType.DEBIT, "DOP", 4700));
    calendar.set(Calendar.MONTH, Calendar.JULY);
    calendar.set(Calendar.DAY_OF_MONTH, 29);
    transactions.add(new Transaction("Pago en tienda", "Farmacia Plus", calendar.getTimeInMillis(),
      Transaction.RequestType.DEBIT, "DOP", 1230.77));
  }

  @NonNull
  @Override
  public Observable<Result<ApiCode, Set<Bank>>> banks() {
    return Observable.just(Result.create(ApiCode.OK, banks))
      .delay(2L, TimeUnit.SECONDS);
  }

  @NonNull
  @Override
  public Observable<Result<ApiCode, InitialData>> initialLoad() {
    return Observable.just(Result.create(ApiCode.OK, new InitialData(accounts, recipients)))
      .delay(2L, TimeUnit.SECONDS);
  }

  @NonNull
  @Override
  public Observable<Result<ApiCode, Set<Account>>> accounts() {
    return Observable.just(Result.create(ApiCode.OK, accounts))
      .delay(2L, TimeUnit.SECONDS);
  }

  @NonNull
  @Override
  public Observable<Result<ApiCode, Balance>> queryBalance(@NonNull Account account,
    @NonNull String pin) {
    final Observable<Result<ApiCode, Balance>> observable;
    if (Integer.parseInt(pin) % 2 == 0) {
      if (balances.containsKey(account)) {
        observable = Observable.just(Result.create(ApiCode.OK, balances.get(account)));
      } else {
        observable = Observable.just(Result.<ApiCode, Balance>create(ApiCode.NOT_FOUND));
      }
    } else {
      observable = Observable.just(Result.<ApiCode, Balance>create(ApiCode.UNAUTHORIZED));
    }
    return observable.delay(2L, TimeUnit.SECONDS);
  }

  @NonNull
  @Override
  public Observable<Result<ApiCode, Set<Recipient>>> recipients() {
    return Observable.just(Result.create(ApiCode.OK, recipients))
      .delay(2L, TimeUnit.SECONDS);
  }

  @NonNull
  @Override
  public Observable<Result<ApiCode, List<Transaction>>> recentTransactions() {
    return Observable.just(Result.create(ApiCode.OK, transactions))
      .delay(2L, TimeUnit.SECONDS);
  }
}
