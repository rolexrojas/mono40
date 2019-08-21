package com.tpago.movil.d.ui.main.recipient.index.category;

import static com.tpago.movil.d.ui.main.recipient.index.category.Category.PAY;
import static com.tpago.movil.d.ui.main.recipient.index.category.Category.TRANSFER;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.activity.toolbar.ActivityToolbar;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoaderDialogFragment;
import com.tpago.movil.app.ui.main.transaction.paypal.PayPalTransactionArgument;
import com.tpago.movil.app.ui.main.transaction.summary.TransactionSummaryUtil;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.company.partner.PartnerStore;
import com.tpago.movil.d.domain.AccountRecipient;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.UserRecipient;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.data.util.BinderFactory;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.ui.DepActivityBase;
import com.tpago.movil.d.ui.Dialogs;
import com.tpago.movil.d.ui.main.DepMainActivityBase;
import com.tpago.movil.d.ui.main.MainContainer;
import com.tpago.movil.d.ui.main.PinConfirmationDialogFragment;
import com.tpago.movil.d.ui.main.list.ListItemAdapter;
import com.tpago.movil.d.ui.main.list.ListItemHolder;
import com.tpago.movil.d.ui.main.list.ListItemHolderCreatorFactory;
import com.tpago.movil.d.ui.main.list.NoResultsListItemItem;
import com.tpago.movil.d.ui.main.list.NoResultsListItemHolder;
import com.tpago.movil.d.ui.main.list.NoResultsListItemHolderBinder;
import com.tpago.movil.d.ui.main.list.NoResultsListItemHolderCreator;
import com.tpago.movil.d.ui.main.recipient.addition.AddRecipientActivityBase;
import com.tpago.movil.d.ui.main.recipient.index.category.selectbank.BankListFragment;
import com.tpago.movil.d.ui.main.recipient.index.category.selectcarrier.CarrierSelectFragment;
import com.tpago.movil.d.ui.main.transaction.TransactionCategory;
import com.tpago.movil.d.ui.main.transaction.TransactionCreationActivityBase;
import com.tpago.movil.d.ui.main.transaction.own.OwnTransactionCreationActivity;
import com.tpago.movil.d.ui.view.widget.FullScreenLoadIndicator;
import com.tpago.movil.d.ui.view.widget.LoadIndicator;
import com.tpago.movil.d.ui.ChildFragment;
import com.tpago.movil.d.ui.view.widget.SearchView;
import com.tpago.movil.d.ui.view.widget.SwipeRefreshLayoutRefreshIndicator;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.dep.init.InitActivityBase;
import com.tpago.movil.paypal.PayPalAccount;
import com.tpago.movil.util.ObjectHelper;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * {@link RecipientCategoryScreen Screen} implementation that uses a {@link ChildFragment fragment}
 * as container.
 *
 * @author hecvasro
 */
public class RecipientCategoryFragment
        extends ChildFragment<MainContainer>
        implements RecipientCategoryScreen,
        ListItemHolder.OnClickListener,
        OnSaveButtonClickedListener,
        CarrierSelectFragment.CarrierSelectFragmentCallback {

    private static final int REQUEST_CODE_RECIPIENT_ADDITION = 0;
    private static final int REQUEST_CODE_TRANSACTION_CREATION = 1;
    private static final int REQUEST_CODE_OWN_TRANSACTION_CREATION = 3;

    private static final int REQUEST_CODE_TRANSACTION = 42;

    private static final String KEY_CATEGORY = "category";
    private static final String TAKE_OVER_LOADER_DIALOG = "TAKE_OVER_LOADER_DIALOG";

    private Unbinder unbinder;
    private ListItemAdapter adapter;

    private LoadIndicator loadIndicator;
    private LoadIndicator fullScreenLoadIndicator;
    private LoadIndicator currentLoadIndicator;

    private Pair<Integer, Pair<Recipient, String>> requestResult;

    private RecipientListItemHolderBinder recipientBinder;

    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Inject
    StringHelper stringHelper;

    @Inject
    PartnerStore partnerStore;
    @Inject
    Category category;
    @Inject
    CompanyHelper companyHelper;
    @Inject
    RecipientCategoryPresenter presenter;

    @Inject
    StringMapper stringMapper;
    @Inject
    AlertManager alertManager;
    @Inject
    ProductManager productManager;
    private Disposable closeSessionDisposable;
    TakeoverLoaderDialogFragment takeoverLoader;

    @NonNull
    public static RecipientCategoryFragment create(Category category) {
        final Bundle bundle = new Bundle();
        bundle.putString(KEY_CATEGORY, category.name());
        final RecipientCategoryFragment fragment = new RecipientCategoryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Prepares the fragment.
        setHasOptionsMenu(true);
        // Injects all the annotated dependencies.
        final String categoryName = getArguments()
                .getString(KEY_CATEGORY);
        final Category category = Category.valueOf(categoryName);
        final RecipientCategoryComponent component = DaggerRecipientCategoryComponent.builder()
                .depMainComponent(getContainer().getComponent())
                .recipientCategoryModule(new RecipientCategoryModule(category, (DepActivityBase) getActivity()))
                .build();
        component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.d_fragment_payments, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Binds all the annotated views and methods.
        unbinder = ButterKnife.bind(this, view);
        // Prepares the actions and recipients list.
        final ListItemHolderCreatorFactory holderCreatorFactory = new ListItemHolderCreatorFactory
                .Builder()
                .addCreator(Recipient.class, new RecipientListItemHolderCreator(this))
                .addCreator(Action.class, new ActionListItemHolderCreator(this))
                .addCreator(NoResultsListItemItem.class, new NoResultsListItemHolderCreator())
                .build();
        final Context context = getContext();
        recipientBinder = new RecipientListItemHolderBinder(this.category, this.companyHelper, this.partnerStore);
        final BinderFactory binderFactory = new BinderFactory.Builder()
                .addBinder(
                        Recipient.class,
                        RecipientListItemHolder.class,
                        recipientBinder
                )
                .addBinder(
                        Action.class,
                        ActionListItemHolder.class,
                        new ActionListItemHolderBinder(stringHelper, category)
                )
                .addBinder(
                        NoResultsListItemItem.class,
                        NoResultsListItemHolder.class,
                        new NoResultsListItemHolderBinder(context)
                )
                .build();
        adapter = new ListItemAdapter(holderCreatorFactory, binderFactory);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView
                .setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        final RecyclerView.ItemDecoration divider = new HorizontalDividerItemDecoration.Builder(context)
                .drawable(R.drawable.divider_line_horizontal)
                .marginResId(R.dimen.space_horizontal_20)
                .showLastDivider()
                .build();
        recyclerView.addItemDecoration(divider);
        // Attaches the screen to the presenter.
        presenter.attachScreen(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Sets the title.
        this.getContainer()
                .setTitle(this.getString(this.category.stringId));
        // Sets the hint of the search box.
        final int hintId;
        if (this.category == PAY) {
            hintId = R.string.pay_screen_search_hint_pay;
        } else if (this.category == TRANSFER) {
            hintId = R.string.pay_screen_search_hint_transfer;
        } else {
            hintId = R.string.pay_screen_search_hint_recharge;
        }
        this.searchView.setHint(this.getString(hintId));
        // Starts the presenter.
        presenter.attachScreen(this);
        presenter.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ObjectHelper.isNotNull(requestResult)) {
            final Recipient recipient = requestResult.second.first;
            final int code = requestResult.first;
            if (code == REQUEST_CODE_TRANSACTION_CREATION || code == REQUEST_CODE_OWN_TRANSACTION_CREATION) {
                presenter.showTransactionSummary(recipient, requestResult.second.second);
            } else if (code == REQUEST_CODE_RECIPIENT_ADDITION) {
                presenter.addRecipient(recipient, category);
            }
            requestResult = null;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.recipient_index_category, menu);

        final String categoryDeletionString = this.getString(this.category.subjectStringId)
                .toLowerCase();
        final MenuItem addMenuItem = menu.findItem(R.id.recipientIndexCategory_menuItem_add);
        addMenuItem
                .setTitle(String.format(this.getString(R.string.format_add), categoryDeletionString));
        final MenuItem removeMenuItem = menu.findItem(R.id.recipientIndexCategory_menuItem_remove);
        removeMenuItem
                .setTitle(String.format(this.getString(R.string.format_remove), categoryDeletionString));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.recipientIndexCategory_menuItem_add:
                startActivityForResult(
                        AddRecipientActivityBase.getLaunchIntent(this.getContext(), this.category),
                        REQUEST_CODE_RECIPIENT_ADDITION
                );
                return true;
            case R.id.recipientIndexCategory_menuItem_remove:
                presenter.startDeleting();
                return true;
            default:
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RECIPIENT_ADDITION) {
            if (resultCode == Activity.RESULT_OK) {
                final Recipient recipient = AddRecipientActivityBase.deserializeResult(data);
                if (ObjectHelper.isNotNull(recipient)) {
                    requestResult = Pair.create(requestCode, Pair.create(recipient, (String) null));
                }
            }
        } else if (requestCode == REQUEST_CODE_TRANSACTION_CREATION) {
            if (resultCode == Activity.RESULT_OK) {
                final Pair<Recipient, String> result = TransactionCreationActivityBase.deserializeResult(
                        data);
                if (ObjectHelper.isNotNull(result)) {
                    requestResult = Pair.create(requestCode, result);
                }
            }
        } else if (requestCode == REQUEST_CODE_OWN_TRANSACTION_CREATION) {
            if (resultCode == Activity.RESULT_OK) {
                final String transactionId = OwnTransactionCreationActivity.deserializeResult(data);
                if (ObjectHelper.isNotNull(transactionId)) {
                    requestResult = Pair.create(requestCode, Pair.create((Recipient) null, transactionId));
                }
            }
        } else if (requestCode == REQUEST_CODE_TRANSACTION) {
            if (resultCode == Activity.RESULT_OK) {
                com.tpago.movil.app.ui.main.transaction.summary.TransactionSummaryDialogFragment
                        .create(TransactionSummaryUtil.unwrap(data))
                        .show(this.getFragmentManager(), null);
            }
        }
    }

    @NonNull
    @Override
    public Observable<String> onQueryChanged() {
        return searchView.onQueryChanged();
    }

    @Override
    public void clearQuery() {
        searchView.clear();
    }

    @Override
    public void showLoadIndicator(boolean fullscreen) {
        if (fullscreen) {
            if (ObjectHelper.isNull(fullScreenLoadIndicator)) {
                fullScreenLoadIndicator = new FullScreenLoadIndicator(getChildFragmentManager());
            }
            currentLoadIndicator = fullScreenLoadIndicator;
        } else {
            if (ObjectHelper.isNull(loadIndicator)) {
                loadIndicator = new SwipeRefreshLayoutRefreshIndicator(swipeRefreshLayout);
            }
            currentLoadIndicator = loadIndicator;
        }
        currentLoadIndicator.show();
    }

    @Override
    public void hideLoadIndicator() {
        if (ObjectHelper.isNotNull(currentLoadIndicator)) {
            currentLoadIndicator.hide();
            currentLoadIndicator = null;
        }
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
    public void update(@NonNull Object item) {
        adapter.updateOrAdd(item);
    }

    @Override
    public void setDeleting(boolean deleting) {
        final DepMainActivityBase activity = (DepMainActivityBase) getActivity();
        if (deleting) {
            recipientBinder.setDeleting(true);
            activity.showDeleteLinearLayout();
            activity.setDeleteButtonEnabled(false);
            activity.setOnDeleteButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.deleteSelectedRecipients();
                }
            });
            activity.setOnCancelButtonClickedListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.stopDeleting();
                }
            });
            activity.setOnBackPressedListener(new DepMainActivityBase.OnBackPressedListener() {
                @Override
                public boolean onBackPressed() {
                    presenter.stopDeleting();
                    return true;
                }
            });
        } else {
            recipientBinder.setDeleting(false);
            activity.hideDeleteLinearLayout();
            activity.setDeleteButtonEnabled(false);
            activity.setOnDeleteButtonClickListener(null);
            activity.setOnCancelButtonClickedListener(null);
            activity.setOnBackPressedListener(null);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void startTransaction(Recipient recipient) {
        startActivityForResult(
                TransactionCreationActivityBase.getLaunchIntent(
                        this.getActivity(),
                        TransactionCategory.transform(this.category),
                        recipient
                ),
                REQUEST_CODE_TRANSACTION_CREATION
        );
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void remove(Object item) {
        adapter.remove(item);
    }

    @Override
    public void setDeleteButtonEnabled(boolean enabled) {
        ((DepMainActivityBase) getActivity()).setDeleteButtonEnabled(enabled);
    }

    @Override
    public void showRecipientAdditionDialog(Recipient recipient) {
        RecipientAdditionDialogFragment.create(recipient)
                .show(getChildFragmentManager(), null);
    }

    @Override
    public void showTransactionSummary(
            Recipient recipient,
            boolean alreadyExists,
            String transactionId
    ) {
        TransactionSummaryDialogFragment.create(recipient, alreadyExists, transactionId)
                .show(getChildFragmentManager(), null);
    }

    @Override
    public void requestPin() {
        final View rootView = ButterKnife.findById(getActivity(), android.R.id.content);
        final int x = Math.round((rootView.getRight() - rootView.getLeft()) / 2);
        final int y = Math.round((rootView.getBottom() - rootView.getTop()) / 2);
        PinConfirmationDialogFragment.show(
                getChildFragmentManager(),
                getString(R.string.remove_recipients),
                new PinConfirmationDialogFragment.Callback() {
                    @Override
                    public void confirm(@NonNull String pin) {
                        presenter.onPinRequestFinished(pin);
                    }
                },
                x,
                y
        );
    }

    @Override
    public void showGenericErrorDialog(String message) {
        Dialogs.builder(getContext())
                .setTitle(R.string.error_generic_title)
                .setMessage(message)
                .setPositiveButton(R.string.error_positive_button_text, (dialog, which) -> {
                    if (message.contains(getString(R.string.session_expired))) {
                        closeSession();
                    }
                })
                .show();
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

    @Override
    public void setDeletingResult(boolean result) {
        PinConfirmationDialogFragment.dismiss(getChildFragmentManager(), result);
    }

    @Override
    public void startPayPalTransaction(PayPalAccount recipient) {
        final Intent intent = ActivityToolbar.intentBuilder()
                .context(this.getContext())
                .argument(PayPalTransactionArgument.create(recipient))
                .build();
        this.startActivityForResult(intent, REQUEST_CODE_TRANSACTION);
    }

    @Override
    public void onClick(int position) {
        final Object item = adapter.get(position);
        if (item instanceof Recipient) {
            if (item instanceof UserRecipient) {
                final Context context = this.getContext();
                if (category == TRANSFER) {
                    boolean hasAccounts = false;
                    for (Product product : this.productManager.getProductList()) {
                        if (Product.checkIfAccount(product)) {
                            hasAccounts = true;
                            break;
                        }
                    }
                    if (hasAccounts) {
                        this.startActivityForResult(
                                OwnTransactionCreationActivity.createLaunchIntent(context),
                                REQUEST_CODE_TRANSACTION_CREATION
                        );
                    } else {
                        this.alertManager.builder()
                                .message("No hay cuentas activas asociadas a este teléfono.")
                                .show();
                    }
                } else {
                    this.startActivityForResult(
                            TransactionCreationActivityBase.getLaunchIntent(
                                    context,
                                    TransactionCategory.transform(this.category),
                                    (UserRecipient) item
                            ),
                            REQUEST_CODE_TRANSACTION_CREATION
                    );
                }
            } else {
                presenter.resolve((Recipient) item);
            }
        } else if (item instanceof Action) {
            switch (((Action) item).type()) {
                case ADD_PHONE_NUMBER:
                    presenter.addRecipient(((PhoneNumberAction) item).phoneNumber(), category);
                    break;
                case TRANSACTION_WITH_PHONE_NUMBER:
                    presenter.startTransfer(((PhoneNumberAction) item).phoneNumber());
                    break;
                case ADD_ACCOUNT:
                    presenter.addRecipient(
                            AccountRecipient.builder()
                                    .number(((AccountAction) item).number())
                                    .build(), category
                    );
                    break;
                case TRANSACTION_WITH_ACCOUNT:
                    this.presenter.startTransfer(((AccountAction) item).number());
                    break;
            }
        }
    }

    @Override
    public void onSaveButtonClicked(Recipient recipient, String label) {
        presenter.updateRecipient(recipient, label);
    }

    @Override
    public void showRecipientAdditionCarrierSelection(Recipient recipient) {
        this.getContainer()
                .setChildFragment(CarrierSelectFragment.create(recipient),
                        true, true);
    }

    @Override
    public void onCarrierSelect(Recipient recipient) {
        presenter.onCarrierSelected(recipient);
    }

    @Override
    public void showRecipientAdditionBankSelection(Recipient recipient) {
        this.getContainer()
                .setChildFragment(BankListFragment.create(recipient),
                        true, true);
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
