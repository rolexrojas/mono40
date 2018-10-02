package com.tpago.movil.d.ui.main.recipient.addition.partners;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.tpago.movil.company.partner.Partner;
import com.tpago.movil.d.data.util.BinderFactory;
import com.tpago.movil.d.ui.main.list.ListItemHolderCreatorFactory;
import com.tpago.movil.d.ui.main.recipient.addition.RecipientCandidateListFragment;

/**
 * @author hecvasro
 */
public class PartnerListFragment
  extends RecipientCandidateListFragment<PartnerListPresenter>
  implements PartnerListScreen {

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all the annotated dependencies.
    final PartnerListComponent component = DaggerPartnerListComponent.builder()
      .addRecipientComponent(getContainer().getComponent())
      .build();
    component.inject(this);
  }

  @NonNull
  @Override
  protected ListItemHolderCreatorFactory.Builder createHolderCreatorFactoryBuilder() {
    return new ListItemHolderCreatorFactory.Builder()
      .addCreator(Partner.class, new PartnerListItemHolderCreator(this));
  }

  @NonNull
  @Override
  protected BinderFactory.Builder createHolderBinderFactoryBuilder() {
    return new BinderFactory.Builder()
      .addBinder(
        Partner.class,
        PartnerListItemHolder.class,
        PartnerListItemHolderBinder.create(this.companyHelper)
      );
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    // Let the super class create its view.
    super.onViewCreated(view, savedInstanceState);
  }

  @Override
  public void onDestroyView() {
    // Let the super class destroy its view.
    super.onDestroyView();
  }
}
