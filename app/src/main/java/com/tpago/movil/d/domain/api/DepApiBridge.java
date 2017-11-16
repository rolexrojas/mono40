package com.tpago.movil.d.domain.api;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.tpago.movil.dep.Partner;
import com.tpago.movil.d.domain.Balance;
import com.tpago.movil.d.domain.CreditCardBillBalance;
import com.tpago.movil.d.domain.Customer;
import com.tpago.movil.d.domain.LoanBillBalance;
import com.tpago.movil.d.domain.PaymentResult;
import com.tpago.movil.d.domain.ProductBillBalance;
import com.tpago.movil.d.domain.ProductRecipient;
import com.tpago.movil.d.domain.Bank;
import com.tpago.movil.d.domain.BillBalance;
import com.tpago.movil.d.domain.BillRecipient;
import com.tpago.movil.d.domain.InitialData;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.Transaction;
import com.tpago.movil.PhoneNumber;

import java.math.BigDecimal;
import java.util.List;

import rx.Observable;

/**
 * @author hecvasro
 * @deprecated Use {@link com.tpago.movil.api.Api} instead.
 */
@Deprecated
public interface DepApiBridge {

  @NonNull
  Observable<ApiResult<InitialData>> initialLoad();

  @NonNull
  ApiResult<Balance> queryBalance(Product product, String pin);

  @NonNull
  Observable<ApiResult<List<Transaction>>> recentTransactions();

  @NonNull
  Observable<ApiResult<List<Recipient>>> recipients();

  @NonNull
  Observable<ApiResult<Boolean>> checkIfAffiliated(@NonNull String phoneNumber);

  Observable<ApiResult<String>> transferTo(
    Product product,
    Recipient recipient,
    BigDecimal amount,
    String pin
  );

  Observable<ApiResult<String>> transferTo(
    Product fundingProduct,
    Product destinationProduct,
    BigDecimal amount,
    String pin
  );

  ApiResult<Void> setDefaultPaymentOption(Product paymentOption);

  Observable<ApiResult<Pair<String, Product>>> checkAccountNumber(
    Bank bank,
    String accountNumber
  );

  Observable<ApiResult<List<Bank>>> banks();

  Observable<ApiResult<List<Partner>>> partners();

  Observable<ApiResult<Void>> addBill(
    Partner partner,
    String contractNumber,
    String pin
  );

  ApiResult<Void> removeBill(
    BillRecipient bill,
    String pin
  );

  ApiResult<BillBalance> queryBalance(BillRecipient recipient);

  ApiResult<ProductBillBalance> queryBalance(ProductRecipient recipient);

  Observable<ApiResult<String>> payBill(
    BillRecipient bill,
    Product fundingAccount,
    BillRecipient.Option option,
    String pin
  );

  Observable<ApiResult<PaymentResult>> payCreditCardBill(
    BigDecimal amountToPay,
    CreditCardBillBalance.Option option,
    String pin,
    Product fundingAccount,
    Product creditCard
  );

  Observable<ApiResult<PaymentResult>> payLoanBill(
    BigDecimal amountToPay,
    LoanBillBalance.Option option,
    String pin,
    Product fundingAccount,
    Product loan
  );

  ApiResult<Boolean> validatePin(String pin);

  ApiResult<Customer.State> fetchCustomerState(String phoneNumber);

  ApiResult<Customer> fetchCustomer(String phoneNumber);

  Observable<ApiResult<String>> recharge(
    Partner carrier,
    PhoneNumber phoneNumber,
    Product fundingAccount,
    BigDecimal amount,
    String pin
  );

  ApiResult<String> advanceCash(
    Product fundingAccount,
    Product recipientAccount,
    BigDecimal amount,
    String pin
  );
}
