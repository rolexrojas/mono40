package com.mono40.movil.d.ui.main.recipient.addition.partners;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

import com.mono40.movil.company.partner.Partner;
import com.mono40.movil.d.data.util.BinderFactory;
import com.mono40.movil.d.ui.main.list.ListItemHolderCreatorFactory;
import com.mono40.movil.d.ui.main.recipient.addition.RecipientCandidateListFragment;

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
