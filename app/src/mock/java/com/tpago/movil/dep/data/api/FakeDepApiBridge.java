package com.tpago.movil.d.data.api;

import android.support.annotation.NonNull;

import com.tpago.movil.Bank;
import com.tpago.movil.Partner;
import com.tpago.movil.d.domain.AccountBalance;
import com.tpago.movil.d.domain.Balance;
import com.tpago.movil.d.domain.BillRecipient;
import com.tpago.movil.d.domain.CreditCardBalance;
import com.tpago.movil.d.domain.InitialData;
import com.tpago.movil.d.domain.LoanBalance;
import com.tpago.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.d.domain.PhoneNumberRecipient;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductCreator;
import com.tpago.movil.d.domain.ProductType;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.Transaction;
import com.tpago.movil.d.domain.api.ApiCode;
import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.api.DepApiBridge;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * @author hecvasro
 */
final class FakeDepApiBridge implements DepApiBridge {
  private final List<Bank> bankList = new ArrayList<>();
  private final List<Product> productList = new ArrayList<>();
  private final List<Partner> partnerList = new ArrayList<>();
  private final List<Recipient> recipientList = new ArrayList<>();

  private final InitialData initialData;

  private final Random random = new Random();

  FakeDepApiBridge() {
    Bank bk;
    Product pt;
    bk = Bank.create(38, "ADEMI", "Banco ADEMI", "");
    bankList.add(bk);
    pt = ProductCreator
      .create(ProductType.SAV, "1000", "1000", bk, "RD$", BigDecimal.ONE, true, false);
    productList.add(pt);
    bk = Bank.create(44, "ADOPEM", "Banco ADOPEM", "");
    bankList.add(bk);
    pt = ProductCreator
      .create(ProductType.CC, "1001", "1001", bk, "RD$", BigDecimal.ONE, true, false);
    productList.add(pt);
    bk = Bank.create(5, "BPD", "Banco Popular Dominicano", "");
    bankList.add(bk);
    pt = ProductCreator
      .create(ProductType.SAV, "1002", "1002", bk, "RD$", BigDecimal.ONE, true, false);
    productList.add(pt);
    pt = ProductCreator
      .create(ProductType.CC, "1003", "1003", bk, "RD$", BigDecimal.ONE, true, false);
    productList.add(pt);
    bk = Bank.create(24, "BDP", "Banco del Progreso", "");
    bankList.add(bk);
    pt = ProductCreator
      .create(ProductType.AMEX, "1004", "1004", bk, "RD$", BigDecimal.ONE, true, false);
    productList.add(pt);

    Partner pr;
    pr = Partner.create(48, "LDS", "Leidsa", "");
    partnerList.add(pr);
    pr = Partner.create(46, "BYA", "BOYA", "");
    partnerList.add(pr);
    pr = Partner.create(3, "EEL", "EDEESTE", "");
    partnerList.add(pr);
    pr = Partner.create(22, "ENL", "EDENORTE", "");
    partnerList.add(pr);
    pr = Partner.create(23, "ESL", "EDESUR", "");
    partnerList.add(pr);
    pr = Partner.create(33, "WIN", "Wind", "");
    partnerList.add(pr);
    pr = Partner.create(7, "200", "CODETEL/Claro", "");
    partnerList.add(pr);
    pr = Partner.create(27, "ORL", "Orange", "");
    partnerList.add(pr);
    pr = Partner.create(43, "SPR", "Seguros Sura", "");
    partnerList.add(pr);

    Recipient rt;
    rt = new PhoneNumberRecipient("8098829887", "Hector Vasquez");
    recipientList.add(rt);
    rt = new NonAffiliatedPhoneNumberRecipient("8092817626", "Luis Ruiz", bk, "2000");
    recipientList.add(rt);
    rt = new BillRecipient(pr, "AUTO-000");
    recipientList.add(rt);

    initialData = new InitialData(productList, recipientList);
  }

  @NonNull
  @Override
  public Observable<ApiResult<List<Bank>>> banks(@NonNull String authToken) {
    return Observable.just(new ApiResult<>(ApiCode.OK, bankList, null))
      .delay(1L, TimeUnit.SECONDS);
  }

  @NonNull
  @Override
  public Observable<ApiResult<InitialData>> initialLoad(@NonNull String authToken) {
    return Observable.just(new ApiResult<>(ApiCode.OK, initialData, null))
      .delay(1L, TimeUnit.SECONDS);
  }

  @NonNull
  @Override
  public Observable<ApiResult<Balance>> queryBalance(
    @NonNull String authToken,
    @NonNull Product product,
    @NonNull String pin) {
    final ProductCategory category = product.getCategory();
    final Balance balance;
    if (category.equals(ProductCategory.ACCOUNT)) {
      balance = new AccountBalance(
        BigDecimal.valueOf(654321 * random.nextDouble()),
        BigDecimal.valueOf(654321 * random.nextDouble()));
    } else if (category.equals(ProductCategory.CREDIT_CARD)) {
      balance = new CreditCardBalance(
        BigDecimal.valueOf(654321 * random.nextDouble()),
        BigDecimal.valueOf(654321 * random.nextDouble()));
    } else {
      balance = new LoanBalance(
        BigDecimal.valueOf(654321 * random.nextDouble()),
        BigDecimal.valueOf(654321 * random.nextDouble()));
    }
    return Observable.just(new ApiResult<>(ApiCode.OK, balance, null))
      .delay(1L, TimeUnit.SECONDS);
  }

  @NonNull
  @Override
  public Observable<ApiResult<List<Transaction>>> recentTransactions(@NonNull String authToken) {
    return Observable.error(new UnsupportedOperationException("Not implemented"));
  }

  @NonNull
  @Override
  public Observable<ApiResult<List<Recipient>>> recipients(@NonNull String authToken) {
    return Observable.just(new ApiResult<>(ApiCode.OK, recipientList, null))
      .delay(1L, TimeUnit.SECONDS);
  }

  @NonNull
  @Override
  public Observable<ApiResult<Boolean>> checkIfAffiliated(
    @NonNull String authToken,
    @NonNull String phoneNumber) {
    final int v = Integer.parseInt(Character.toString(phoneNumber.charAt(phoneNumber.length() - 1)));
    return Observable.just(new ApiResult<>(ApiCode.OK, v % 2 == 0, null))
      .delay(1L, TimeUnit.SECONDS);
  }

  @NonNull
  @Override
  public Observable<ApiResult<String>> transferTo(
    @NonNull String authToken,
    @NonNull Product product,
    @NonNull Recipient recipient,
    @NonNull BigDecimal amount,
    @NonNull String pin) {
    return Observable.just(new ApiResult<>(
      ApiCode.OK,
      Long.toString(System.currentTimeMillis()),
      null))
      .delay(1L, TimeUnit.SECONDS);
  }

  @NonNull
  @Override
  public Observable<ApiResult<Void>> setDefaultPaymentOption(
    @NonNull String authToken,
    @NonNull Product product) {
    return Observable.just(new ApiResult<Void>(ApiCode.OK, null, null))
      .delay(1L, TimeUnit.SECONDS);
  }

  @Override
  public Observable<ApiResult<Void>> checkAccountNumber(
    String authToken,
    Bank bank,
    String accountNumber) {
    return Observable.just(new ApiResult<Void>(ApiCode.OK, null, null))
      .delay(1L, TimeUnit.SECONDS);
  }

  @Override
  public Observable<ApiResult<List<Partner>>> partners(String authToken) {
    return Observable.just(new ApiResult<>(ApiCode.OK, partnerList, null))
      .delay(1L, TimeUnit.SECONDS);
  }

  @Override
  public Observable<ApiResult<BillRecipient>> addBill(
    String authToken,
    Partner partner,
    String contractNumber) {
    return Observable.just(new ApiResult<>(
      ApiCode.OK,
      new BillRecipient(partner, contractNumber),
      null))
      .delay(1L, TimeUnit.SECONDS);
  }
}
