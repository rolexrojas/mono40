package com.mono40.movil.d.ui.main.transaction;

import com.mono40.movil.d.ui.ChildFragment;
import com.mono40.movil.d.ui.Container;

/**
 * @author hecvasro
 */
public interface TransactionCreationContainer extends Container<TransactionCreationComponent> {

  void setChildFragment(ChildFragment<TransactionCreationContainer> fragment);

  void finish(boolean succeeded, String transactionId);
}
