package com.tpago.movil.ui.main.accounts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tpago.movil.R;
import com.tpago.movil.data.MessageHelper;
import com.tpago.movil.domain.Account;
import com.tpago.movil.domain.Balance;
import com.tpago.movil.ui.main.SubFragment;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
public class AccountsFragment extends SubFragment implements AccountsScreen,
  View.OnLayoutChangeListener {
  /**
   * TODO
   */
  private Unbinder unbinder;

  /**
   * TODO
   */
  private Adapter adapter;

  /**
   * TODO
   */
  @Inject
  MessageHelper messageHelper;

  /**
   * TODO
   */
  @Inject
  AccountsPresenter presenter;

  /**
   * TODO
   */
  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;

  /**
   * TODO
   */
  @BindView(R.id.button_add_another_account)
  Button addAnotherAccountButton;

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public static AccountsFragment newInstance() {
    return new AccountsFragment();
  }

  /**
   * TODO
   */
  @OnClick(R.id.button_add_another_account)
  void onAddAnotherAccountButtonClicked() {
    // TODO: Open the add another account button.
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all the annotated dependencies.
    final AccountsComponent component = DaggerAccountsComponent.builder()
      .mainComponent(parentScreen.getComponent())
      .accountsModule(new AccountsModule(this))
      .build();
    component.inject(this);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_accounts, container, false);
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
    final RecyclerView.ItemDecoration decoration = new HorizontalDividerItemDecoration
      .Builder(getContext())
      .drawable(R.drawable.list_item_divider)
      .build();
    recyclerView.addItemDecoration(decoration);
    // Adds a listener that gets notified every time the layout of the recycler view changes.
    recyclerView.addOnLayoutChangeListener(this);
  }

  @Override
  public void onStart() {
    super.onStart();
    // Sets the title.
    parentScreen.setTitle(messageHelper.accounts());
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
    // Adds a listener that gets notified every time the layout of the recycler view changes.
    recyclerView.removeOnLayoutChangeListener(this);
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
  public void add(@NonNull Account account) {
    if (adapter != null) {
      adapter.add(account);
    }
  }

  @Override
  public void setBalance(@NonNull Account account, @Nullable Balance balance) {
    if (adapter != null) {
      adapter.setBalance(account, balance);
    }
  }

  @Override
  public void showLastTransactionsButton() {
    if (adapter != null) {
      adapter.showLastTransactionsItem();
    }
  }

  @Override
  public void onLayoutChange(View view, int left, int top, int right, int bottom,
    int oldLeft, int oldTop, int oldRight, int oldBottom) {
    if (view == recyclerView && addAnotherAccountButton != null) {
      final int buttonTop = addAnotherAccountButton.getTop();
      final boolean flag = bottom < buttonTop;
      addAnotherAccountButton.setEnabled(flag);
      addAnotherAccountButton.setVisibility(flag ? View.VISIBLE : View.INVISIBLE);
      Timber.d("RecyclerView.Bottom (%1$d) < Button.Top (%2$d) = %3$s", bottom, buttonTop, flag);
    }
  }

  /**
   * TODO
   */
  private class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * TODO
     */
    private static final int TYPE_ACCOUNT = 0;

    /**
     * TODO
     */
    private static final int TYPE_LAST_TRANSACTIONS = 1;

    /**
     * TODO
     */
    private final LastTransactionsItem lastTransactionsItem = new LastTransactionsItem();

    /**
     * TODO
     */
    private final List<Object> items = new ArrayList<>();

    /**
     * TODO
     *
     * @param account
     *   TODO
     *
     * @return TODO
     */
    @Nullable
    private AccountItem findByAccount(@NonNull Account account) {
      for (Object item : items) {
        if (item instanceof AccountItem && ((AccountItem) item).account.equals(account)) {
          return (AccountItem) item;
        }
      }
      return null;
    }

    /**
     * TODO
     */
    final void clear() {
      final int count = getItemCount();
      if (count > 0) {
        items.clear();
        notifyItemRangeRemoved(0, count);
      }
    }

    /**
     * TODO
     *
     * @param account
     *   TODO
     */
    final void add(@NonNull Account account) {
      final AccountItem item = findByAccount(account);
      if (item == null) {
        items.add(new AccountItem(account));
        notifyItemInserted(getItemCount());
      }
    }

    /**
     * TODO
     *
     * @param account
     *   TODO
     * @param balance
     *   TODO
     */
    final void setBalance(@NonNull Account account, @Nullable Balance balance) {
      final AccountItem item = findByAccount(account);
      if (item != null) {
        item.setBalance(balance);
        notifyItemChanged(items.indexOf(item));
      }
    }

    /**
     * TODO
     */
    final void showLastTransactionsItem() {
      if (!items.contains(lastTransactionsItem)) {
        items.add(lastTransactionsItem);
        notifyItemInserted(getItemCount());
      }
    }

    @Override
    public int getItemViewType(int position) {
      return items.get(position) instanceof AccountItem ? TYPE_ACCOUNT : TYPE_LAST_TRANSACTIONS;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
      if (viewType == TYPE_ACCOUNT) {
        return new AccountItemViewHolder(inflater.inflate(R.layout.item_account, parent, false));
      } else {
        return new LastTransactionsViewHolder(inflater.inflate(R.layout.item_last_transactions,
          parent, false));
      }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      if (getItemViewType(position) == TYPE_ACCOUNT) {
        final AccountItemViewHolder accountHolder = (AccountItemViewHolder) holder;
        final AccountItem item = (AccountItem) items.get(position);
        final Account account = item.getAccount();
        // TODO: Load the logo of the bank.
        accountHolder.accountAliasTextView.setText(account.getAlias());
        accountHolder.bankNameTextView.setText(account.getBank().getName());
        final Balance balance = item.getBalance();
        if (balance != null) {
          accountHolder.accountBalanceTextView.setVisibility(View.VISIBLE);
          // TODO: Format the balance as currency.
          accountHolder.queryAccountBalanceButton.setVisibility(View.GONE);
        } else {
          accountHolder.accountBalanceTextView.setVisibility(View.GONE);
          accountHolder.queryAccountBalanceButton.setVisibility(View.VISIBLE);
        }
      }
    }

    @Override
    public int getItemCount() {
      return items.size();
    }
  }
}
