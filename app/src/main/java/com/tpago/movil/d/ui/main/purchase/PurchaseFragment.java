package com.tpago.movil.d.ui.main.purchase;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.nfc.NfcAdapter;
import android.nfc.cardemulation.CardEmulation;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoaderDialogFragment;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.data.util.BinderFactory;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.ui.ChildFragment;
import com.tpago.movil.d.ui.DepActivityBase;
import com.tpago.movil.d.ui.Dialogs;
import com.tpago.movil.d.ui.main.DepMainActivityBase;
import com.tpago.movil.d.ui.main.MainContainer;
import com.tpago.movil.d.ui.main.PinConfirmationDialogFragment;
import com.tpago.movil.d.ui.main.list.ListItemAdapter;
import com.tpago.movil.d.ui.main.list.ListItemHolder;
import com.tpago.movil.d.ui.main.list.ListItemHolderCreatorFactory;
import com.tpago.movil.dep.init.InitActivityBase;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author hecvasro
 */
@Deprecated
public class PurchaseFragment
        extends ChildFragment<MainContainer>
        implements PurchaseContainer,
        PurchaseScreen,
        ListItemHolder.OnClickListener,
        SelectedItemDecoration.Provider,
        PurchasePaymentDialogFragment.OnDismissedListener {

    private static final String TAG_PAYMENT_SCREEN = "paymentScreen";
    private static final String TAKE_OVER_LOADER_DIALOG = "TAKE_OVER_LOADER";
    private final String NFC_SERVICE = "com.cube.sdk.hce.TorreHostApduService";  // TODO: Change to PosBridge

    PurchaseComponent component;

    private Unbinder unbinder;
    private ListItemAdapter adapter;
    private boolean hasCards;
    private boolean hasRequestedToMakeAppAsDefaultForNFC;

    @Inject
    StringHelper stringHelper;
    @Inject
    PurchasePaymentOptionBinder paymentOptionBinder;
    @Inject
    PurchasePresenter presenter;

    @BindString(R.string.commerce_payment_option_ready_text_value)
    String readyMessage;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private TakeoverLoaderDialogFragment takeoverLoader;
    private Disposable closeSessionDisposable;

    @NonNull
    public static PurchaseFragment newInstance() {
        return new PurchaseFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Injects all the annotated dependencies.
        component = DaggerPurchaseComponent.builder()
                .depMainComponent(getContainer().getComponent())
                .purchaseModule(new PurchaseModule((DepActivityBase) getActivity()))
                .build();
        component.inject(this);

    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.d_fragment_payments_commerce, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initializes all reusable variables.
        final Context context = getContext();
        // Binds all the annotated views and methods.
        unbinder = ButterKnife.bind(this, view);
        // Prepares the payment options list.
        final ListItemHolderCreatorFactory holderCreatorFactory = new ListItemHolderCreatorFactory
                .Builder()
                .addCreator(Product.class, new PurchasePaymentOptionListItemHolderCreator(this))
                .addCreator(String.class, new TextListItemHolderCreator())
                .build();
        final BinderFactory holderBinderFactory = new BinderFactory.Builder()
                .addBinder(Product.class, PurchasePaymentOptionHolder.class, paymentOptionBinder)
                .addBinder(String.class, TextListItemHolder.class, new TextListItemBinder())
                .build();

        adapter = new ListItemAdapter(holderCreatorFactory, holderBinderFactory);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,
                false
        ));
        final Resources resources = getResources();
        recyclerView.addItemDecoration(
                new SpaceDividerItemDecoration(
                        resources.getDimensionPixelSize(R.dimen.commerce_payment_option_margin)));
        final int borderWidth = resources.getDimensionPixelOffset(
                R.dimen.commerce_payment_option_border_width);
        final int borderColor = ContextCompat.getColor(
                context,
                R.color.d_commerce_payment_option_border
        );
        final int borderRadius = resources.getDimensionPixelOffset(
                R.dimen.commerce_payment_option_border_radius);
        recyclerView.addItemDecoration(new SelectedItemDecoration(this, borderWidth, borderColor,
                borderRadius
        ));
        // Attaches the screen to the presenter.
        presenter.attachScreen(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Sets the title.
        getContainer().setTitle(getString(R.string.screen_payments_commerce_title));
        // Starts the presenter.
        presenter.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.presenter.resume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final DepMainActivityBase activity = (DepMainActivityBase) getActivity();

            if (activity != null) {
                final Context applicationContext = activity.getApplicationContext();
                final CardEmulation cardEmulation = CardEmulation.getInstance(NfcAdapter.getDefaultAdapter(applicationContext));
                final ComponentName paymentServiceComponent = new ComponentName(activity, this.NFC_SERVICE);

                if (!cardEmulation.isDefaultServiceForCategory(paymentServiceComponent, CardEmulation.CATEGORY_PAYMENT)) {
                    this.requestNFCPermissions();
                }

                if (cardEmulation.categoryAllowsForegroundPreference(CardEmulation.CATEGORY_PAYMENT)) {
                    Log.e("setPreferredService", String.valueOf(cardEmulation.setPreferredService(activity, paymentServiceComponent)));
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Activity activity = getActivity();
            if (activity != null) {
                final CardEmulation cardEmulation = CardEmulation.getInstance(NfcAdapter.getDefaultAdapter(activity.getApplicationContext()));
                cardEmulation.unsetPreferredService(activity);
            }
        }
    }

    @Override
    public void onStop() {
        this.presenter.stop();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Detaches the screen from the presenter.
        presenter.detachScreen();
        // Unbinds all the annotated views and methods.
        unbinder.unbind();
    }

    @Nullable
    @Override
    public PurchaseComponent getComponent() {
        return component;
    }

    @Override
    public void clearPaymentOptions() {
        adapter.clear();
    }

    @Override
    public void addPaymentOption(@NonNull Product product) {
        adapter.add(product);
    }

    @Override
    public void markAsSelected(@NonNull Product product) {
        int index = adapter.indexOf(readyMessage);
        if (index >= 0) {
            adapter.remove(index);
        }
        index = adapter.indexOf(product);
        adapter.add(index + 1, readyMessage);
    }

    @Override
    public void openPaymentScreen(@NonNull Product paymentOption) {
        final FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        PurchasePaymentDialogFragment.newInstance(paymentOption)
                .show(transaction, TAG_PAYMENT_SCREEN);
    }

    @Override
    public void requestPin() {
        final Display display = getActivity().getWindowManager()
                .getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        final int originX = size.x / 2;
        final int originY = size.y / 2;
        PinConfirmationDialogFragment.show(
                getChildFragmentManager(),
                getString(R.string.activate_payment_methods),
                (PinConfirmationDialogFragment.Callback) pin -> presenter.activateCards(pin),
                originX,
                originY
        );
    }

    @Override
    public void onActivationFinished(boolean succeeded) {
        PinConfirmationDialogFragment.dismiss(getChildFragmentManager(), succeeded);
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

    @Override
    public void requestNFCPermissions() {
        if (!this.hasCards) {
            return;
        }
        if (hasRequestedToMakeAppAsDefaultForNFC) {
            return;
        }
        this.hasRequestedToMakeAppAsDefaultForNFC = true;
        Intent intent = new Intent();
        intent.setAction(CardEmulation.ACTION_CHANGE_DEFAULT);
        intent.putExtra(CardEmulation.EXTRA_SERVICE_COMPONENT,
                new ComponentName(getActivity(), this.NFC_SERVICE));
        intent.putExtra(CardEmulation.EXTRA_CATEGORY, CardEmulation.CATEGORY_PAYMENT);
        getContext().startActivity(intent);
    }

    @Override
    public void setHasCards(boolean hasCards) {
        this.hasCards = hasCards;
    }

    @Override
    public void onClick(int position) {
        final Object item = adapter.get(position);
        if (item instanceof Product) {
            presenter.onPaymentOptionSelected((Product) item);
        }
    }

    @Override
    public int getSelectedItemPosition() {
        return adapter.indexOf(presenter.getSelectedPaymentOption());
    }

    @Override
    public void onDismissed() {
        presenter.resume();
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
