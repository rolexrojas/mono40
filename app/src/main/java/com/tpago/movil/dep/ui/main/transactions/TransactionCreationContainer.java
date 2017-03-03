package com.tpago.movil.dep.ui.main.transactions;

import com.tpago.movil.dep.ui.ChildFragment;
import com.tpago.movil.dep.ui.Container;

/**
 * @author hecvasro
 */
public interface TransactionCreationContainer extends Container<TransactionCreationComponent> {
  void setChildFragment(ChildFragment<TransactionCreationContainer> fragment);
  void finish(boolean succeeded, String transactionId);
}
