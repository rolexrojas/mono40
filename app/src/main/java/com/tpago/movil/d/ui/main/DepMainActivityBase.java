package com.tpago.movil.d.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.app.di.ComponentBuilderSupplier;
import com.tpago.movil.app.ui.activity.ActivityQualifier;
import com.tpago.movil.app.ui.activity.base.ActivityModule;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.app.ui.fragment.FragmentReplacer;
import com.tpago.movil.app.ui.main.MainComponent;
import com.tpago.movil.app.ui.main.settings.SettingsFragment;
import com.tpago.movil.app.ui.main.transaction.disburse.index.FragmentDisburseIndex;
import com.tpago.movil.app.ui.main.transaction.insurance.micro.index.MicroInsuranceIndexFragment;
import com.tpago.movil.app.ui.main.transaction.summary.TransactionSummaryUtil;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.RecipientManager;
import com.tpago.movil.d.domain.ResetEvent;
import com.tpago.movil.d.domain.UserRecipient;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.domain.util.EventBus;
import com.tpago.movil.d.ui.ChildFragment;
import com.tpago.movil.d.ui.DepActivityModule;
import com.tpago.movil.d.ui.SwitchableContainerActivityBase;
import com.tpago.movil.d.ui.main.products.ProductsFragment;
import com.tpago.movil.d.ui.main.purchase.NonNfcPurchaseFragment;
import com.tpago.movil.d.ui.main.purchase.PurchaseFragment;
import com.tpago.movil.d.ui.main.recipient.addition.AddRecipientActivityBase;
import com.tpago.movil.d.ui.main.recipient.index.category.OnSaveButtonClickedListener;
import com.tpago.movil.d.ui.main.recipient.index.category.RecipientCategoryFragment;
import com.tpago.movil.d.ui.main.recipient.index.category.TransactionSummaryDialogFragment;
import com.tpago.movil.d.ui.main.transaction.TransactionCreationActivityBase;
import com.tpago.movil.d.ui.main.transaction.own.OwnTransactionCreationActivity;
import com.tpago.movil.d.ui.qr.QrActivity;
import com.tpago.movil.d.ui.view.widget.SlidingPaneLayout;
import com.tpago.movil.dep.App;
import com.tpago.movil.dep.TimeOutManager;
import com.tpago.movil.dep.init.InitActivityBase;
import com.tpago.movil.dep.main.MainModule;
import com.tpago.movil.dep.widget.Keyboard;
import com.tpago.movil.reactivex.DisposableUtil;
import com.tpago.movil.session.SessionManager;
import com.tpago.movil.transaction.TransactionSummary;
import com.tpago.movil.util.LogoutTimerService;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.RootUtil;
import com.tpago.movil.util.UiUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.tpago.movil.d.ui.main.recipient.index.category.Category.PAY;
import static com.tpago.movil.d.ui.main.recipient.index.category.Category.RECHARGE;
import static com.tpago.movil.d.ui.main.recipient.index.category.Category.TRANSFER;

/**
 * @author hecvasro
 */
@Deprecated
public class DepMainActivityBase
        extends SwitchableContainerActivityBase<DepMainComponent>
        implements MainContainer,
        MainScreen,
        TimeOutManager.TimeOutHandler {
    private static final int REQUEST_CODE_RECIPIENT_ADDITION = 0;
    private static final int REQUEST_CODE_TRANSACTION_CREATION = 1;
    private static final int REQUEST_CODE_OWN_TRANSACTION_CREATION = 3;
    private static final int REQUEST_CODE_TRANSACTION = 42;
    private Pair<Integer, Pair<Recipient, String>> requestResult;


    @NonNull
    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, DepMainActivityBase.class);
    }

    public static DepMainActivityBase get(Activity activity) {
        ObjectHelper.checkNotNull(activity, "activity");
        if (!(activity instanceof DepMainActivityBase)) {
            throw new ClassCastException("!(activity instanceof DepMainActivityBase)");
        }
        return (DepMainActivityBase) activity;
    }

    public final Toolbar toolbar() {
        return this.toolbar;
    }

    private Unbinder unbinder;
    private DepMainComponent component;
    private OnBackPressedListener onBackPressedListener;

    private boolean shouldRequestAuthentication = false;

    @Inject
    @ActivityQualifier
    ComponentBuilderSupplier componentBuilderSupplier;
    @Inject
    @ActivityQualifier
    FragmentReplacer fragmentReplacer;

    @Inject
    AlertManager alertManager;

    @Inject
    EventBus eventBus;
    @Inject
    MainPresenter presenter;
    @Inject
    PosBridge posBridge;
    @Inject
    ProductManager productManager;
    @Inject
    SessionManager sessionManager;
    @Inject
    StringHelper depStringHelper;
    @Inject
    TimeOutManager timeOutManager;
    @Inject
    RecipientManager recipientManager;

    @BindView(R.id.sliding_pane_layout)
    SlidingPaneLayout slidingPaneLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.linear_layout_delete)
    LinearLayout deleteLinearLayout;
    @BindView(R.id.image_button_cancel)
    ImageButton cancelImageButton;
    @BindView(R.id.image_button_delete)
    ImageButton deleteImageButton;

    private Disposable closeSessionDisposable = Disposables.disposed();

    private void handleCloseSessionSuccess() {
        this.startActivity(InitActivityBase.getLaunchIntent(this));
        this.finish();
    }

    private void handleCloseSessionError(Throwable throwable) {
        Timber.e(throwable, "Closing session");
        this.alertManager.showAlertForGenericFailure();
    }

    private void closeSession() {
        this.closeSessionDisposable = this.sessionManager.closeSession()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> this.takeoverLoader.show())
                .doFinally(this.takeoverLoader::hide)
                .subscribe(this::handleCloseSessionSuccess, this::handleCloseSessionError);
    }

    public final ComponentBuilderSupplier componentBuilderSupplier() {
        return this.componentBuilderSupplier;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        eventBus.dispatch(new ResetEvent());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected int layoutResId() {
        return R.layout.d_activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.unbinder = ButterKnife.bind(this);
        // Injects all the annotated dependencies.
        this.component = App.get(this)
                .componentBuilderSupplier()
                .get(DepMainActivityBase.class, MainComponent.Builder.class)
                .activityModule(ActivityModule.create(this))
                .depActivityModule(new DepActivityModule(this))
                .mainModule(new MainModule(this))
                .build();
        this.component.inject(this);
        // Prepares the action bar.
        this.setSupportActionBar(this.toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (ObjectHelper.isNotNull(actionBar)) {
            actionBar.setDisplayShowTitleEnabled(true);
        }
        // Prepares the toolbar.
        this.toolbarTitle.setText(R.string.pay);
        this.toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        this.toolbar.setNavigationOnClickListener((view) -> {
            if (this.slidingPaneLayout.isOpen()) {
                this.slidingPaneLayout.closePane();
            } else {
                this.slidingPaneLayout.openPane();
            }
            Keyboard.hide(this);
        });
        // Sets the startup screen.
        this.setChildFragment(RecipientCategoryFragment.create(PAY), false, false);
        // Attaches the screen to the presenter.
        this.presenter.attachScreen(this);
        // Creates the presenter.
        this.presenter.create();

        this.timeOutManager.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (this.shouldRequestAuthentication) {
            this.shouldRequestAuthentication = false;
            this.closeSession();
        } else {
            this.presenter.start();
            startService(new Intent(this, LogoutTimerService.class));
        }
    }

    @Override
    protected void onStop() {
        DisposableUtil.dispose(this.closeSessionDisposable);

        this.presenter.stop();

        super.onStop();
    }

    final void showTransactionSummary(final Recipient recipient, final String transactionId) {
        showTransactionSummaryP(
                recipient,
                this.recipientManager.checkIfExists(recipient),
                transactionId
        );
    }

    public void showTransactionSummaryP(
            Recipient recipient,
            boolean alreadyExists,
            String transactionId
    ) {
        TransactionSummaryDialogFragment dialogFragment = TransactionSummaryDialogFragment.create(recipient, alreadyExists, transactionId);
        dialogFragment.setListener((recipient1, label) -> updateRecipient(recipient1, label));
        dialogFragment.show(getSupportFragmentManager(), null);

    }

    final void updateRecipient(@NonNull Recipient recipient, @Nullable String label) {
        if (!(recipient instanceof UserRecipient)) {
            recipient.setLabel(label);
            this.recipientManager.update(recipient);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (RootUtil.isDeviceRooted()) {
            RootUtil.showRootErrorDialog(this, this);
        }
        if (ObjectHelper.isNotNull(requestResult)) {
            final Recipient recipient = requestResult.second.first;
            final int code = requestResult.first;
            if (code == REQUEST_CODE_TRANSACTION_CREATION || code == REQUEST_CODE_OWN_TRANSACTION_CREATION) {
                showTransactionSummary(recipient, requestResult.second.second);
            }
            requestResult = null;
        }
    }

    @Override
    protected void onDestroy() {
        if (this.sessionManager.isUserSet() && this.sessionManager.isSessionOpen()) {
            this.handleTimeOut();
        }
        super.onDestroy();
        timeOutManager.stop();

        // Destroys the presenter.
        presenter.destroy();
        // Detaches the screen from the presenter.
        presenter.detachScreen();

        unbinder.unbind();
    }

    private void setChildFragment(ChildFragment<MainContainer> childFragment) {
        this.setChildFragment(childFragment, true, true);
    }

    @OnClick({
            R.id.main_menuItem_pay,
            R.id.main_menuItem_purchase,
            R.id.main_menuItem_transfer,
            R.id.main_menuItem_recharge,
            R.id.main_menuItem_disburse,
            R.id.main_menuItem_wallet,
            R.id.main_menuItem_insurance,
            R.id.main_menuItem_settings,
            R.id.main_menuItem_exit
    })
    final void onMenuItemButtonClicked(@NonNull View view) {
        if (this.slidingPaneLayout.isOpen()) {
            this.slidingPaneLayout.closePane();
        }

        switch (view.getId()) {
            case R.id.main_menuItem_pay:
                this.toolbarTitle.setText(R.string.pay);
                this.setChildFragment(RecipientCategoryFragment.create(PAY));
                break;
            case R.id.main_menuItem_purchase:
                this.toolbarTitle.setText(R.string.buy);
                if (this.posBridge.isAvailable()) {
                    this.setChildFragment(PurchaseFragment.newInstance());
                } else {
                    this.setChildFragment(NonNfcPurchaseFragment.create());
                }
                break;
            case R.id.main_menuItem_transfer:
                this.toolbarTitle.setText(R.string.transfer);
                this.setChildFragment(RecipientCategoryFragment.create(TRANSFER));
                break;
            case R.id.main_menuItem_recharge:
                this.toolbarTitle.setText(R.string.recharge);
                this.setChildFragment(RecipientCategoryFragment.create(RECHARGE));
                break;
            case R.id.main_menuItem_disburse:
                this.toolbarTitle.setText(R.string.withdraw);
                this.fragmentReplacer.begin(FragmentDisburseIndex.create())
                        .transition(FragmentReplacer.Transition.FIFO)
                        .addToBackStack()
                        .commit();
                break;
            case R.id.main_menuItem_wallet:
                this.toolbarTitle.setText(R.string.products);
                this.setChildFragment(ProductsFragment.newInstance());
                break;
            case R.id.main_menuItem_insurance:
                this.toolbarTitle.setText(R.string.main_transaction_insurance_micro);
                this.fragmentReplacer.begin(MicroInsuranceIndexFragment.create())
                        .transition(FragmentReplacer.Transition.FIFO)
                        .addToBackStack()
                        .commit();
                break;
            case R.id.main_menuItem_settings:
                this.toolbarTitle.setText(R.string.settings);
                this.fragmentReplacer.begin(SettingsFragment.create())
                        .transition(FragmentReplacer.Transition.FIFO)
                        .addToBackStack()
                        .commit();
                break;
            case R.id.main_menuItem_exit:
                this.closeSession();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (this.slidingPaneLayout.isOpen()) {
            this.slidingPaneLayout.closePane();
            return;
        }

        if (ObjectHelper.isNotNull(this.onBackPressedListener)) {
            final boolean flag = this.onBackPressedListener.onBackPressed();
            if (flag) {
                return;
            }
        }

        final FragmentManager manager = this.fragmentReplacer.manager();
        if (manager.getBackStackEntryCount() > 0) {
            super.onBackPressed();
            return;
        }

        this.closeSession();
    }

    @Nullable
    @Override
    public DepMainComponent getComponent() {
        return component;
    }

    @Override
    public void setTitle(@Nullable String title) {
        final ActionBar actionBar = getSupportActionBar();
        if (ObjectHelper.isNotNull(actionBar)) {
            actionBar.setTitle(title);
        }
    }

    @Override
    public void openPurchaseScreen() {
        final ChildFragment<MainContainer> childFragment;
        if (posBridge.isAvailable()) {
            childFragment = PurchaseFragment.newInstance();
        } else {
            childFragment = NonNfcPurchaseFragment.create();
        }
        setChildFragment(childFragment, true, true);
    }

    public void showDeleteLinearLayout() {
        deleteLinearLayout.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.INVISIBLE);
        toolbar.setEnabled(false);
    }

    public void setOnCancelButtonClickedListener(View.OnClickListener listener) {
        cancelImageButton.setOnClickListener(listener);
    }

    public void setDeleteButtonEnabled(boolean enabled) {
        UiUtil.setEnabled(deleteImageButton, enabled);
    }

    public void setOnDeleteButtonClickListener(View.OnClickListener listener) {
        deleteImageButton.setOnClickListener(listener);
    }

    public void hideDeleteLinearLayout() {
        toolbar.setEnabled(true);
        toolbar.setVisibility(View.VISIBLE);
        deleteLinearLayout.setVisibility(View.GONE);
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        timeOutManager.reset();
    }

    @Override
    public void handleTimeOut() {
        if (App.get(this)
                .isVisible()) {
            this.closeSession();
        } else {
            this.shouldRequestAuthentication = true;
        }
    }

    @OnClick(R.id.qr_code_icon)
    public void onQrCodeIconClick(View view) {
        Intent intent = new Intent(this, QrActivity.class);
        startActivityForResult(intent, REQUEST_CODE_TRANSACTION_CREATION);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                Log.d("com.tpago.mobile", "QR_CODE_RECIPIENT = " + result.first.getId());
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
            Log.d("com.tpago.mobile", "resultCode = " + (resultCode == Activity.RESULT_OK));
            if (data != null && data.getExtras() != null) {
                Log.d("com.tpago.mobile", "resultData = " + data.getExtras().toString());
                for (String key : data.getExtras().keySet()) {
                    Log.d("com.tpago.mobile", key + " is a key in the bundle = " + data.getExtras().getString(key));
                }
            }
            if (resultCode == Activity.RESULT_OK && data != null) {
                TransactionSummary transactionSummary = TransactionSummaryUtil.unwrap(data);
                if (transactionSummary != null) {
                    com.tpago.movil.app.ui.main.transaction.summary.TransactionSummaryDialogFragment
                            .create(transactionSummary)
                            .show(getSupportFragmentManager(), null);
                }
            }
        }
    }

    public interface OnBackPressedListener {

        boolean onBackPressed();
    }
}
