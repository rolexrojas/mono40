package com.gbh.movil.ui.main.payments;

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
import com.gbh.movil.data.MessageHelper;
import com.gbh.movil.ui.main.list.Item;
import com.gbh.movil.ui.main.list.ItemAdapter;
import com.gbh.movil.ui.main.list.ItemHolderBinderFactory;
import com.gbh.movil.ui.main.list.ItemHolderCreatorFactory;
import com.gbh.movil.ui.main.list.NoResultsItem;
import com.gbh.movil.ui.main.list.NoResultsItemHolder;
import com.gbh.movil.ui.main.list.NoResultsItemHolderBinder;
import com.gbh.movil.ui.main.list.NoResultsItemHolderCreator;
import com.gbh.movil.ui.view.widget.RefreshIndicator;
import com.gbh.movil.ui.view.widget.SwipeRefreshLayoutRefreshIndicator;
import com.gbh.movil.ui.main.SubFragment;
import com.gbh.movil.ui.view.widget.SearchView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;

/**
 * {@link PaymentsScreen Screen} implementation that uses a {@link SubFragment fragment} as
 * container.
 *
 * @author hecvasro
 */
public class PaymentsFragment extends SubFragment implements PaymentsScreen {
  private Unbinder unbinder;
  private RefreshIndicator refreshIndicator;
  private ItemAdapter adapter;

  @BindView(R.id.search_view)
  SearchView searchView;
  @BindView(R.id.swipe_refresh_layout)
  SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;

  @Inject
  MessageHelper messageHelper;
  @Inject
  PaymentsPresenter presenter;

  /**
   * Creates a new instance of the {@link PaymentsFragment screen}.
   *
   * @return A new instance of the {@link PaymentsFragment screen}.
   */
  @NonNull
  public static PaymentsFragment newInstance() {
    return new PaymentsFragment();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_payments, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Injects all the annotated dependencies.
    final PaymentsComponent component = DaggerPaymentsComponent.builder()
      .mainComponent(parentScreen.getComponent())
      .build();
    component.inject(this);
    // Binds all the annotated views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Prepares the actions and recipients list.
    final ItemHolderCreatorFactory holderCreatorFactory = new ItemHolderCreatorFactory.Builder()
      .addCreator(ContactRecipientItem.class, new ContactRecipientItemHolderCreator())
      .addCreator(NoResultsItem.class, new NoResultsItemHolderCreator())
      .build();
    final ItemHolderBinderFactory binderFactory = new ItemHolderBinderFactory.Builder()
      .addBinder(ContactRecipientItem.class, ContactRecipientItemHolder.class,
        new ContactRecipientItemHolderBinder())
      .addBinder(NoResultsItem.class, NoResultsItemHolder.class,
        new NoResultsItemHolderBinder(messageHelper))
      .build();
    adapter = new ItemAdapter(holderCreatorFactory, binderFactory);
    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
    recyclerView.setItemAnimator(null);
    final Context context = getContext();
    recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,
      false));
    final RecyclerView.ItemDecoration divider = new HorizontalDividerItemDecoration.Builder(context)
      .drawable(R.drawable.divider)
      .marginResId(R.dimen.list_item_inset_horizontal)
      .build();
    recyclerView.addItemDecoration(divider);
    // Attaches the screen to the presenter.
    presenter.attachScreen(this);
  }

  @Override
  public void onStart() {
    super.onStart();
    // Sets the title.
    parentScreen.setTitle(messageHelper.payments());
    // Starts the presenter.
    presenter.start();
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
    // Detaches the screen from the presenter.
    presenter.detachScreen();
    // Unbinds all the annotated views and methods.
    unbinder.unbind();
  }

  @Override
  public void clear() {
    adapter.clear();
  }

  @Override
  public void add(@NonNull Item item) {
    adapter.add(item);
  }

  @NonNull
  @Override
  public Observable<String> onQueryChanged() {
    return searchView.onQueryChanged();
  }

  @Nullable
  @Override
  public RefreshIndicator getRefreshIndicator() {
    if (Utils.isNull(refreshIndicator) && Utils.isNotNull(swipeRefreshLayout)) {
      refreshIndicator = new SwipeRefreshLayoutRefreshIndicator(swipeRefreshLayout);
    }
    return refreshIndicator;
  }
}
