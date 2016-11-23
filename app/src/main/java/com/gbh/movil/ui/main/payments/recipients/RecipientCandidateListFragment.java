package com.gbh.movil.ui.main.payments.recipients;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gbh.movil.R;
import com.gbh.movil.Utils;
import com.gbh.movil.ui.SubFragment;
import com.gbh.movil.ui.main.list.Holder;
import com.gbh.movil.ui.main.list.Adapter;
import com.gbh.movil.ui.main.list.HolderBinderFactory;
import com.gbh.movil.ui.main.list.HolderCreatorFactory;
import com.gbh.movil.ui.main.list.NoResultsHolder;
import com.gbh.movil.ui.main.list.NoResultsHolderBinder;
import com.gbh.movil.ui.main.list.NoResultsHolderCreator;
import com.gbh.movil.ui.main.list.NoResultsItem;
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
public abstract class RecipientCandidateListFragment<P extends RecipientCandidateListPresenter>
  extends SubFragment<AddRecipientComponent> implements RecipientCandidateListScreen,
  Holder.OnClickListener {
  private SearchOrChooseRecipientScreen directParent;
  private Unbinder unbinder;
  private RefreshIndicator refreshIndicator;
  private Adapter adapter;

  @Inject
  protected P presenter;

  @BindView(R.id.swipe_refresh_layout)
  SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  protected abstract HolderCreatorFactory.Builder createHolderCreatorFactoryBuilder();

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  protected abstract HolderBinderFactory.Builder createHolderBinderFactoryBuilder();

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    // Attaches the direct parent to the fragment.
    final Fragment fragment = getParentFragment();
    if (Utils.isNull(fragment)) {
      throw new NullPointerException("Parent fragment is missing");
    } else if (!(fragment instanceof SearchOrChooseRecipientScreen)) {
      throw new ClassCastException("Parent fragment must implement the 'SearchOrChooseRecipientScreen' interface");
    } else {
      directParent = (SearchOrChooseRecipientScreen) fragment;
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_recipient_candidate_list, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all the annotated views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Prepares the contact item container.
    final HolderCreatorFactory holderCreatorFactory = createHolderCreatorFactoryBuilder()
      .addCreator(NoResultsItem.class, new NoResultsHolderCreator())
      .build();
    final Context context = getContext();
    final HolderBinderFactory holderBinderFactory = createHolderBinderFactoryBuilder()
      .addBinder(NoResultsItem.class, NoResultsHolder.class, new NoResultsHolderBinder(context))
      .build();
    adapter = new Adapter(holderCreatorFactory, holderBinderFactory);
    recyclerView.setAdapter(adapter);
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
  }

  @Override
  public void onStart() {
    super.onStart();
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
    // Detaches the presenter for the fragment.
    presenter.detachScreen();
    // Unbinds all the annotated views and methods.
    unbinder.unbind();
  }

  @Override
  public void onDetach() {
    super.onDetach();
    // Detaches the direct parent from the fragment.
    directParent = null;
  }

  @Override
  public void clear() {
    adapter.clear();
  }

  @Override
  public void add(@NonNull Object item) {
    adapter.add(item);
  }

  @Nullable
  @Override
  public RefreshIndicator getRefreshIndicator() {
    if (Utils.isNull(refreshIndicator) && Utils.isNotNull(swipeRefreshLayout)) {
      refreshIndicator = new SwipeRefreshLayoutRefreshIndicator(swipeRefreshLayout);
    }
    return refreshIndicator;
  }

  @NonNull
  @Override
  public Observable<String> onQueryChanged() {
    return directParent.onQueryChanged();
  }

  @Override
  public void onClick(int position) {
    // TODO: Let the direct parent screen that a recipient candidate was clicked.
  }
}
