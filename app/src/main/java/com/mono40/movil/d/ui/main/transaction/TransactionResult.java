package com.mono40.movil.d.ui.main.transaction;

import com.mono40.movil.d.domain.Recipient;

public class TransactionResult {
    Recipient recipient;
    String transactionId;
    TransactionCategory transactionCategory;

    public TransactionResult(Recipient recipient, String transactionId, TransactionCategory transactionCategory) {
        this.recipient = recipient;
        this.transactionId = transactionId;
        this.transactionCategory = transactionCategory;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public TransactionCategory getTransactionCategory() {
        return transactionCategory;
    }
}
