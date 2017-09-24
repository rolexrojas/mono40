package com.tpago.movil.d.ui.main.recipient.addition.banks;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.tpago.movil.d.data.util.BinderFactory;
import com.tpago.movil.d.ui.main.list.ListItemHolderCreatorFactory;
import com.tpago.movil.d.ui.main.recipient.addition.RecipientCandidateListFragment;
import com.tpago.movil.domain.Bank;

/**
 * @author hecvasro
 */
public class BankListFragment
  extends RecipientCandidateListFragment<BankListPresenter>
  implements BankListScreen {

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all the annotated dependencies.
    final BankListComponent component = DaggerBankListComponent.builder()
      .addRecipientComponent(getContainer().getComponent())
      .build();
    component.inject(this);
  }

  @NonNull
  @Override
  protected ListItemHolderCreatorFactory.Builder createHolderCreatorFactoryBuilder() {
    return new ListItemHolderCreatorFactory.Builder()
      .addCreator(Bank.class, new BankListItemHolderCreator(this));
  }

  @NonNull
  @Override
  protected BinderFactory.Builder createHolderBinderFactoryBuilder() {
    return new BinderFactory.Builder()
      .addBinder(Bank.class, BankListItemHolder.class, new BankListItemHolderBinder());
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
