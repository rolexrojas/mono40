package com.tpago.movil.data.api;

import android.support.annotation.NonNull;

import com.tpago.movil.domain.Account;
import com.tpago.movil.domain.Balance;
import com.tpago.movil.domain.Bank;
import com.tpago.movil.domain.BankAccount;
import com.tpago.movil.domain.CreditCard;
import com.tpago.movil.domain.api.ApiBridge;
import com.tpago.movil.domain.api.ApiCode;
import com.tpago.movil.domain.api.ApiResult;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * @author hecvasro
 */
class FakeApiBridge implements ApiBridge {
  private final Set<Bank> banks = new HashSet<>();
  private final Set<Account> accounts = new HashSet<>();
  private final Map<Account, Balance> balances = new HashMap<>();

  FakeApiBridge() {
    Bank bank;
    Account account;
    Balance balance;
    bank = new Bank("BPD", "Banco Popular Dominicano");
    account = new BankAccount("1234", "DOP", bank, 5.0, "5.0", "balance");
    accounts.add(account);
    balance = new Balance(87645, "87645");
    balances.put(account, balance);
    bank = new Bank("BANRESERVAS", "Banreservas");
    banks.add(bank);
    account = new CreditCard("2134", "DOP", bank, 3.0, "3.0", "balance");
    accounts.add(account);
    balance = new Balance(2645, "2645");
    balances.put(account, balance);
    bank = new Bank("BDI", "Banco BDI");
    banks.add(bank);
    account = new BankAccount("3124", "USD", bank, 4.0, "4.0", "balance");
    accounts.add(account);
    balance = new Balance(99, "99");
    balances.put(account, balance);
//    bank = new Bank("ADEMI", "Banco ADEMI");
//    banks.add(bank);
//    account = new CreditCard("4123", "DOP", bank, 12.0, "12.0", "balance");
//    accounts.add(account);
//    balance = new Balance(937461238, "937461238");
//    balances.put(account, balance);
//    bank = new Bank("SCOTIABANK", "Scotiabank");
//    banks.add(bank);
//    account = new BankAccount("4321", "USD", bank, 1.0, "1.0", "balance");
//    accounts.add(account);
//    balance = new Balance(736144, "736144");
//    balances.put(account, balance);
  }

  @NonNull
  @Override
  public Observable<ApiResult<Set<Bank>>> getAllBanks() {
    return Observable.just(new ApiResult<>(ApiCode.SUCCESS, banks))
      .delay(1L, TimeUnit.SECONDS);
  }

  @NonNull
  @Override
  public Observable<ApiResult<Set<Account>>> getAllAccounts() {
    return Observable.just(new ApiResult<>(ApiCode.SUCCESS, accounts))
      .delay(1L, TimeUnit.SECONDS);
  }

  @NonNull
  @Override
  public Observable<ApiResult<Balance>> queryBalance(@NonNull Account account,
    @NonNull String pin) {
    if (balances.containsKey(account)) {
      return Observable.just(new ApiResult<>(ApiCode.SUCCESS, balances.get(account)))
        .delay(1L, TimeUnit.SECONDS);
    } else {
      return Observable.just(new ApiResult<Balance>(ApiCode.NOT_FOUND, null))
        .delay(1L, TimeUnit.SECONDS);
    }
  }
}
