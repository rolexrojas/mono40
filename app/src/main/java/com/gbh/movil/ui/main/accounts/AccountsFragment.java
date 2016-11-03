package com.gbh.movil.ui.main.accounts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gbh.movil.ui.main.AddAnotherAccountFragment;
import com.gbh.movil.ui.main.PinConfirmationDialogFragment;
import com.gbh.movil.R;
import com.gbh.movil.data.MessageHelper;
import com.gbh.movil.domain.Account;
import com.gbh.movil.domain.Balance;
import com.gbh.movil.domain.Bank;
import com.gbh.movil.ui.main.SubFragment;
import com.gbh.movil.ui.main.accounts.transactions.RecentTransactionsActivity;
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
  View.OnLayoutChangeListener, ShowRecentTransactionsViewHolder.Listener {
  private static final String TAG_PIN_CONFIRMATION = "pinConfirmation";

  private Unbinder unbinder;

  private Adapter adapter;

  @Inject
  MessageHelper messageHelper;

  @Inject
  AccountsPresenter presenter;

  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;

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
   *
   * @param account
   *   {@link Account} that will be queried.
   */
  private void queryBalance(@NonNull final Account account, final int x, final int y) {
    final FragmentManager manager = getChildFragmentManager();
    final Fragment fragment = manager.findFragmentByTag(TAG_PIN_CONFIRMATION);
    if (fragment != null && fragment instanceof PinConfirmationDialogFragment) {
      ((PinConfirmationDialogFragment) fragment).dismiss();
    }
    Timber.d("X = %1$d, Y = %2$d", x, y);
    PinConfirmationDialogFragment.newInstance(x, y,
      messageHelper.feeForTransaction(account.getCurrency(), account.getQueryFee()),
      new PinConfirmationDialogFragment.Callback() {
        @Override
        public void confirm(@NonNull String pin) {
          presenter.queryBalance(account, pin);
        }
      }).show(manager, TAG_PIN_CONFIRMATION);
  }

  /**
   * TODO
   */
  @OnClick(R.id.button_add_another_account)
  void onAddAnotherAccountButtonClicked() {
    parentScreen.setSubScreen(AddAnotherAccountFragment.newInstance());
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
      adapter = new Adapter(this);
    }
    recyclerView.setAdapter(adapter);
    recyclerView.setItemAnimator(null);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
      LinearLayoutManager.VERTICAL, false));
    final RecyclerView.ItemDecoration divider = new HorizontalDividerItemDecoration
      .Builder(getContext())
      .drawable(R.drawable.list_item_divider)
      .build();
    recyclerView.addItemDecoration(divider);
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
  public void onBalanceQueried(boolean succeeded, @NonNull Account account,
    @Nullable Balance balance) {
    final Fragment fragment = getChildFragmentManager().findFragmentByTag(TAG_PIN_CONFIRMATION);
    if (fragment != null && fragment instanceof PinConfirmationDialogFragment) {
      ((PinConfirmationDialogFragment) fragment).resolve(succeeded);
    }
    setBalance(account, balance);
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

  @Override
  public void onShowRecentTransactionsButtonClicked() {
    startActivity(RecentTransactionsActivity.getLaunchIntent(getContext()));
  }

  /**
   * TODO
   */
  private class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    implements AccountItemViewHolder.Listener {
    private static final int TYPE_ACCOUNT = 0;

    private static final int TYPE_LAST_TRANSACTIONS = 1;

    private final ShowRecentTransactionsViewHolder.Listener listener;

    private final ShowRecentTransactionsItem showRecentTransactionsItem
      = new ShowRecentTransactionsItem();

    private final List<Object> items = new ArrayList<>();

    private Adapter(@NonNull ShowRecentTransactionsViewHolder.Listener listener) {
      this.listener = listener;
    }

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
      if (!items.contains(showRecentTransactionsItem)) {
        items.add(showRecentTransactionsItem);
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
        return new AccountItemViewHolder(inflater.inflate(R.layout.list_item_account, parent,
          false), this);
      } else {
        return new ShowRecentTransactionsViewHolder(inflater
          .inflate(R.layout.list_item_show_recent_transactions, parent, false), listener);
      }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      if (getItemViewType(position) == TYPE_ACCOUNT) {
        final AccountItemViewHolder accountHolder = (AccountItemViewHolder) holder;
        final AccountItem item = (AccountItem) items.get(position);
        final Account account = item.getAccount();
        final Bank bank = account.getBank();
        // TODO: Load bank's logo.
        accountHolder.accountAliasTextView.setText(account.getAlias());
        accountHolder.bankNameTextView.setText(bank.getName());
        final Balance balance = item.getBalance();
        if (balance != null) {
          accountHolder.amountView.setVisibility(View.VISIBLE);
          accountHolder.amountView.setCurrency(account.getCurrency());
          accountHolder.amountView.setValue(balance.getValue());
          accountHolder.queryAccountBalanceButton.setVisibility(View.GONE);
        } else {
          accountHolder.amountView.setVisibility(View.GONE);
          accountHolder.queryAccountBalanceButton.setVisibility(View.VISIBLE);
        }
      }
    }

    @Override
    public int getItemCount() {
      return items.size();
    }

    @Override
    public void onQueryBalanceButtonClicked(int position, int x, int y) {
      queryBalance(((AccountItem) items.get(position)).getAccount(), x, y);
    }
  }
}
