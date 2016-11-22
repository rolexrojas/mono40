package com.gbh.movil.ui.main.payments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.gbh.movil.R;
import com.gbh.movil.Utils;
import com.gbh.movil.data.StringHelper;
import com.gbh.movil.domain.PhoneNumber;
import com.gbh.movil.ui.main.MainComponent;
import com.gbh.movil.ui.main.list.ItemAdapter;
import com.gbh.movil.ui.main.list.Holder;
import com.gbh.movil.ui.main.list.ItemHolderBinderFactory;
import com.gbh.movil.ui.main.list.ItemHolderCreatorFactory;
import com.gbh.movil.ui.main.list.NoResultsItem;
import com.gbh.movil.ui.main.list.NoResultsHolder;
import com.gbh.movil.ui.main.list.NoResultsHolderBinder;
import com.gbh.movil.ui.main.list.NoResultsHolderCreator;
import com.gbh.movil.ui.main.payments.recipients.AddRecipientActivity;
import com.gbh.movil.ui.view.widget.RefreshIndicator;
import com.gbh.movil.ui.view.widget.SwipeRefreshLayoutRefreshIndicator;
import com.gbh.movil.ui.SubFragment;
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
public class PaymentsFragment extends SubFragment<MainComponent> implements PaymentsScreen,
  Holder.OnClickListener {
  private Unbinder unbinder;
  private RefreshIndicator refreshIndicator;
  private ItemAdapter itemAdapter;

  @BindView(R.id.search_view)
  SearchView searchView;
  @BindView(R.id.swipe_refresh_layout)
  SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;

  @Inject
  StringHelper stringHelper;
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

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Prepares the fragment.
    setHasOptionsMenu(true);
    // Injects all the annotated dependencies.
    final PaymentsComponent component = DaggerPaymentsComponent.builder()
      .mainComponent(parentScreen.getComponent())
      .build();
    component.inject(this);
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
    // Binds all the annotated views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Prepares the actions and recipients list.
    final ItemHolderCreatorFactory holderCreatorFactory = new ItemHolderCreatorFactory.Builder()
      .addCreator(PhoneNumberRecipientItem.class, new RecipientHolderCreator(this))
      .addCreator(Action.class, new ActionHolderCreator(this))
      .addCreator(NoResultsItem.class, new NoResultsHolderCreator())
      .build();
    final Context context = getContext();
    final ItemHolderBinderFactory binderFactory = new ItemHolderBinderFactory.Builder()
      .addBinder(PhoneNumberRecipientItem.class, RecipientHolder.class,
        new PhoneNumberRecipientItemHolderBinder())
      .addBinder(Action.class, ActionHolder.class, new ActionHolderBinder(stringHelper))
      .addBinder(NoResultsItem.class, NoResultsHolder.class, new NoResultsHolderBinder(context))
      .build();
    itemAdapter = new ItemAdapter(holderCreatorFactory, binderFactory);
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
    // Attaches the screen to the presenter.
    presenter.attachScreen(this);
  }

  @Override
  public void onStart() {
    super.onStart();
    // Sets the title.
    parentScreen.setTitle(getString(R.string.payments_title));
    // Starts the presenter.
    presenter.start();
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    // Inflates the menu of the fragment.
    inflater.inflate(R.menu.payments, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.payments_menu_option_add_recipient:
        startActivity(AddRecipientActivity.getLaunchIntent(getContext()));
        return true;
      case R.id.payments_menu_option_remove_recipient:
        // TODO
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
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

  @NonNull
  @Override
  public Observable<String> onQueryChanged() {
    return searchView.onQueryChanged();
  }

  @Override
  public void clear() {
    itemAdapter.clearItems();
  }

  @Override
  public void add(@NonNull Object item) {
    itemAdapter.addItem(item);
  }

  @Override
  public void startAddRecipientScreen(@NonNull PhoneNumber phoneNumber) {
    startActivity(AddRecipientActivity.getLaunchIntent(getContext(), phoneNumber));
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
    presenter.onItemClicked(itemAdapter.getItem(position));
  }
}
