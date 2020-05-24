package com.mono40.movil.d.ui.main.purchase;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mono40.movil.R;
import com.mono40.movil.app.ui.activity.base.ActivityModule;
import com.mono40.movil.app.ui.loader.takeover.TakeoverLoader;
import com.mono40.movil.company.CompanyHelper;
import com.mono40.movil.d.domain.Product;
import com.mono40.movil.d.domain.ProductManager;
import com.mono40.movil.d.domain.api.ApiCode;
import com.mono40.movil.d.domain.api.ApiResult;
import com.mono40.movil.d.domain.api.DepApiBridge;
import com.mono40.movil.d.ui.ChildFragment;
import com.mono40.movil.d.ui.DepActivityBase;
import com.mono40.movil.d.ui.Dialogs;
import com.mono40.movil.d.ui.main.MainContainer;
import com.mono40.movil.d.ui.main.PinConfirmationDialogFragment;
import com.mono40.movil.d.ui.main.recipient.index.disburse.DisbursementActivity;
import com.mono40.movil.d.ui.view.RecyclerViewBaseAdapter;
import com.mono40.movil.reactivex.DisposableUtil;
import com.mono40.movil.session.SessionManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * @author hecvasro
 */
public final class NonNfcPurchaseFragment extends ChildFragment<MainContainer> implements PurchaseContainer {

    public static NonNfcPurchaseFragment create() {
        return new NonNfcPurchaseFragment();
    }

    private static final String TAG_PAYMENT_SCREEN = "paymentScreen";

    @Inject
    ProductManager productManager;
    @Inject
    PurchasePresenter presenter;
    @Inject
    CompanyHelper companyHelper;

    @Inject
    SessionManager sessionManager;
    @Inject
    DepApiBridge apiBridge;
    @Inject
    TakeoverLoader takeoverLoader;

    private Unbinder unbinder;
    @BindView(R.id.card_list)
    RecyclerView cardListView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final NonNfcPurchaseComponent component = DaggerNonNfcPurchaseComponent.builder()
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
        return inflater.inflate(R.layout.d_fragment_purchase_fragment_non_nfc, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Binds all the annotated resources, views and methods.
        unbinder = ButterKnife.bind(this, view);
        cardListView.setLayoutManager(new LinearLayoutManager(getContext()));
        cardListView.setAdapter(new CardListAdapter());
    }

    public void handleSuccess(Product paymentMethod) {
        PinConfirmationDialogFragment.dismiss(getFragmentManager(), true);
        final FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        NonNfcPurchasePaymentDialogFragment.newInstance(paymentMethod, new NonNfcPurchasePaymentDialogFragment.OnDismissedListener() {
            @Override
            public void onDismissed() {
                loadProducts();
            }
        })
                .show(transaction, TAG_PAYMENT_SCREEN);
    }


    public void handleError() {
        PinConfirmationDialogFragment.dismiss(getFragmentManager(), false);
        Dialogs.builder(getContext())
                .setTitle(R.string.error_generic_title)
                .setMessage(R.string.error_incorrect_pin)
                .setPositiveButton(R.string.error_positive_button_text, null)
                .show();
    }

    public void handleResult(ApiResult<Void> apiResultObservable, Product paymentMethod) {
        if (apiResultObservable.getCode() == ApiCode.OK) {
            handleSuccess(paymentMethod);
        } else {
            handleError();
        }
    }

    public void activatePurchase(String pin, Product paymentMethod) {
        apiBridge.activatePurchaseWithoutNfc(paymentMethod, pin)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResultObservable -> handleResult(apiResultObservable, paymentMethod), throwable -> handleError());
    }

    @Override
    public void onStart() {
        super.onStart();
        // Sets the title.
        getContainer().setTitle(getString(R.string.screen_payments_commerce_title));
        loadProducts();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void loadProducts() {
        cardListView.setAdapter(null);
        CardListAdapter adapter = new CardListAdapter(productManager.getPaymentOptionList(),
                item -> {
                    PinConfirmationDialogFragment.show(
                            getActivity().getSupportFragmentManager(),
                            getContext().getString(R.string.confirm_pin_payment_authorize_description),
                            (PinConfirmationDialogFragment.Callback) pin -> activatePurchase(pin, item),
                            0,
                            0
                    );
                }, sessionManager.getUser(), companyHelper);
        cardListView.setAdapter(adapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unbinds all the annotated resources, views and methods.
        unbinder.unbind();
    }

    @Nullable
    @Override
    public PurchaseComponent getComponent() {
        return DaggerPurchaseComponent.builder()
                .depMainComponent(getContainer().getComponent())
                .purchaseModule(new PurchaseModule((DepActivityBase) getActivity()))
                .build();
    }
}
