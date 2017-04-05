package com.tpago.movil.api;

import com.google.auto.value.AutoValue;
import com.tpago.movil.Account;
import com.tpago.movil.CreditCard;
import com.tpago.movil.Loan;
import com.tpago.movil.PaymentMethod;
import com.tpago.movil.Product;

import java.util.List;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class DInitialData {
  public static DInitialData create(
    List<Account> accountList,
    List<CreditCard> creditCardList,
    List<Loan> loanList,
    List<PaymentMethod<? extends Product>> paymentMethodList) {
    throw new UnsupportedOperationException("Not implemented");
  }

  public abstract List<Account> getAccountList();
  public abstract List<CreditCard> getCreditCardList();
  public abstract List<Loan> getLoanList();
  public abstract List<PaymentMethod<? extends Product>> getPaymentMethodList();
}
