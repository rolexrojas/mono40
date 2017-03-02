package com.tpago.movil.dep.ui.main.transactions.bills;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpago.movil.R;
import com.tpago.movil.dep.ui.ChildFragment;
import com.tpago.movil.dep.ui.main.transactions.TransactionCreationContainer;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */

public class BillTransactionCreationFragment
  extends ChildFragment<TransactionCreationContainer>
  implements BillTransactionCreationPresenter.View {
  private Unbinder unbinder;
  private BillTransactionCreationPresenter presenter;

  public static BillTransactionCreationFragment create() {
    return new BillTransactionCreationFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    presenter = new BillTransactionCreationPresenter(this, getContainer().getComponent());
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_transaction_creation_bill, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    unbinder = ButterKnife.bind(this, view);
  }

  @Override
  public void onStart() {
    super.onStart();
    presenter.onViewStarted();
  }

  @Override
  public void onStop() {
    super.onStop();
    presenter.onViewStopped();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    presenter = null;
  }
}
