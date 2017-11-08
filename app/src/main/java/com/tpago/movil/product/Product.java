package com.tpago.movil.product;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.tpago.movil.currency.Currency;
import com.tpago.movil.balance.AvailableBalance;
import com.tpago.movil.balance.BalanceStore;
import com.tpago.movil.balance.PayableBalance;
import com.tpago.movil.bank.Bank;

import java.math.BigDecimal;

/**
 * Product representation
 *
 * @author hecvasro
 */
@AutoValue
public abstract class Product {

  public static Builder builder() {
    throw new UnsupportedOperationException("not implemented");
  }

  private final BalanceStore<AvailableBalance> availableBalance;
  private final BalanceStore<PayableBalance> payableBalance;

  Product() {
    throw new UnsupportedOperationException("not implemented");
  }

  public abstract Bank bank();

  public abstract Type type();

  @Memoized
  public Category category() {
    throw new UnsupportedOperationException("not implemented");
  }

  public abstract String alias();

  public abstract String number();

  public abstract Currency currency();

  /**
   * Cost of querying its balance.
   */
  public abstract BigDecimal balanceQueryCost();

  /**
   * {@link AvailableBalance Balance} that holds the amount that it has available.
   */
  public final BalanceStore<AvailableBalance> availableBalance() {
    throw new UnsupportedOperationException("not implemented");
  }

  /**
   * {@link PayableBalance Balance} that holds the amount that it has pending for payment.
   */
  public final BalanceStore<PayableBalance> payableBalance() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Nullable
  public abstract String imageUriTemplate();

  @Nullable
  public abstract Uri imageUri();

  /**
   * Indicates whether it's a payment method or not.
   */
  public abstract boolean isPaymentMethod();

  @Memoized
  @Override
  public abstract String toString();

  @Memoized
  @Override
  public abstract int hashCode();

  /**
   * Product type enumeration
   */
  public enum Type {
    /**
     * American Express from any bank
     */
    AMEX,
    /**
     * Visa or MasterCard from any bank
     */
    CC,
    /**
     * Account from Citi Bank
     * <p>
     * Only accepts credit transactions.
     */
    CDA,
    /**
     * Debit direct account from any bank
     */
    DDA,
    /**
     * Loan from any bank
     */
    LOAN,
    /**
     * Prepay account from any bank
     * <p>
     * Wallets use these type.
     */
    PPA,
    /**
     * Savings account from any bank
     */
    SAV,
    /**
     * Savings account from Banco Union
     * <p>
     * Only accepts credit transactions.
     */
    SAVCLARO,
    /**
     * Savings account from Banco Union
     * <p>
     * Accepts credit and debit transactions.
     */
    SAVELLA,
    /**
     * Visa from Banco Union
     */
    TBD
  }

  /**
   * Product category enumeration
   */
  public enum Category {
    ACCOUNT,
    CREDIT_CARD,
    LOAN
  }

  @AutoValue.Builder
  public static abstract class Builder {

    Builder() {
    }

    public abstract Builder bank(Bank bank);

    public abstract Builder type(Type type);

    public abstract Builder alias(String alias);

    public abstract Builder number(String number);

    public abstract Builder currency(Currency currency);

    public abstract Builder balanceQueryCost(BigDecimal balanceQueryCost);

    public abstract Builder imageUriTemplate(@Nullable String imageUriTemplate);

    public abstract Builder imageUri(@Nullable Uri imageUri);

    public abstract Builder isPaymentMethod(boolean isPaymentMethod);

    public abstract Product build();
  }
}
