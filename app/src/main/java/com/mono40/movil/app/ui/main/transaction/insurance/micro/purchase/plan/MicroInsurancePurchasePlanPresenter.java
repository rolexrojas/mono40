package com.mono40.movil.app.ui.main.transaction.insurance.micro.purchase.plan;

import android.net.Uri;

import com.mono40.movil.R;
import com.mono40.movil.app.StringMapper;
import com.mono40.movil.app.ui.Presenter;
import com.mono40.movil.app.ui.main.transaction.insurance.micro.purchase.MicroInsurancePurchase;
import com.mono40.movil.app.ui.main.transaction.insurance.micro.purchase.MicroInsurancePurchaseComponent;
import com.mono40.movil.app.ui.main.transaction.item.IndexItem;
import com.mono40.movil.company.CompanyHelper;
import com.mono40.movil.company.partner.PartnerStore;
import com.mono40.movil.insurance.micro.MicroInsurancePartner;
import com.mono40.movil.insurance.micro.MicroInsurancePlan;
import com.mono40.movil.session.SessionManager;
import com.mono40.movil.util.UriBuilder;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public final class MicroInsurancePurchasePlanPresenter
  extends Presenter<MicroInsurancePurchasePlanPresentation> {

  static MicroInsurancePurchasePlanPresenter create(
    MicroInsurancePurchasePlanPresentation presentation,
    MicroInsurancePurchaseComponent component
  ) {
    return new MicroInsurancePurchasePlanPresenter(presentation, component);
  }

  @Inject SessionManager sessionManager;

  @Inject MicroInsurancePurchase purchase;

  @Inject StringMapper stringMapper;

  @Inject PartnerStore partnerStore;

  @Inject CompanyHelper companyHelper;


  private MicroInsurancePurchasePlanPresenter(
    MicroInsurancePurchasePlanPresentation presentation,
    MicroInsurancePurchaseComponent component
  ) {
    super(presentation);

    // Injects all annotated dependencies.
    component.inject(this);
  }

  private IndexItem mapPlanToIndexItem(MicroInsurancePlan plan) {
    final MicroInsurancePartner partner = this.purchase.partner();
    final MicroInsurancePlan.Data planData = plan.data();
    return IndexItem.builder()
      .pictureUri(UriBuilder.createFromPartners(this.partnerStore, this.companyHelper, partner.id())) // TODO: Set partner's logo.
      .titleText(partner.name())
      .subtitleText(planData.name())
      .actionText(this.stringMapper.apply(R.string.buy))
      .actionRunner(() -> {
        this.purchase.plan(plan);
        this.presentation.moveToNextScreen();
      })
      .build();
  }

  @Override
  public void onPresentationResumed() {
    this.presentation.setTitle(this.stringMapper.apply(R.string.main_transaction_insurance_micro));
    final String subtitle = this.sessionManager.getUser()
      .phoneNumber()
      .formattedValued();
    this.presentation.setSubtitle(subtitle);

    final List<IndexItem> items = Observable.fromIterable(this.purchase.plans())
      .map(this::mapPlanToIndexItem)
      .toList()
      .blockingGet();
    this.presentation.setItems(items);
  }
}
