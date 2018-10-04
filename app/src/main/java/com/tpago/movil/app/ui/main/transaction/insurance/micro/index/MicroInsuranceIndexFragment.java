package com.tpago.movil.app.ui.main.transaction.insurance.micro.index;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.item.ItemAdapter;
import com.tpago.movil.app.ui.item.ItemHelper;
import com.tpago.movil.app.ui.item.ItemTypeFactory;
import com.tpago.movil.app.ui.main.BaseMainFragment;
import com.tpago.movil.app.ui.main.transaction.insurance.micro.purchase.MicroInsurancePurchase;
import com.tpago.movil.app.ui.main.transaction.insurance.micro.purchase.MicroInsurancePurchaseActivity;
import com.tpago.movil.app.ui.main.transaction.item.IndexItem;
import com.tpago.movil.app.ui.main.transaction.item.IndexItemHolder;
import com.tpago.movil.app.ui.main.transaction.item.IndexItemHolderBinder;
import com.tpago.movil.app.ui.main.transaction.summary.TransactionSummaryDialogFragment;
import com.tpago.movil.app.ui.main.transaction.summary.TransactionSummaryUtil;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.company.partner.PartnerStore;
import com.tpago.movil.d.ui.main.DepMainActivityBase;
import com.tpago.movil.session.SessionManager;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public final class MicroInsuranceIndexFragment extends BaseMainFragment
  implements MicroInsuranceIndexPresentation {

  private static final int REQUEST_CODE = 42;

  public static MicroInsuranceIndexFragment create() {
    return new MicroInsuranceIndexFragment();
  }

  @BindView(R.id.recyclerView) RecyclerView recyclerView;

  @Inject MicroInsuranceIndexPresenter presenter;

  @Inject PartnerStore partnerStore;

  @Inject CompanyHelper companyHelper;
  
  @Inject SessionManager sessionManager;

  private ItemAdapter adapter;

  @Override
  protected int titleResId() {
    return R.string.main_transaction_insurance_micro;
  }

  @Override
  protected String subTitle() {
    final String subtitle = this.sessionManager.getUser()
            .phoneNumber()
            .formattedValued();
    return subtitle;
  }

  @Override
  protected int layoutResId() {
    return R.layout.main_transaction_index;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE) {
      if (resultCode == Activity.RESULT_OK) {
        TransactionSummaryDialogFragment.create(TransactionSummaryUtil.unwrap(data))
          .show(this.getFragmentManager(), null);
      }
    }
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Initializes the dependency injector.
    final MicroInsuranceIndexComponent component = DepMainActivityBase.get(this.getActivity())
      .componentBuilderSupplier()
      .get(MicroInsuranceIndexFragment.class, MicroInsuranceIndexComponent.Builder.class)
      .indexModule(MicroInsuranceIndexModule.create(this))
      .build();

    // Injects all annotated dependencies.
    component.inject(this);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    final Context context = this.getContext();

    final ItemTypeFactory typeFactory = ItemTypeFactory.builder()
      .type(IndexItem.class, IndexItem.type())
      .build();
    this.adapter = ItemAdapter.builder(typeFactory)
      .itemType(
        IndexItem.class,
        IndexItemHolder::create,
        IndexItemHolderBinder.create()
      )
      .build();

    this.recyclerView.setAdapter(this.adapter);
    this.recyclerView.setHasFixedSize(true);
    this.recyclerView.setLayoutManager(ItemHelper.layoutManagerLinearVertical(context));
    this.recyclerView.addItemDecoration(ItemHelper.dividerLineHorizontal(context));
  }

  @Override
  public void onResume() {
    super.onResume();
    this.presenter.onPresentationResumedWithPartners(partnerStore, companyHelper); // TODO: CHANGE FOR NORMAL ONPRESENTATIONRESUMED
  }

  @Override
  public void onPause() {
    this.presenter.onPresentationPaused();
    super.onPause();
  }

  @Override
  public void setItems(List<IndexItem> items) {
    this.adapter.clear();
    for (IndexItem item : items) {
      this.adapter.add(item);
    }
  }

  @Override
  public void startTransaction(MicroInsurancePurchase purchase) {
    final Intent intent = MicroInsurancePurchaseActivity
      .createLaunchIntent(this.getContext(), purchase);
    this.startActivityForResult(intent, REQUEST_CODE);
  }
}
