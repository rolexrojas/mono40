package com.mono40.movil.d.ui.main.transaction.contacts;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.mono40.movil.PhoneNumber;
import com.mono40.movil.R;
import com.mono40.movil.app.StringMapper;
import com.mono40.movil.company.Company;
import com.mono40.movil.company.CompanyHelper;
import com.mono40.movil.company.partner.Partner;
import com.mono40.movil.company.partner.PartnerStore;
import com.mono40.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.mono40.movil.d.domain.PhoneNumberRecipient;
import com.mono40.movil.d.domain.Product;
import com.mono40.movil.d.domain.Recipient;
import com.mono40.movil.d.domain.UserRecipient;
import com.mono40.movil.d.domain.api.DepApiBridge;
import com.mono40.movil.d.ui.ChildFragment;
import com.mono40.movil.d.ui.Dialogs;
import com.mono40.movil.d.ui.main.PinConfirmationDialogFragment;
import com.mono40.movil.d.ui.main.PinConfirmationDialogFragment.Callback;
import com.mono40.movil.d.ui.main.transaction.TransactionCreationActivityBase;
import com.mono40.movil.d.ui.main.transaction.TransactionCreationComponent;
import com.mono40.movil.d.ui.main.transaction.TransactionCreationContainer;
import com.mono40.movil.session.SessionManager;
import com.mono40.movil.util.ObjectHelper;
import com.mono40.movil.util.StringHelper;
import com.mono40.movil.util.TaxUtil;
import com.mono40.movil.util.TransactionType;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * @author Hector Vasquez
 */
public final class CarrierSelectionFragment extends ChildFragment<TransactionCreationContainer> {

    static CarrierSelectionFragment create() {
        return new CarrierSelectionFragment();
    }

    private Unbinder unbinder;

    private Adapter adapter = new Adapter();
    private List<Partner> carriers;
    private TransactionCreationActivityBase activity;

    private Subscription rechargeSubscription = Subscriptions.unsubscribed();

    @Inject
    AtomicReference<BigDecimal> value;
    @Inject
    AtomicReference<Product> fundingAccount;
    @Inject
    DepApiBridge apiBridge;
    @Inject
    PartnerStore partnerStore;
    @Inject
    Recipient recipient;
    @Inject
    SessionManager sessionManager;
    @Inject
    CompanyHelper companyHelper;
    @Inject
    StringMapper stringMapper;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private void recharge(String pin) {
        final Partner carrier;
        final PhoneNumber phoneNumber;
        if (this.recipient instanceof UserRecipient) {
            final UserRecipient r = (UserRecipient) this.recipient;

            carrier = r.getCarrier();
            phoneNumber = r.phoneNumber();
        } else if (this.recipient instanceof NonAffiliatedPhoneNumberRecipient) {
            final NonAffiliatedPhoneNumberRecipient r
                    = (NonAffiliatedPhoneNumberRecipient) this.recipient;

            carrier = r.getCarrier();
            phoneNumber = r.getPhoneNumber();
        } else {
            final PhoneNumberRecipient r = (PhoneNumberRecipient) this.recipient;

            carrier = r.getCarrier();
            phoneNumber = r.getPhoneNumber();
        }

        rechargeSubscription = this.apiBridge.recharge(
                carrier,
                phoneNumber,
                this.fundingAccount.get(),
                this.value.get(),
                pin
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    PinConfirmationDialogFragment.dismiss(getChildFragmentManager(), result.isSuccessful());

                    if (result.isSuccessful()) {
                        getContainer()
                                .finish(true, result.getData());
                    } else {
                        Dialogs.builder(getContext())
                                .setTitle(R.string.error_generic_title)
                                .setMessage(
                                        result.getError()
                                                .getDescription()
                                )
                                .setPositiveButton(
                                        R.string.error_positive_button_text,
                                        null
                                )
                                .create()
                                .show();
                    }
                }, throwable -> {
                    Timber.e(throwable);

                    PinConfirmationDialogFragment.dismiss(getChildFragmentManager(), false);

                    Dialogs.builder(getContext())
                            .setTitle(R.string.error_generic_title)
                            .setMessage(R.string.error_generic)
                            .setPositiveButton(R.string.error_positive_button_text, null)
                            .create()
                            .show();
                });
    }

    private String selectLabelToShow(String recipientName, String recipientLabel, String phoneNumber) {
        if (!StringHelper.isNullOrEmpty(recipientName)) {
            return recipientName;
        }

        if (!StringHelper.isNullOrEmpty(recipientLabel)) {
            return recipientLabel;
        }

        return phoneNumber;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final TransactionCreationComponent c = getContainer().getComponent();
        if (ObjectHelper.isNotNull(c)) {
            c.inject(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(
                R.layout.carrier_selection_fragment,
                container,
                false
        );
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        swipeRefreshLayout.setEnabled(false);
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
        final Context context = getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
        ));
        final RecyclerView.ItemDecoration divider = new HorizontalDividerItemDecoration.Builder(context)
                .drawable(R.drawable.divider_line_horizontal)
                .marginResId(R.dimen.space_horizontal_20)
                .showLastDivider()
                .build();
        recyclerView.addItemDecoration(divider);

        activity = (TransactionCreationActivityBase) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ObjectHelper.isNull(this.carriers)) {
            this.carriers = this.partnerStore.getCarriers()
                    .defaultIfEmpty(new ArrayList<>())
                    .blockingGet();
            this.adapter.notifyItemRangeInserted(0, this.carriers.size());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!rechargeSubscription.isUnsubscribed()) {
            rechargeSubscription.unsubscribe();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.image_view_background);
            textView = itemView.findViewById(R.id.text_view);
        }

        @Override
        public void onClick(View v) {
            final String phoneNumber;
            final Partner p = carriers.get(this.getAdapterPosition());

            if (recipient instanceof UserRecipient) {
                final UserRecipient r = (UserRecipient) recipient;
                r.setCarrier(p);

                sessionManager.updateCarrier(p);

                phoneNumber = r.phoneNumber()
                        .formattedValued();
            } else if (recipient instanceof NonAffiliatedPhoneNumberRecipient) {
                final NonAffiliatedPhoneNumberRecipient r = (NonAffiliatedPhoneNumberRecipient) recipient;
                r.setCarrier(p);

//        recipientManager.update(recipient);

                phoneNumber = r.getPhoneNumber()
                        .formattedValued();
            } else {
                final PhoneNumberRecipient r = (PhoneNumberRecipient) recipient;
                r.setCarrier(p);

//        recipientManager.update(recipient);

                phoneNumber = r.getPhoneNumber()
                        .formattedValued();
            }

            final String description = TaxUtil.getConfirmPinTransactionMessage(
                    TransactionType.RECHARGE, value.get().doubleValue(),
                    fundingAccount.get(), selectLabelToShow(activity.getRecipientName(), recipient.getLabel(), recipient.getIdentifier()),
                    fundingAccount.get().getCurrency(), "",
                    stringMapper, 0, 0, 0, 0);
            final int x = Math.round((v.getRight() - v.getLeft()) / 2);
            final int y = Math.round((v.getBottom() - v.getTop()) / 2);

            PinConfirmationDialogFragment.show(
                    getChildFragmentManager(),
                    description,
                    (Callback) pin -> recharge(pin),
                    x,
                    y
            );
        }
    }

    final class Adapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.d_list_item_bank, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Partner carrier = carriers.get(position);

            Picasso.get()
                    .load(companyHelper.getLogoUri(carrier, Company.LogoStyle.COLORED_24))
                    .into(holder.imageView);
            holder.textView.setText(carrier.name());
        }

        @Override
        public int getItemCount() {
            return carriers.size();
        }
    }
}
