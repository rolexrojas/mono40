package com.gbh.movil.ui.main.products;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gbh.movil.Utils;
import com.gbh.movil.data.res.AssetProvider;
import com.gbh.movil.ui.UiUtils;
import com.gbh.movil.ui.main.MainContainer;
import com.gbh.movil.ui.main.list.Adapter;
import com.gbh.movil.ui.main.list.HolderBinderFactory;
import com.gbh.movil.ui.main.list.HolderCreatorFactory;
import com.gbh.movil.ui.view.widget.LoadIndicator;
import com.gbh.movil.ui.view.widget.SwipeRefreshLayoutRefreshIndicator;
import com.gbh.movil.ui.main.AddAnotherProductFragment;
import com.gbh.movil.ui.main.PinConfirmationDialogFragment;
import com.gbh.movil.R;
import com.gbh.movil.data.StringHelper;
import com.gbh.movil.domain.Product;
import com.gbh.movil.domain.Balance;
import com.gbh.movil.ui.SubFragment;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

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
public class ProductsFragment extends SubFragment<MainContainer> implements ProductsScreen,
  ProductHolder.OnQueryActionButtonClickedListener,
  ShowRecentTransactionsHolder.OnShowRecentTransactionsButtonClickedListener {
  private static final String TAG_PIN_CONFIRMATION = "pinConfirmation";

  private Unbinder unbinder;
  private Adapter adapter;
  private LoadIndicator loadIndicator;

  @Inject
  StringHelper stringHelper;
  @Inject
  AssetProvider assetProvider;
  @Inject
  ProductsPresenter presenter;

  @BindView(R.id.swipe_refresh_layout)
  SwipeRefreshLayout swipeRefreshLayout;
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
  public static ProductsFragment newInstance() {
    return new ProductsFragment();
  }

  /**
   * TODO
   *
   * @param product
   *   {@link Product} that will be queried.
   */
  private void queryBalance(@NonNull final Product product, final int x, final int y) {
    final FragmentManager manager = getChildFragmentManager();
    final Fragment fragment = manager.findFragmentByTag(TAG_PIN_CONFIRMATION);
    if (Utils.isNotNull(fragment) && fragment instanceof PinConfirmationDialogFragment) {
      ((PinConfirmationDialogFragment) fragment).dismiss();
    }
    Timber.d("X = %1$d, Y = %2$d", x, y);
    PinConfirmationDialogFragment.newInstance(x, y,
      stringHelper.feeForTransaction(product.getCurrency(), product.getQueryFee()),
      new PinConfirmationDialogFragment.Callback() {
        @Override
        public void confirm(@NonNull String pin) {
          presenter.queryBalance(product, pin);
        }
      }).show(manager, TAG_PIN_CONFIRMATION);
  }

  /**
   * TODO
   */
  @OnClick(R.id.button_add_another_account)
  void onAddAnotherAccountButtonClicked() {
    container.setSubScreen(AddAnotherProductFragment.newInstance());
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all the annotated dependencies.
    final ProductsComponent component = DaggerProductsComponent.builder()
      .mainComponent(container.getComponent())
      .build();
    component.inject(this);
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
    final HolderCreatorFactory holderCreatorFactory = new HolderCreatorFactory.Builder()
      .addCreator(ShowRecentTransactionsItem.class,
        new ShowRecentTransactionsHolderCreator(this))
      .addCreator(ProductItem.class, new ProductHolderCreator(this))
      .build();
    final HolderBinderFactory holderBinderFactory = new HolderBinderFactory.Builder()
      .addBinder(ProductItem.class, ProductHolder.class, new ProductHolderBinder(assetProvider))
      .build();
    adapter = new Adapter(holderCreatorFactory, holderBinderFactory);
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
    container.setTitle(stringHelper.accounts());
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
  public void onBalanceQueried(boolean succeeded, @NonNull Product product,
    @Nullable Balance balance) {
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
//    startActivity(RecentTransactionsActivity.getLaunchIntent(getContext()));
    UiUtils.createDialog(getContext(), getString(R.string.sorry),
      getString(R.string.info_not_available_recent_transactions), getString(R.string.ok), null, null,
      null).show();
  }
}
