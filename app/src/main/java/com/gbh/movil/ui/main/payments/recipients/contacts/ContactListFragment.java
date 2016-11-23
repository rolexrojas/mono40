package com.gbh.movil.ui.main.payments.recipients.contacts;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gbh.movil.R;
import com.gbh.movil.Utils;
import com.gbh.movil.ui.SubFragment;
import com.gbh.movil.ui.main.list.ItemAdapter;
import com.gbh.movil.ui.main.list.Holder;
import com.gbh.movil.ui.main.list.ItemHolderBinderFactory;
import com.gbh.movil.ui.main.list.ItemHolderCreatorFactory;
import com.gbh.movil.ui.main.list.NoResultsHolder;
import com.gbh.movil.ui.main.list.NoResultsHolderBinder;
import com.gbh.movil.ui.main.list.NoResultsHolderCreator;
import com.gbh.movil.ui.main.list.NoResultsItem;
import com.gbh.movil.ui.main.payments.recipients.AddRecipientComponent;
import com.gbh.movil.ui.view.widget.RefreshIndicator;
import com.gbh.movil.ui.view.widget.SwipeRefreshLayoutRefreshIndicator;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public class ContactListFragment extends SubFragment<AddRecipientComponent>
  implements ContactListScreen, Holder.OnClickListener {
  private Unbinder unbinder;
  private RefreshIndicator refreshIndicator;
  private ItemAdapter itemAdapter;

  @Inject
  ContactListPresenter presenter;

  @BindView(R.id.swipe_refresh_layout)
  SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_contact_list, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Injects all the annotated dependencies.
    final ContactListComponent component = DaggerContactListComponent.builder()
      .addRecipientComponent(parentScreen.getComponent())
      .build();
    component.inject(this);
    // Binds all the annotated views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Prepares the contact item container.
    final ItemHolderCreatorFactory holderCreatorFactory = new ItemHolderCreatorFactory.Builder()
      .addCreator(Contact.class, new ContactHolderCreator(this))
      .addCreator(NoResultsItem.class, new NoResultsHolderCreator())
      .build();
    final Context context = getContext();
    final ItemHolderBinderFactory holderBinderFactory = new ItemHolderBinderFactory.Builder()
      .addBinder(Contact.class, ContactHolder.class, new ContactHolderBinder())
      .addBinder(NoResultsItem.class, NoResultsHolder.class, new NoResultsHolderBinder(context))
      .build();
    itemAdapter = new ItemAdapter(holderCreatorFactory, holderBinderFactory);
    recyclerView.setAdapter(itemAdapter);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,
      false));
    final RecyclerView.ItemDecoration divider = new HorizontalDividerItemDecoration.Builder(context)
      .drawable(R.drawable.divider)
      .marginResId(R.dimen.list_item_inset_horizontal)
      .showLastDivider()
      .build();
    recyclerView.addItemDecoration(divider);
    // Attaches the presenter to the fragment.
    presenter.attachScreen(this);
    // Creates the presenter.
    presenter.create();
  }

  @Override
  public void onStop() {
    super.onStop();
    // Stops the presenter.
    presenter.stop();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Destroys the presenter.
    presenter.destroy();
    // Detaches the presenter for the fragment.
    presenter.detachScreen();
    // Unbinds all the annotated views and methods.
    unbinder.unbind();
  }

  @NonNull
  @Override
  public Observable<String> onQueryChanged() {
    return Observable.empty();
  }

  @Override
  public void clear() {
    itemAdapter.clearItems();
  }

  @Override
  public void add(@NonNull Object item) {
    itemAdapter.addItem(item);
  }

  @Nullable
  @Override
  public RefreshIndicator getRefreshIndicator() {
     if (Utils.isNull(refreshIndicator) && Utils.isNotNull(swipeRefreshLayout)) {
      refreshIndicator = new SwipeRefreshLayoutRefreshIndicator(swipeRefreshLayout);
    }
    return refreshIndicator;
  }

  @Override
  public void onClick(int position) {
    // TODO
  }
}
