package com.gbh.movil.ui.main.accounts.transactions;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gbh.movil.R;
import com.gbh.movil.data.Formatter;
import com.gbh.movil.data.MessageHelper;
import com.gbh.movil.domain.Transaction;
import com.gbh.movil.ui.main.SubFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

  private Adapter adapter;

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
    // Prepares the recycler view.
    if (adapter == null) {
      adapter = new Adapter();
    }
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
      LinearLayoutManager.VERTICAL, false));
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
    if (adapter != null) {
      adapter.clear();
    }
  }

  @Override
  public void add(@NonNull Date date) {
    if (adapter != null) {
      adapter.add(date);
    }
  }

  @Override
  public void add(@NonNull Transaction transaction) {
    if (adapter != null) {
      adapter.add(transaction);
    }
  }

  private class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_GROUP_TITLE = 0;

    private static final int TYPE_TRANSACTION = 1;

    private final List<Object> items = new ArrayList<>();

    /**
     * TODO
     */
    void clear() {
      final int count = getItemCount();
      if (count > 0) {
        items.clear();
        notifyItemRangeRemoved(0, count);
      }
    }

    /**
     * TODO
     *
     * @param date
     *   TODO
     */
    void add(@NonNull Date date) {
      if (!items.contains(date)) {
        items.add(date);
        notifyItemInserted(getItemCount());
      }
    }

    /**
     * TODO
     *
     * @param transaction
     *   TODO
     */
    void add(@NonNull Transaction transaction) {
      if (!items.contains(transaction)) {
        items.add(transaction);
        notifyItemInserted(getItemCount());
      }
    }

    @Override
    public int getItemViewType(int position) {
      return items.get(position) instanceof Date ? TYPE_GROUP_TITLE : TYPE_TRANSACTION;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
      if (viewType == TYPE_GROUP_TITLE) {
        return new GroupItemViewHolder(inflater.inflate(R.layout.list_item_group_title, parent,
          false));
      } else {
        return new TransactionItemViewHolder(inflater.inflate(R.layout.list_item_transaction,
          parent, false));
      }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      final Object item = items.get(position);
      final int type = getItemViewType(position);
      if (type == TYPE_GROUP_TITLE) {
        ((GroupItemViewHolder) holder).getTextView().setText(Formatter.date((Date) item));
      } else {
        final Transaction transaction = (Transaction) item;
        final TransactionItemViewHolder transactionHolder = (TransactionItemViewHolder) holder;
        transactionHolder.nameTextView.setText(transaction.getName());
        transactionHolder.typeTextView.setText(transaction.getType());
        final double value = transaction.getValue();
        final String currencyCode = transaction.getCurrencyCode();
        transactionHolder.valueTextView.setText(Formatter.currency(currencyCode, value));
        final Context context = holder.itemView.getContext();
        final int colorId = value > 0 ? R.color.currency_positive : R.color.currency_negative;
        transactionHolder.valueTextView.setTextColor(ContextCompat.getColor(context, colorId));
      }
    }

    @Override
    public int getItemCount() {
      return items.size();
    }
  }
}
