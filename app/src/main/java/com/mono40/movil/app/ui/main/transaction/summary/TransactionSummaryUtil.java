package com.mono40.movil.app.ui.main.transaction.summary;

import android.content.Intent;

import com.mono40.movil.transaction.TransactionSummary;
import com.mono40.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
public final class TransactionSummaryUtil {

  private static final String KEY = TransactionSummary.class.getCanonicalName();

  public static Intent wrap(TransactionSummary transactionSummary) {
    ObjectHelper.checkNotNull(transactionSummary, "transactionSummary");
    final Intent intent = new Intent();
    intent.putExtra(KEY, transactionSummary);
    return intent;
  }

  public static TransactionSummary unwrap(Intent intent) {
    return ObjectHelper.checkNotNull(intent, "intent")
      .getParcelableExtra(KEY);
  }

  private TransactionSummaryUtil() {
  }
}
