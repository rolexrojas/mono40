package com.mono40.movil.d.domain.api;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.mono40.movil.PhoneNumber;
import com.mono40.movil.company.bank.Bank;
import com.mono40.movil.company.partner.Partner;
import com.mono40.movil.d.data.api.CustomerSecretKey;
import com.mono40.movil.d.data.api.CustomerSecretTokenResponse;
import com.mono40.movil.d.domain.Balance;
import com.mono40.movil.d.domain.BillBalance;
import com.mono40.movil.d.domain.BillRecipient;
import com.mono40.movil.d.domain.CreditCardBillBalance;
import com.mono40.movil.d.domain.Customer;
import com.mono40.movil.d.domain.InitialData;
import com.mono40.movil.d.domain.LoanBillBalance;
import com.mono40.movil.d.domain.PaymentResult;
import com.mono40.movil.d.domain.Product;
import com.mono40.movil.d.domain.ProductBillBalance;
import com.mono40.movil.d.domain.ProductRecipient;
import com.mono40.movil.d.domain.Recipient;
import com.mono40.movil.d.domain.Transaction;

import java.math.BigDecimal;
import java.util.List;

import rx.Observable;

/**
 * @author hecvasro
 * @deprecated Use {@link com.mono40.movil.api.Api} instead.
 */
@Deprecated
public interface DepApiBridge {

    @NonNull
    Observable<ApiResult<InitialData>> initialLoad();

    Observable<ApiResult<CustomerSecretTokenResponse>> fetchCustomerSecretToken();

    Observable<ApiResult<CustomerSecretKey>> fetchCustomerSecretKey();

    @NonNull
    ApiResult<Balance> queryBalance(Product product, String pin);

    @NonNull
    Observable<ApiResult<List<Transaction>>> recentTransactions();

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

    Observable<ApiResult<String>> payToMerchant(
            Product product,
            Recipient recipient,
            BigDecimal amount,
            String pin
    );

    ApiResult<Void> setDefaultPaymentOption(Product paymentOption);

    Observable<ApiResult<Pair<String, Product>>> checkAccountNumber(
            Bank bank,
            String accountNumber
    );

    Observable<ApiResult<Pair<String, Product>>> checkAccountNumber(
            Bank bank,
            String accountNumber,
            String type
    );

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

    Observable<ApiResult<Void>> activatePurchaseWithoutNfc(
            Product fundingProduct,
            String pin
    );

}
