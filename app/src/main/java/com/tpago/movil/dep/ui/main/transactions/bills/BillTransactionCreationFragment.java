package com.tpago.movil.dep.ui.main.transactions.bills;

import com.tpago.movil.dep.domain.Recipient;
import com.tpago.movil.dep.ui.ChildFragment;
import com.tpago.movil.dep.ui.main.transactions.TransactionCreationContainer;

import javax.inject.Inject;

/**
 * @author hecvasro
 */

public class BillTransactionCreationFragment
  extends ChildFragment<TransactionCreationContainer> {
  @Inject Recipient recipient;
}
