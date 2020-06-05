package com.mono40.movil.d.ui.main;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.core.util.Pair;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mono40.movil.R;
import com.mono40.movil.app.di.ComponentBuilderSupplier;
import com.mono40.movil.app.ui.activity.ActivityQualifier;
import com.mono40.movil.app.ui.activity.base.ActivityModule;
import com.mono40.movil.app.ui.alert.AlertManager;
import com.mono40.movil.app.ui.fragment.FragmentReplacer;
import com.mono40.movil.app.ui.main.MainComponent;
import com.mono40.movil.app.ui.main.settings.SettingsFragment;
import com.mono40.movil.app.ui.main.transaction.disburse.index.FragmentDisburseIndex;
import com.mono40.movil.app.ui.main.transaction.insurance.micro.index.MicroInsuranceIndexFragment;
import com.mono40.movil.app.ui.main.transaction.summary.TransactionSummaryUtil;
import com.mono40.movil.d.data.StringHelper;
import com.mono40.movil.d.domain.MerchantRecipient;
import com.mono40.movil.d.domain.ProductManager;
import com.mono40.movil.d.domain.Recipient;
import com.mono40.movil.d.domain.RecipientManager;
import com.mono40.movil.d.domain.ResetEvent;
import com.mono40.movil.d.domain.UserRecipient;
import com.mono40.movil.d.domain.pos.PosBridge;
import com.mono40.movil.d.domain.util.EventBus;
import com.mono40.movil.d.ui.ChildFragment;
import com.mono40.movil.d.ui.DepActivityModule;
import com.mono40.movil.d.ui.SwitchableContainerActivityBase;
import com.mono40.movil.d.ui.main.products.ProductsFragment;
import com.mono40.movil.d.ui.main.purchase.NonNfcPurchaseFragment;
import com.mono40.movil.d.ui.main.purchase.PurchaseFragment;
import com.mono40.movil.d.ui.main.recipient.addition.AddRecipientActivityBase;
import com.mono40.movil.d.ui.main.recipient.index.category.RecipientCategoryFragment;
import com.mono40.movil.d.ui.main.recipient.index.category.TransactionSummaryDialogFragment;
import com.mono40.movil.d.ui.main.transaction.TransactionCreationActivityBase;
import com.mono40.movil.d.ui.main.transaction.TransactionResult;
import com.mono40.movil.d.ui.main.transaction.own.OwnTransactionCreationActivity;
import com.mono40.movil.d.ui.qr.QrActivity;
import com.mono40.movil.d.ui.qr.QrScannerFragment;
import com.mono40.movil.d.ui.view.widget.SlidingPaneLayout;
import com.mono40.movil.dep.App;
import com.mono40.movil.dep.TimeOutManager;
import com.mono40.movil.dep.init.InitActivityBase;
import com.mono40.movil.dep.main.MainModule;
import com.mono40.movil.dep.widget.Keyboard;
import com.mono40.movil.reactivex.DisposableUtil;
import com.mono40.movil.session.SessionManager;
import com.mono40.movil.transaction.TransactionSummary;
import com.mono40.movil.util.LogoutTimerService;
import com.mono40.movil.util.ObjectHelper;
import com.mono40.movil.util.RootUtil;
import com.mono40.movil.util.UiUtil;

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

import static com.mono40.movil.d.ui.main.recipient.index.category.Category.PAY;
import static com.mono40.movil.d.ui.main.recipient.index.category.Category.RECHARGE;
import static com.mono40.movil.d.ui.main.recipient.index.category.Category.TRANSFER;

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
    public static final String QR_SCANNER_WINDOW = "QR_SCANNER_WINDOW";

    @NonNull
    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, DepMainActivityBase.class);
    }

    public static DepMainActivityBase get(Activity activity) {
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
    @BindView(R.id.qr_code_icon)
    View qrCodeIcon;
    private LocalBroadcastManager localBroadcastManager;
    private LogoutReceiver logoutReceiver;

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
        startService(new Intent(this, LogoutTimerService.class));
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
        qrCodeIcon.setVisibility(View.VISIBLE);
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

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        logoutReceiver = new LogoutReceiver();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (this.shouldRequestAuthentication) {
            this.shouldRequestAuthentication = false;
            this.closeSession();
        } else {
            this.presenter.start();
            IntentFilter intentFilter = new IntentFilter(LogoutTimerService.LOGOUT_BROADCAST);
            localBroadcastManager.registerReceiver(logoutReceiver, intentFilter);
            localBroadcastManager.sendBroadcast(new Intent(LogoutTimerService.USER_INTERACTION_BROADCAST));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        localBroadcastManager.unregisterReceiver(logoutReceiver);
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
        dialogFragment.setListener((recipient1, label) -> {
            recipient1.setLabel(label);
            updateRecipient(recipient1, label);
            ((RecipientCategoryFragment) getSupportFragmentManager().findFragmentByTag(TAG_CHILD_FRAGMENT)).update(recipient);
        });
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
        timeOutManager.reset();
        if (RootUtil.isDeviceRooted()) {
            RootUtil.showRootErrorDialog(this, this);
        }
        qrCodeIcon.setEnabled(true);
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
                qrCodeIcon.setVisibility(View.VISIBLE);
                break;
            case R.id.main_menuItem_purchase:
                this.toolbarTitle.setText(R.string.buy);
                if (false) {
                    this.setChildFragment(PurchaseFragment.newInstance());
                } else {
                    //this.setChildFragment(NonNfcPurchaseFragment.create());
                    LaunchQrReader(view);
                }
                qrCodeIcon.setVisibility(View.VISIBLE);
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
        localBroadcastManager.sendBroadcast(new Intent(LogoutTimerService.USER_INTERACTION_BROADCAST));
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
        view.setEnabled(false);
        Intent intent = new Intent(this, QrActivity.class);
        startActivityForResult(intent, REQUEST_CODE_TRANSACTION_CREATION);

    }

    private void LaunchQrReader(View view){
        view.setEnabled(false);
        Intent intent = new Intent(this, QrActivity.class);
        intent.putExtra(QR_SCANNER_WINDOW, "1");
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
                final TransactionResult result = TransactionCreationActivityBase.deserializeTransactionResult(
                        data);
                View view = new View(this);
                if (result.getRecipient() instanceof MerchantRecipient) {
                    view.setId(R.id.main_menuItem_pay);
                }
                onMenuItemButtonClicked(view);
                Log.d("com.cryptoqr.mobile", "QR_CODE_RECIPIENT = " + result.getTransactionId());
                if (ObjectHelper.isNotNull(result)) {
                    requestResult = Pair.create(requestCode, TransactionCreationActivityBase.deserializeResult(
                            data));
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
            Log.d("com.cryptoqr.mobile", "resultCode = " + (resultCode == Activity.RESULT_OK));
            if (data != null && data.getExtras() != null) {
                Log.d("com.cryptoqr.mobile", "resultData = " + data.getExtras().toString());
                for (String key : data.getExtras().keySet()) {
                    Log.d("com.cryptoqr.mobile", key + " is a key in the bundle = " + data.getExtras().getString(key));
                }
            }
            if (resultCode == Activity.RESULT_OK && data != null) {
                TransactionSummary transactionSummary = TransactionSummaryUtil.unwrap(data);
                if (transactionSummary != null) {
                    com.mono40.movil.app.ui.main.transaction.summary.TransactionSummaryDialogFragment
                            .create(transactionSummary)
                            .show(getSupportFragmentManager(), null);
                }
            }
        }
    }

    public interface OnBackPressedListener {

        boolean onBackPressed();
    }

    class LogoutReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            DepMainActivityBase.this.startActivity(InitActivityBase.getLaunchIntent(DepMainActivityBase.this));
            DepMainActivityBase.this.finish();
        }
    }
}
