package com.tpago.movil.d.ui.main.recipient.index.category.selectcarrier;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpago.movil.R;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.company.partner.Partner;
import com.tpago.movil.company.partner.PartnerStore;
import com.tpago.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.d.domain.PhoneNumberRecipient;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.RecipientManager;
import com.tpago.movil.d.ui.ChildFragment;
import com.tpago.movil.d.ui.main.MainContainer;
import com.tpago.movil.d.ui.main.recipient.index.category.OnSaveButtonClickedListener;
import com.tpago.movil.d.ui.main.recipient.index.category.RecipientAdditionDialogFragment;
import com.tpago.movil.d.ui.view.RecyclerViewBaseAdapter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CarrierSelectFragment extends ChildFragment<MainContainer> implements OnSaveButtonClickedListener {
  @Inject
  public PartnerStore partnerStore;
  @Inject
  public CompanyHelper companyHelper;
  @Inject
  public RecipientManager recipientManager;
  @BindView(R.id.recycler_view)
  RecyclerView carriersListView;
  Unbinder unbinder;
  Recipient recipient;
  private static final String FRAGMENT_ARG_RECIPIENT = "FRAGMENT_ARG_RECIPIENT";

  public static CarrierSelectFragment create(Recipient recipient) {
    Bundle args = new Bundle();
    args.putParcelable(FRAGMENT_ARG_RECIPIENT, recipient);
    CarrierSelectFragment fragment = new CarrierSelectFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    DaggerCarrierSelectComponent.builder()
        .depMainComponent(getContainer().getComponent())
        .carrierSelectModule(new CarrierSelectModule())
        .build().inject(this);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.carrier_selection_fragment, container,
        false);
    unbinder = ButterKnife.bind(this, view);
    if (getArguments() != null) {
      recipient = getArguments().getParcelable(FRAGMENT_ARG_RECIPIENT);
    }
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    carriersListView.setAdapter(new CarriersAdapter(new ArrayList<>(), companyHelper));
    carriersListView.setLayoutManager(new LinearLayoutManager(getContext()));
    if (getContext() != null) {
      carriersListView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
          .drawable(R.drawable.divider_line_horizontal)
          .marginResId(R.dimen.space_horizontal_20)
          .showLastDivider()
          .build());
    }
    partnerStore.getCarriers()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSuccess(partners -> carriersListView.setAdapter(new CarriersAdapter(partners,
            (RecyclerViewBaseAdapter.RecyclerAdapterCallback<Partner>) item -> {
              if (recipient instanceof PhoneNumberRecipient) {
                ((PhoneNumberRecipient) recipient).setCarrier(item);
              } else if (recipient instanceof NonAffiliatedPhoneNumberRecipient) {
                ((NonAffiliatedPhoneNumberRecipient) recipient).setCarrier(item);
              }
              RecipientAdditionDialogFragment.create(recipient)
                  .show(getChildFragmentManager(), null);
            },
            companyHelper)))
        .subscribe();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override
  public void onSaveButtonClicked(Recipient recipient, String label) {
    recipient.setLabel(label);
    recipientManager.update(recipient);
    FragmentManager fragmentManager = getFragmentManager();
    if (fragmentManager != null) {
      fragmentManager.popBackStack();
    }
  }

  public interface CarrierSelectFragmentCallback {
    void onCarrierSelect(Recipient recipient);
  }
}
