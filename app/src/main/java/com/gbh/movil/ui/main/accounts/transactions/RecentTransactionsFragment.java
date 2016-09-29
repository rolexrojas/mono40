package com.gbh.movil.ui.main.accounts.transactions;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gbh.movil.R;
import com.gbh.movil.data.MessageHelper;
import com.gbh.movil.domain.Transaction;
import com.gbh.movil.ui.main.SubFragment;

import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * TODO
 *
 * @author hecvasro
 */
public class RecentTransactionsFragment extends SubFragment implements RecentTransactionsScreen {
  private Unbinder unbinder;

  @Inject
  MessageHelper messageHelper;

  @Inject
  RecentTransactionsPresenter presenter;

  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public static RecentTransactionsFragment newInstance() {
    return new RecentTransactionsFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all the dependencies.
    final RecentTransactionsComponent component = DaggerRecentTransactionsComponent.builder()
      .mainComponent(parentScreen.getComponent())
      .recentTransactionsModule(new RecentTransactionsModule(this))
      .build();
    component.inject(this);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_recent_transactions, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all the annotated views and methods.
    unbinder = ButterKnife.bind(this, view);
  }

  @Override
  public void onStart() {
    super.onStart();
    // Sets the title.
    parentScreen.setTitle(messageHelper.recentTransactions());
    // Starts the presenter.
    presenter.start();
  }

  @Override
  public void onStop() {
    super.onStop();
    // Stops the presenter.
    presenter.stop();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Unbinds all the annotated views and methods.
    unbinder.unbind();
  }

  @Override
  public void clear() {
    // TODO
  }

  @Override
  public void add(@NonNull Date date) {
    // TODO
  }

  @Override
  public void add(@NonNull Transaction transaction) {
    // TODO
  }
}
