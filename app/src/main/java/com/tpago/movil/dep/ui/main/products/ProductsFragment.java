package com.tpago.movil.dep.ui.main.products;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tpago.movil.dep.data.res.DepAssetProvider;
import com.tpago.movil.dep.misc.Utils;
import com.tpago.movil.dep.data.util.BinderFactory;
import com.tpago.movil.dep.ui.Dialogs;
import com.tpago.movil.dep.ui.main.MainContainer;
import com.tpago.movil.dep.ui.main.list.ListItemAdapter;
import com.tpago.movil.dep.ui.main.list.ListItemHolderCreatorFactory;
import com.tpago.movil.dep.ui.view.widget.LoadIndicator;
import com.tpago.movil.dep.ui.view.widget.SwipeRefreshLayoutRefreshIndicator;
import com.tpago.movil.dep.ui.main.AddAnotherProductFragment;
import com.tpago.movil.dep.ui.main.PinConfirmationDialogFragment;
import com.tpago.movil.R;
import com.tpago.movil.dep.data.StringHelper;
import com.tpago.movil.dep.domain.Product;
import com.tpago.movil.dep.domain.Balance;
import com.tpago.movil.dep.ui.ChildFragment;
import com.tpago.movil.text.Texts;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * @author hecvasro
 */
@Deprecated
public class ProductsFragment extends ChildFragment<MainContainer>
  implements ProductsScreen,
  ProductListItemHolder.OnQueryActionButtonClickedListener,
  ShowRecentTransactionsListItemHolder.OnShowRecentTransactionsButtonClickedListener,
  PinConfirmationDialogFragment.OnDismissListener {
  private static final String TAG_PIN_CONFIRMATION = "pinConfirmation";

  private Unbinder unbinder;
  private ListItemAdapter adapter;
  private LoadIndicator loadIndicator;
  private String requestMessage = null;

  @Inject
  StringHelper stringHelper;
  @Inject
  DepAssetProvider assetProvider;
  @Inject
  ProductsPresenter presenter;

  @BindView(R.id.swipe_refresh_layout)
  SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;
  @BindView(R.id.button_add_another_account)
  Button addAnotherAccountButton;

  @NonNull
  public static ProductsFragment newInstance() {
    return new ProductsFragment();
  }

  private void queryBalance(@NonNull final Product product, final int x, final int y) {
    final FragmentManager manager = getChildFragmentManager();
    final Fragment fragment = manager.findFragmentByTag(TAG_PIN_CONFIRMATION);
    if (Utils.isNotNull(fragment) && fragment instanceof PinConfirmationDialogFragment) {
      ((PinConfirmationDialogFragment) fragment).dismiss();
    }
    PinConfirmationDialogFragment.newInstance(x, y,
      stringHelper.feeForTransaction(product.getCurrency(), product.getQueryFee()),
      new PinConfirmationDialogFragment.Callback() {
        @Override
        public void confirm(@NonNull String pin) {
          presenter.queryBalance(product, pin);
        }
      }).show(manager, TAG_PIN_CONFIRMATION);
  }

  @OnClick(R.id.button_add_another_account)
  void onAddAnotherAccountButtonClicked() {
    getContainer().setChildFragment(AddAnotherProductFragment.newInstance(), true, true);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all the annotated dependencies.
    final ProductsComponent component = DaggerProductsComponent.builder()
      .depMainComponent(getContainer().getComponent())
      .build();
    component.inject(this);
    setHasOptionsMenu(true);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_products, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all the annotated views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Prepares the recycler view.
    final ListItemHolderCreatorFactory holderCreatorFactory = new ListItemHolderCreatorFactory.Builder()
      .addCreator(ShowRecentTransactionsItem.class,
        new ShowRecentTransactionsListItemHolderCreator(this))
      .addCreator(ProductItem.class, new ProductListItemHolderCreator(this))
      .build();
    final BinderFactory holderBinderFactory = new BinderFactory.Builder()
      .addBinder(ProductItem.class, ProductListItemHolder.class,
        new ProductListItemHolderBinder(stringHelper, assetProvider))
      .build();
    adapter = new ListItemAdapter(holderCreatorFactory, holderBinderFactory);
    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
    recyclerView.setItemAnimator(null);
    final Context context = getContext();
    final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,
      LinearLayoutManager.VERTICAL, false) {
      @Override
      public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        // Determines whether the add another account button must be visible or not. It will
        // only be visible if the bottom of the last child of the recycler view does not collide
        // with the top of the add another account button.
        final int lastChildPosition = findLastVisibleItemPosition();
        if (lastChildPosition >= 0) {
          final View lastChild = recyclerView.getChildAt(lastChildPosition);
          if (Utils.isNotNull(lastChild) && Utils.isNotNull(addAnotherAccountButton)) {
            final int lastChildBottom = lastChild.getBottom();
            final int buttonTop = addAnotherAccountButton.getTop();
            final boolean flag = lastChildBottom < buttonTop;
            addAnotherAccountButton.setEnabled(flag);
            addAnotherAccountButton.setVisibility(flag ? View.VISIBLE : View.INVISIBLE);
            Timber.d("RecyclerView.LastChild.Bottom (%1$d) < Button.Top (%2$d) = %3$s",
              lastChildBottom, buttonTop, flag);
          }
        }
      }
    };
    recyclerView.setLayoutManager(layoutManager);
    final RecyclerView.ItemDecoration divider = new HorizontalDividerItemDecoration.Builder(context)
      .drawable(R.drawable.divider)
      .marginResId(R.dimen.space_horizontal_normal)
      .build();
    recyclerView.addItemDecoration(divider);
    // Attaches the screen to the presenter.
    presenter.attachScreen(this);
  }

  @Override
  public void onStart() {
    super.onStart();
    // Sets the title.
    getContainer().setTitle(stringHelper.accounts());
    // Starts the presenter.
    presenter.start();
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.products, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.menu_item_add_another_account) {
      onAddAnotherAccountButtonClicked();
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
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
    // Detaches the screen from the presenter.
    presenter.detachScreen();
    // Unbinds all the annotated views and methods.
    unbinder.unbind();
  }

  @Override
  public void clear() {
    adapter.clear();
  }

  @Override
  public void add(@NonNull Object item) {
    adapter.add(item);
  }

  @Override
  public void onBalanceQueried(
    boolean succeeded,
    @NonNull Product product,
    @Nullable Balance balance,
    @Nullable String message) {
    final Fragment fragment = getChildFragmentManager().findFragmentByTag(TAG_PIN_CONFIRMATION);
    if (Utils.isNotNull(fragment) && fragment instanceof PinConfirmationDialogFragment) {
      ((PinConfirmationDialogFragment) fragment).resolve(succeeded);
    }
    setBalance(product, balance);
  }

  @Override
  public void setBalance(@NonNull Product product, @Nullable Balance balance) {
    final ProductItem item = new ProductItem(product);
    if (adapter.contains(item)) {
      final int index = adapter.indexOf(item);
      final ProductItem actualItem = (ProductItem) adapter.get(index);
      actualItem.setBalance(balance);
      adapter.set(index, actualItem);
    }
  }

  @Nullable
  public LoadIndicator getRefreshIndicator() {
    if (Utils.isNull(loadIndicator) && Utils.isNotNull(swipeRefreshLayout)) {
      loadIndicator = new SwipeRefreshLayoutRefreshIndicator(swipeRefreshLayout);
    }
    return loadIndicator;
  }

  @Override
  public void onQueryBalanceButtonClicked(int position, int x, int y) {
    queryBalance(((ProductItem) adapter.get(position)).getProduct(), x, y);
  }

  @Override
  public void onShowRecentTransactionsButtonClicked() {
    Dialogs.featureNotAvailable(getActivity()).show();
  }

  @Override
  public void onDismiss(boolean succeeded) {
    if (!succeeded) {
      final String m = Texts.isEmpty(requestMessage) ? getString(R.string.error_message) : requestMessage;
      Dialogs.builder(getContext())
        .setTitle(R.string.error_title)
        .setMessage(m)
        .setPositiveButton(R.string.error_positive_button_text, null)
        .show();
      requestMessage = null;
    }
  }
}
