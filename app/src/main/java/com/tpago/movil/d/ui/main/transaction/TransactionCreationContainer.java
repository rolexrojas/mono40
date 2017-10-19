package com.tpago.movil.d.ui.main.transaction;

import com.tpago.movil.d.ui.ChildFragment;
import com.tpago.movil.d.ui.Container;

/**
 * @author hecvasro
 */
public interface TransactionCreationContainer extends Container<TransactionCreationComponent> {

  void setChildFragment(ChildFragment<TransactionCreationContainer> fragment);

  void finish(boolean succeeded, String transactionId);
}
