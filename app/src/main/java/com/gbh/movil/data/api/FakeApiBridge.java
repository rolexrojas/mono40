package com.gbh.movil.data.api;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Account;
import com.gbh.movil.domain.AccountBalance;
import com.gbh.movil.domain.Balance;
import com.gbh.movil.domain.Bank;
import com.gbh.movil.domain.CreditCard;
import com.gbh.movil.domain.CreditCardBalance;
import com.gbh.movil.domain.InitialData;
import com.gbh.movil.domain.LoanBalance;
import com.gbh.movil.domain.PhoneNumber;
import com.gbh.movil.domain.PhoneNumberRecipient;
import com.gbh.movil.domain.Product;
import com.gbh.movil.domain.ProductCreator;
import com.gbh.movil.domain.ProductIdentifier;
import com.gbh.movil.domain.Recipient;
import com.gbh.movil.domain.Transaction;
import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.domain.api.ApiCode;
import com.gbh.movil.domain.api.ApiResult;
import com.google.i18n.phonenumbers.NumberParseException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
class FakeApiBridge implements ApiBridge {
  private final Set<Bank> banks;
  private final Set<Product> products;
  private final Map<Product, Balance> balances;
  private final Set<Recipient> recipients;
  private final List<Transaction> transactions;

  private static int getLastDigit(@NonNull String s) {
    return Integer.parseInt(s.substring(s.length() - 1, s.length()));
  }

  private static boolean isLastDigitEven(@NonNull String s) {
    return getLastDigit(s) % 2 == 0;
  }

  FakeApiBridge() {
    banks = new HashSet<>();
    banks.add(new Bank(38, "ADEMI", "Banco Ademi"));
    banks.add(new Bank(44, "ADOPEM", "Banco Adopem"));
    banks.add(new Bank(35, "ALAVER", "Banco Alaver"));
    banks.add(new Bank(36, "BDI", "Banco BDI"));
    banks.add(new Bank(37, "LDH", "Banco Lopez de Haro"));
    banks.add(new Bank(5, "BPD", "Banco Popular"));
    banks.add(new Bank(24, "BDP", "Banco del Progreso"));
    banks.add(new Bank(4, "BR", "Banreservas"));
    banks.add(new Bank(45, "UNION", "Banco Union"));

    int i = 0;
    BigDecimal a;
    BigDecimal b;
    String alias;
    Product product;
    Balance balance;
    products = new HashSet<>();
    balances = new HashMap<>();
    final Random random = new Random();
    final String[] currencies = new String[] { "DOP", "USD" };
    final ProductIdentifier[] identifiers = ProductIdentifier.values();
    for (Bank bank : banks) {
      alias = Integer.toString((i + 1) * 1000);
      product = ProductCreator.create(identifiers[random.nextInt(identifiers.length)], alias,
        alias, bank, currencies[random.nextInt(currencies.length)],
        BigDecimal.valueOf(random.nextInt(11)), random.nextBoolean());
      products.add(product);
      a = BigDecimal.valueOf(random.nextInt(1000001));
      b = BigDecimal.valueOf(random.nextInt(1000001));
      if (product instanceof Account) {
        balance = new AccountBalance(a, b);
      } else if (product instanceof CreditCard) {
        balance = new CreditCardBalance(a, b);
      } else {
        balance = new LoanBalance(a, b);
      }
      balances.put(product, balance);
      i++;
    }

    recipients = new HashSet<>();
    try {
      recipients.add(new PhoneNumberRecipient(new PhoneNumber("8098829887"), "Hector Vasquez"));
      recipients.add(new PhoneNumberRecipient(new PhoneNumber("8092817621"), "Luis Miguel Ruiz"));
      recipients.add(new PhoneNumberRecipient(new PhoneNumber("8098853660"), null));
    } catch (NumberParseException exception) {
      Timber.e(exception);
    }

    transactions = new ArrayList<>();
    // TODO: Add transactions;
  }

  @NonNull
  private static <T> Observable.Transformer<T, T> delay() {
    return new Observable.Transformer<T, T>() {
      @Override
      public Observable<T> call(Observable<T> observable) {
        return observable.delay(1500L, TimeUnit.MILLISECONDS);
      }
    };
  }

  @NonNull
  @Override
  public Observable<ApiResult<Set<Bank>>> banks() {
    return Observable.just(ApiResult.create(banks))
      .compose(FakeApiBridge.<ApiResult<Set<Bank>>>delay());
  }

  @NonNull
  @Override
  public Observable<ApiResult<InitialData>> initialLoad() {
    return Observable.just(ApiResult.create(new InitialData(products, recipients)))
      .compose(FakeApiBridge.<ApiResult<InitialData>>delay());
  }

  @NonNull
  @Override
  public Observable<ApiResult<Balance>> queryBalance(@NonNull final Product product,
    @NonNull String pin) {
    return Observable.just(isLastDigitEven(pin))
      .map(new Func1<Boolean, ApiResult<Balance>>() {
        @Override
        public ApiResult<Balance> call(Boolean flag) {
          if (flag) {
            if (balances.containsKey(product)) {
              return ApiResult.create(balances.get(product));
            } else {
              return ApiResult.create(ApiCode.NOT_FOUND);
            }
          } else {
            return ApiResult.create(ApiCode.FORBIDDEN);
          }
        }
      })
      .compose(FakeApiBridge.<ApiResult<Balance>>delay());
  }

  @NonNull
  @Override
  public Observable<ApiResult<List<Transaction>>> recentTransactions() {
    return Observable.just(ApiResult.create(transactions))
      .compose(FakeApiBridge.<ApiResult<List<Transaction>>>delay());
  }

  @NonNull
  @Override
  public Observable<ApiResult<Set<Recipient>>> recipients() {
    return Observable.just(ApiResult.create(recipients))
      .compose(FakeApiBridge.<ApiResult<Set<Recipient>>>delay());
  }

  @NonNull
  @Override
  public Observable<ApiResult<Boolean>> checkIfAffiliated(@NonNull PhoneNumber phoneNumber) {
    return Observable.just(isLastDigitEven(phoneNumber.getContent()))
      .map(new Func1<Boolean, ApiResult<Boolean>>() {
        @Override
        public ApiResult<Boolean> call(Boolean flag) {
          return ApiResult.create(flag);
        }
      })
      .compose(FakeApiBridge.<ApiResult<Boolean>>delay());
  }

  @NonNull
  @Override
  public Observable<ApiResult<Boolean>> transferTo(@NonNull Product product,
    @NonNull Recipient recipient, @NonNull BigDecimal amount, @NonNull String pin) {
    return Observable.error(new UnsupportedOperationException());
  }
}
