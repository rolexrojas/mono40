package com.gbh.movil.ui.main.payments.transactions.contacts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gbh.movil.R;
import com.gbh.movil.ui.SubFragment;
import com.gbh.movil.ui.main.payments.transactions.TransactionCreationContainer;

/**
 * TODO
 *
 * @author hecvasro
 */
public class ContactTransactionCreationFragment extends SubFragment<TransactionCreationContainer> {
  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public static ContactTransactionCreationFragment newInstance() {
    return new ContactTransactionCreationFragment();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_transaction_creation_phone_number, container, false);
  }
}
