package com.tpago.movil.d.ui.main.products.transactions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpago.movil.dep.App;
import com.tpago.movil.R;
import com.tpago.movil.d.misc.Utils;
import com.tpago.movil.d.data.Formatter;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.Transaction;
import com.tpago.movil.d.ui.DepBaseActivity;
import com.tpago.movil.d.ui.view.widget.LoadIndicator;
import com.tpago.movil.d.ui.view.widget.SwipeRefreshLayoutRefreshIndicator;
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
 * @author hecvasro
 */
public class RecentTransactionsActivity
  extends DepBaseActivity
  implements RecentTransactionsScreen,
  SwipeRefreshLayout.OnRefreshListener {
  private Unbinder unbinder;
  private Adapter adapter;
  private LoadIndicator loadIndicator;

  @Inject
  StringHelper stringHelper;
  @Inject
  RecentTransactionsPresenter presenter;

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.swipe_refresh_layout)
  SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;

  @NonNull
  public static Intent getLaunchIntent(@NonNull Context context) {
    return new Intent(context, RecentTransactionsActivity.class);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
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
    // Adds a listener that gets notified every time the content must be refreshed.
    swipeRefreshLayout.setOnRefreshListener(this);
    // Prepares the recycler view.
    adapter = new Adapter();
    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
    recyclerView.setItemAnimator(null);
    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
      false));
    final RecyclerView.ItemDecoration divider = new HorizontalDividerItemDecoration.Builder(this)
      .drawable(R.drawable.d_divider)
      .marginResId(R.dimen.space_horizontal_normal)
      .visibilityProvider(new FlexibleDividerDecoration.VisibilityProvider() {
        @Override
        public boolean shouldHideDivider(int position, RecyclerView parent) {
          return position < 0 || adapter.shouldHideDivider(position);
        }
      })
      .build();
    recyclerView.addItemDecoration(divider);
    // Injects all the dependencies.
    final RecentTransactionsComponent component = DaggerRecentTransactionsComponent.builder()
      .appComponent(((App) getApplication()).component())
      .build();
    component.inject(this);
    // Attaches the screen to the presenter.
    presenter.attachScreen(this);
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
    // Detaches the screen from the presenter.
    presenter.detachScreen();
    // Removes the listener that gets notified every time the content must be refreshed.
    swipeRefreshLayout.setOnRefreshListener(null);
    // Unbinds all the annotated views and methods.
    unbinder.unbind();
  }

  @Nullable
  public LoadIndicator getRefreshIndicator() {
    if (Utils.isNull(loadIndicator) && Utils.isNotNull(swipeRefreshLayout)) {
      loadIndicator = new SwipeRefreshLayoutRefreshIndicator(swipeRefreshLayout);
    }
    return loadIndicator;
  }

  @Override
  public void clear() {
    if (Utils.isNotNull(adapter)) {
      adapter.clear();
    }
  }

  @Override
  public void add(@NonNull Date date) {
    if (Utils.isNotNull(adapter)) {
      adapter.add(date);
    }
  }

  @Override
  public void add(@NonNull Transaction transaction) {
    if (Utils.isNotNull(adapter)) {
      adapter.add(transaction);
    }
  }

  @Override
  public void onRefresh() {
    presenter.refresh();
  }

  @Override
  protected int layoutResId() {
    return R.layout.d_activity_recent_transactions;
  }

  /**
   * TODO
   */
  private class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_GROUP_TITLE = 0;
    private static final int TYPE_TRANSACTION = 1;

    private final List<Object> items = new ArrayList<>();

    boolean shouldHideDivider(int position) {
      final int type = getItemViewType(position);
      return type == TYPE_GROUP_TITLE || (type == TYPE_TRANSACTION &&
        (!((position + 1) < items.size())) || getItemViewType(position + 1) != TYPE_TRANSACTION);
    }

    void clear() {
      final int count = getItemCount();
      if (count > 0) {
        items.clear();
        notifyItemRangeRemoved(0, count);
      }
    }

    void add(@NonNull Date date) {
      if (!items.contains(date)) {
        items.add(date);
        notifyItemInserted(getItemCount());
      }
    }

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
        return new GroupItemViewHolder(
          inflater.inflate(R.layout.d_list_item_group_title, parent, false)
        );
      } else {
        return new TransactionItemViewHolder(
          inflater.inflate(R.layout.d_list_item_transaction, parent, false)
        );
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
        transactionHolder.nameTextView.setText(transaction.detail());
        transactionHolder.typeTextView.setText(transaction.type());
//        final String currency = transaction.getCurrency();
//        transactionHolder.amountTextView.setPrefix("RD$");
//        transactionHolder.amountTextView.setPrefixTextColorFromResource(colorId);
//        final BigDecimal value = transaction.getValue();
        transactionHolder.amountTextView.setContent(Formatter.amount(transaction.amount()));
//        transactionHolder.amountTextView.setTextColorFromResource(colorId);
      }
    }

    @Override
    public int getItemCount() {
      return items.size();
    }
  }
}
