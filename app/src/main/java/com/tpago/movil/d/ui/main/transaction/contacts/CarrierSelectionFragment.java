package com.tpago.movil.d.ui.main.transaction.contacts;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import com.squareup.picasso.Picasso;
import com.tpago.movil.company.LogoCatalog;
import com.tpago.movil.company.LogoCatalogMapper;
import com.tpago.movil.dep.Partner;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.R;
import com.tpago.movil.dep.api.ApiImageUriBuilder;
import com.tpago.movil.dep.api.DCurrencies;
import com.tpago.movil.d.data.Formatter;
import com.tpago.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.d.domain.PhoneNumberRecipient;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.RecipientManager;
import com.tpago.movil.d.domain.UserRecipient;
import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.ui.ChildFragment;
import com.tpago.movil.d.ui.Dialogs;
import com.tpago.movil.d.ui.main.PinConfirmationDialogFragment;
import com.tpago.movil.d.ui.main.PinConfirmationDialogFragment.Callback;
import com.tpago.movil.d.ui.main.transaction.TransactionCreationComponent;
import com.tpago.movil.d.ui.main.transaction.TransactionCreationContainer;
import com.tpago.movil.d.ui.view.widget.LoadIndicator;
import com.tpago.movil.d.ui.view.widget.SwipeRefreshLayoutRefreshIndicator;
import com.tpago.movil.payment.Carrier;
import com.tpago.movil.payment.PartnerBuilderFactory;
import com.tpago.movil.session.SessionManager;
import com.tpago.movil.util.ObjectHelper;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
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
  private List<Partner> carrierList = new ArrayList<>();

  private LoadIndicator loadIndicator;
  private Subscription subscription = Subscriptions.unsubscribed();
  private Subscription rechargeSubscription = Subscriptions.unsubscribed();

  @Inject SessionManager sessionManager;
  @Inject LogoCatalogMapper logoCatalogMapper;

  @Inject
  RecipientManager recipientManager;
  @Inject
  DepApiBridge apiBridge;
  @Inject
  Recipient recipient;
  @Inject
  AtomicReference<Product> fundingAccount;
  @Inject
  AtomicReference<BigDecimal> value;

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
      .subscribe(new Action1<ApiResult<String>>() {
        @Override
        public void call(ApiResult<String> result) {
          PinConfirmationDialogFragment.dismiss(getChildFragmentManager(), result.isSuccessful());

          if (result.isSuccessful()) {
            getContainer()
              .finish(true, result.getData());
          } else {
            Dialogs.builder(getContext())
              .setTitle(R.string.error_generic_title)
              .setMessage(result.getError()
                .getDescription())
              .setPositiveButton(R.string.error_positive_button_text, null)
              .create()
              .show();
          }
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable);

          PinConfirmationDialogFragment.dismiss(getChildFragmentManager(), false);

          Dialogs.builder(getContext())
            .setTitle(R.string.error_generic_title)
            .setMessage(R.string.error_generic)
            .setPositiveButton(R.string.error_positive_button_text, null)
            .create()
            .show();
        }
      });
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
    loadIndicator = new SwipeRefreshLayoutRefreshIndicator(swipeRefreshLayout);
    adapter = new Adapter();
    recyclerView.setAdapter(adapter);
    final Context context = getContext();
    recyclerView.setLayoutManager(new LinearLayoutManager(
      context,
      LinearLayoutManager.VERTICAL,
      false
    ));
    final RecyclerView.ItemDecoration divider = new HorizontalDividerItemDecoration.Builder(context)
      .drawable(R.drawable.d_divider)
      .marginResId(R.dimen.space_horizontal_normal)
      .showLastDivider()
      .build();
    recyclerView.addItemDecoration(divider);
  }

  @Override
  public void onResume() {
    super.onResume();
    subscription = apiBridge.partners()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnSubscribe(new Action0() {
        @Override
        public void call() {
          loadIndicator.show();
        }
      })
      .subscribe(new Action1<ApiResult<List<Partner>>>() {
        @Override
        public void call(ApiResult<List<Partner>> result) {
          if (result.isSuccessful()) {
            if (ObjectHelper.isNull(carrierList)) {
              carrierList = new ArrayList<>();
            } else {
              adapter.notifyItemRangeRemoved(0, carrierList.size());
              carrierList.clear();
            }

            for (Partner partner : result.getData()) {
              if (partner.getType()
                .equals(Partner.TYPE_CARRIER)) {
                carrierList.add(partner);
              }
            }
            adapter.notifyItemRangeInserted(0, carrierList.size());
          } else {
            Dialogs.builder(getContext())
              .setTitle(R.string.error_generic_title)
              .setMessage(result.getError()
                .getDescription())
              .setPositiveButton(R.string.ok, null)
              .show();
          }
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable);
          Dialogs.builder(getContext())
            .setTitle(R.string.error_generic_title)
            .setMessage(R.string.error_generic)
            .setPositiveButton(R.string.error_positive_button_text, null)
            .show();
        }
      }, new Action0() {
        @Override
        public void call() {
          loadIndicator.hide();
        }
      });
  }

  @Override
  public void onPause() {
    super.onPause();
    if (!rechargeSubscription.isUnsubscribed()) {
      rechargeSubscription.unsubscribe();
    }
    if (!subscription.isUnsubscribed()) {
      subscription.unsubscribe();
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
      imageView = ButterKnife.findById(itemView, R.id.image_view_background);
      textView = ButterKnife.findById(itemView, R.id.text_view);
    }

    @Override
    public void onClick(View v) {
      final String phoneNumber;
      final Partner p = carrierList.get(this.getAdapterPosition());

      if (recipient instanceof UserRecipient) {
        final UserRecipient r = (UserRecipient) recipient;
        r.setCarrier(p);

        final String logoTemplate = p.getImageUriTemplate();
        final LogoCatalog logoCatalog = logoCatalogMapper.apply(logoTemplate);
        final Carrier carrier
          = (Carrier) PartnerBuilderFactory.make(com.tpago.movil.payment.Partner.Type.CARRIER)
          .code(p.getCode())
          .id(p.getId())
          .name(p.getName())
          .logoTemplate(logoTemplate)
          .logoCatalog(logoCatalog)
          .build();
        sessionManager.updateCarrier(carrier);

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

      final String description = String.format(
        "Recargar %1$s a %2$s",
        Formatter.amount(
          DCurrencies.map(
            fundingAccount.get()
              .getCurrency()
          ),
          value.get()
        ),
        phoneNumber
      );
      final int x = Math.round((v.getRight() - v.getLeft()) / 2);
      final int y = Math.round((v.getBottom() - v.getTop()) / 2);

      PinConfirmationDialogFragment.show(
        getChildFragmentManager(),
        description,
        new Callback() {
          @Override
          public void confirm(String pin) {
            recharge(pin);
          }
        },
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
      final Partner carrier = carrierList.get(position);

      final Uri imageUri = ApiImageUriBuilder.build(
        holder.imageView.getContext(),
        carrier,
        ApiImageUriBuilder.Style.PRIMARY_24
      );
      Picasso.with(getContext())
        .load(imageUri)
        .into(holder.imageView);
      holder.textView.setText(carrier.getName());
    }

    @Override
    public int getItemCount() {
      return carrierList.size();
    }
  }
}
