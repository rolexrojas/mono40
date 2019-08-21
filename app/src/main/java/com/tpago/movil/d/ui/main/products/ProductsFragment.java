package com.tpago.movil.d.ui.main.products;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.tpago.movil.app.ui.loader.takeover.TakeoverLoaderDialogFragment;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.dep.api.DCurrencies;
import com.tpago.movil.d.data.util.BinderFactory;
import com.tpago.movil.d.ui.Dialogs;
import com.tpago.movil.d.ui.main.MainContainer;
import com.tpago.movil.d.ui.main.list.ListItemAdapter;
import com.tpago.movil.d.ui.main.list.ListItemHolder;
import com.tpago.movil.d.ui.main.list.ListItemHolderCreatorFactory;
import com.tpago.movil.d.ui.main.products.transactions.RecentTransactionsActivityBase;
import com.tpago.movil.d.ui.view.Views;
import com.tpago.movil.d.ui.view.widget.LoadIndicator;
import com.tpago.movil.d.ui.view.widget.SwipeRefreshLayoutRefreshIndicator;
import com.tpago.movil.d.ui.main.AddAnotherProductFragment;
import com.tpago.movil.d.ui.main.PinConfirmationDialogFragment;
import com.tpago.movil.R;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.Balance;
import com.tpago.movil.d.ui.ChildFragment;
import com.tpago.movil.dep.init.InitActivityBase;
import com.tpago.movil.util.ObjectHelper;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author hecvasro
 */
@Deprecated
public class ProductsFragment
        extends ChildFragment<MainContainer>
        implements ProductsScreen,
        ListItemHolder.OnClickListener,
        ProductListItemHolder.OnQueryBalanceButtonPressedListener,
        ShowRecentTransactionsListItemHolder.OnShowRecentTransactionsButtonClickedListener {

    private static final String TAKE_OVER_LOADER_DIALOG = "TAKE_OVER_DIALOG";
    private Unbinder unbinder;
    private ListItemAdapter adapter;
    private LoadIndicator loadIndicator;

    @Inject
    StringHelper stringHelper;
    @Inject
    CompanyHelper companyHelper;
    @Inject
    ProductsPresenter presenter;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.button_add_another_account)
    Button addAnotherAccountButton;
    private Disposable closeSessionDisposable;
    private TakeoverLoaderDialogFragment takeoverLoader;

    @NonNull
    public static ProductsFragment newInstance() {
        return new ProductsFragment();
    }

    private void queryBalance(@NonNull final Product product, final int x, final int y) {
        PinConfirmationDialogFragment.show(
                getChildFragmentManager(),
                stringHelper.feeForTransaction(DCurrencies.map(product.getCurrency()), product.getQueryFee()),
                new PinConfirmationDialogFragment.Callback() {
                    @Override
                    public void confirm(String pin) {
                        presenter.queryBalance(product, pin);
                    }
                },
                x,
                y
        );
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
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.d_fragment_products, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Binds all the annotated views and methods.
        unbinder = ButterKnife.bind(this, view);
        // Prepares the recycler view.
        final ListItemHolderCreatorFactory holderCreatorFactory = new ListItemHolderCreatorFactory
                .Builder()
                .addCreator(
                        ShowRecentTransactionsItem.class,
                        new ShowRecentTransactionsListItemHolderCreator(this)
                )
                .addCreator(
                        ProductItem.class,
                        new ProductListItemHolderCreator(this, this)
                )
                .build();
        final BinderFactory holderBinderFactory = new BinderFactory.Builder()
                .addBinder(
                        ProductItem.class,
                        ProductListItemHolder.class,
                        new ProductListItemHolderBinder(this.stringHelper, this.companyHelper)
                )
                .build();
        adapter = new ListItemAdapter(holderCreatorFactory, holderBinderFactory);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(null);
        final Context context = getContext();
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false
        ) {
            @Override
            public void onLayoutCompleted(RecyclerView.State state) {
                super.onLayoutCompleted(state);
                // Determines whether the add another account button must be visible or not. It will
                // only be visible if the bottom of the last child of the recycler view does not collide
                // with the top of the add another account button.
                final int lastChildPosition = findLastVisibleItemPosition();
                if (lastChildPosition >= 0) {
                    final View lastChild = recyclerView.getChildAt(lastChildPosition);
                    if (ObjectHelper.isNotNull(lastChild) && ObjectHelper.isNotNull(addAnotherAccountButton)) {
                        final int lastChildBottom = lastChild.getBottom();
                        final int buttonTop = addAnotherAccountButton.getTop();
                        final boolean flag = lastChildBottom < buttonTop;
                        addAnotherAccountButton.setEnabled(flag);
                        addAnotherAccountButton.setVisibility(flag ? View.VISIBLE : View.INVISIBLE);
                        Timber.d("RecyclerView.LastChild.Bottom (%1$d) < Button.Top (%2$d) = %3$s",
                                lastChildBottom, buttonTop, flag
                        );
                    }
                }
            }
        };
        recyclerView.setLayoutManager(layoutManager);
        final RecyclerView.ItemDecoration divider = new HorizontalDividerItemDecoration.Builder(context)
                .drawable(R.drawable.divider_line_horizontal)
                .marginResId(R.dimen.space_horizontal_20)
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
        inflater.inflate(R.menu.d_products, menu);
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
    public void onBalanceQueried(Product product, Pair<Long, Balance> balance) {
        PinConfirmationDialogFragment.dismiss(
                getChildFragmentManager(),
                ObjectHelper.isNotNull(balance)
        );
        setBalance(product, balance);
    }

    @Override
    public void setBalance(@NonNull Product product, Pair<Long, Balance> balance) {
        final ProductItem item = ProductItem.create(product);
        if (adapter.contains(item)) {
            final int index = adapter.indexOf(item);
            final ProductItem actualItem = (ProductItem) adapter.get(index);
            actualItem.setBalance(balance);
            adapter.set(index, actualItem);
        }
    }

    @Override
    public void showGenericErrorDialog(String title, String message) {
        Dialogs.builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.error_positive_button_text, (dialog, which) -> {
                    if (message.contains(getString(R.string.session_expired))) {
                        closeSession();
                    }
                })
                .show();
    }

    @Override
    public void showGenericErrorDialog(String message) {
        showGenericErrorDialog(getString(R.string.error_generic_title), message);
    }

    @Override
    public void showGenericErrorDialog() {
        showGenericErrorDialog(getString(R.string.error_generic));
    }

    @Override
    public void showUnavailableNetworkError() {
        Toast.makeText(getContext(), R.string.error_unavailable_network, Toast.LENGTH_LONG)
                .show();
    }

    @Nullable
    public LoadIndicator getRefreshIndicator() {
        if (ObjectHelper.isNull(loadIndicator) && ObjectHelper.isNotNull(swipeRefreshLayout)) {
            loadIndicator = new SwipeRefreshLayoutRefreshIndicator(swipeRefreshLayout);
        }
        return loadIndicator;
    }

    @Override
    public void onClick(int position) {
        final int[] location = Views.getLocationOnScreen(
                recyclerView.findViewHolderForAdapterPosition(position));
        queryBalance(
                ((ProductItem) adapter.get(position)).getProduct(),
                location[0],
                location[1]
        );
    }

    @Override
    public void onQueryBalanceButtonPressed(int position, int x, int y) {
        queryBalance(((ProductItem) adapter.get(position)).getProduct(), x, y);
    }

    @Override
    public void onShowRecentTransactionsButtonClicked() {
        startActivity(RecentTransactionsActivityBase.getLaunchIntent(getContext()));
    }

    private void closeSession() {
        this.closeSessionDisposable = getContainer().getComponent().sessionManager().closeSession()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> this.showTakeOver())
                .doFinally(this::dismissTakeOverLoader)
                .subscribe(this::handleCloseSession, (Consumer<Throwable>) throwable -> {
                    Log.d("com.tpago.mobile", throwable.getMessage(), throwable);
                });
    }

    private void showTakeOver() {
        if (takeoverLoader != null) {
            takeoverLoader.dismiss();
        } else {
            takeoverLoader = TakeoverLoaderDialogFragment.create();
            getChildFragmentManager().beginTransaction()
                    .add(takeoverLoader, TAKE_OVER_LOADER_DIALOG)
                    .show(takeoverLoader)
                    .commit();
        }

    }

    private void dismissTakeOverLoader() {
        if (takeoverLoader != null) {
            takeoverLoader.dismiss();
            takeoverLoader = null;
        }
    }

    private void handleCloseSession() {
        Intent intent = InitActivityBase.getLaunchIntent(getContext());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        this.getActivity().finish();
        this.startActivity(intent);
    }
}
