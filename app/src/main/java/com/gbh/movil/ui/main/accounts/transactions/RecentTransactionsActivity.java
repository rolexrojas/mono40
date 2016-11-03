package com.gbh.movil.ui.main.accounts.transactions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gbh.movil.App;
import com.gbh.movil.R;
import com.gbh.movil.data.Formatter;
import com.gbh.movil.data.MessageHelper;
import com.gbh.movil.domain.Transaction;
import com.gbh.movil.ui.BaseActivity;
import com.yqritc.recyclerviewflexibledivider.FlexibleDividerDecoration;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

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
public class RecentTransactionsActivity extends BaseActivity implements RecentTransactionsScreen {
  private Unbinder unbinder;

  private Adapter adapter;

  @Inject
  MessageHelper messageHelper;
  @Inject
  RecentTransactionsPresenter presenter;

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;

  @NonNull
  public static Intent getLaunchIntent(@NonNull Context context) {
    return new Intent(context, RecentTransactionsActivity.class);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all the dependencies.
    final RecentTransactionsComponent component = DaggerRecentTransactionsComponent.builder()
      .appComponent(((App) getApplication()).getComponent())
      .recentTransactionsModule(new RecentTransactionsModule(this))
      .build();
    component.inject(this);
    // Sets the content layout identifier.
    setContentView(R.layout.activity_recent_transactions);
    // Binds all the annotated views and methods.
    unbinder = ButterKnife.bind(this);
    // Prepares the toolbar.
    toolbar.setTitle(R.string.recent_transactions);
    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onBackPressed();
      }
    });
    // Prepares the recycler view.
    if (adapter == null) {
      adapter = new Adapter();
    }
    recyclerView.setAdapter(adapter);
    recyclerView.setItemAnimator(null);
    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
      false));
    final RecyclerView.ItemDecoration divider = new HorizontalDividerItemDecoration.Builder(this)
      .drawable(R.drawable.list_item_divider)
      .visibilityProvider(new FlexibleDividerDecoration.VisibilityProvider() {
        @Override
        public boolean shouldHideDivider(int position, RecyclerView parent) {
          return position < 0 || adapter.shouldHideDivider(position);
        }
      })
      .build();
    recyclerView.addItemDecoration(divider);
  }

  @Override
  public void onStart() {
    super.onStart();
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
  public void onDestroy() {
    super.onDestroy();
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

    boolean shouldHideDivider(int position) {
      final int type = getItemViewType(position);
      return type == TYPE_GROUP_TITLE || (type == TYPE_TRANSACTION &&
        (!((position + 1) < items.size())) || getItemViewType(position + 1) != TYPE_TRANSACTION);
    }

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
        final String currencyCode = transaction.getCurrency();
        final int colorId = transaction.getRequestType() == Transaction.RequestType.CREDIT ?
          R.color.transaction_type_credit : R.color.transaction_type_debit;
        transactionHolder.amountView.setCurrency(currencyCode);
        transactionHolder.amountView.setCurrencyColor(colorId);
        transactionHolder.amountView.setValue(Math.abs(value));
        transactionHolder.amountView.setValueColor(colorId);
      }
    }

    @Override
    public int getItemCount() {
      return items.size();
    }
  }
}
