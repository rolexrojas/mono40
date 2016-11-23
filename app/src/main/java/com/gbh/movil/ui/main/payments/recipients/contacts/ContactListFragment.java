package com.gbh.movil.ui.main.payments.recipients.contacts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.gbh.movil.ui.main.list.HolderBinderFactory;
import com.gbh.movil.ui.main.list.HolderCreatorFactory;
import com.gbh.movil.ui.main.payments.recipients.RecipientCandidateListFragment;

/**
 * TODO
 *
 * @author hecvasro
 */
public class ContactListFragment extends RecipientCandidateListFragment<ContactListPresenter>
  implements ContactListScreen {
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all the annotated dependencies.
    final ContactListComponent component = DaggerContactListComponent.builder()
      .addRecipientComponent(parentScreen.getComponent())
      .build();
    component.inject(this);
  }

  @NonNull
  @Override
  protected HolderCreatorFactory.Builder createHolderCreatorFactoryBuilder() {
    return new HolderCreatorFactory.Builder()
      .addCreator(Contact.class, new ContactHolderCreator(this));
  }

  @NonNull
  @Override
  protected HolderBinderFactory.Builder createHolderBinderFactoryBuilder() {
    return new HolderBinderFactory.Builder()
      .addBinder(Contact.class, ContactHolder.class, new ContactHolderBinder());
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    // Let the super class create its view.
    super.onViewCreated(view, savedInstanceState);
    // Creates the presenter.
    presenter.create();
  }

  @Override
  public void onDestroyView() {
    // Destroys the presenter.
    presenter.destroy();
    // Let the super class destroy its view.
    super.onDestroyView();
  }
}
