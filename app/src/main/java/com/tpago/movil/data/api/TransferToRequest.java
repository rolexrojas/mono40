package com.tpago.movil.data.api;

import android.support.annotation.NonNull;

import com.tpago.movil.domain.Bank;
import com.tpago.movil.domain.Product;
import com.tpago.movil.domain.Recipient;

import java.math.BigDecimal;

/**
 * TODO
 *
 * @author hecvasro
 */
class TransferToRequest {
  private String accountAlias;
  private String accountNumber;
  private String accountType;
  private Bank bank;
  private String currency;
  private String pin;
  private BigDecimal amount;
  private String recipientMsisdn;
  private String recipientName;

  public TransferToRequest(@NonNull Product product, @NonNull Recipient recipient,
    @NonNull BigDecimal amount, @NonNull String pin) {
    this.accountAlias = product.getAlias();
    this.accountNumber = product.getNumber();
    this.accountType = product.getType().name();
    this.bank = product.getBank();
    this.currency = product.getCurrency();
    this.pin = pin;
    this.amount = amount;
    this.recipientMsisdn = recipient.getIdentifier();
    this.recipientName = recipient.getLabel();
  }
}
