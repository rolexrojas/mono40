package com.tpago.movil.d.ui.main.recipients.contacts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.tpago.movil.d.data.util.BinderFactory;
import com.tpago.movil.d.ui.main.list.ListItemHolderCreatorFactory;
import com.tpago.movil.d.ui.main.recipients.Contact;
import com.tpago.movil.d.ui.main.recipients.RecipientCandidateListFragment;

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
      .addRecipientComponent(getContainer().getComponent())
      .build();
    component.inject(this);
  }

  @NonNull
  @Override
  protected ListItemHolderCreatorFactory.Builder createHolderCreatorFactoryBuilder() {
    return new ListItemHolderCreatorFactory.Builder()
      .addCreator(Contact.class, new ContactListItemHolderCreator(this));
  }

  @NonNull
  @Override
  protected BinderFactory.Builder createHolderBinderFactoryBuilder() {
    return new BinderFactory.Builder()
      .addBinder(Contact.class, ContactListItemHolder.class, new ContactListItemHolderBinder());
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
