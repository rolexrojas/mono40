package com.tpago.movil.d.ui.main.recipient.addition;

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

import com.tpago.movil.Partner;
import com.tpago.movil.R;
import com.tpago.movil.d.misc.Utils;
import com.tpago.movil.d.data.util.BinderFactory;
import com.tpago.movil.d.ui.ChildFragment;
import com.tpago.movil.d.ui.main.list.ListItemHolder;
import com.tpago.movil.d.ui.main.list.ListItemAdapter;
import com.tpago.movil.d.ui.main.list.ListItemHolderCreatorFactory;
import com.tpago.movil.d.ui.main.list.NoResultsListItemHolder;
import com.tpago.movil.d.ui.main.list.NoResultsListItemHolderBinder;
import com.tpago.movil.d.ui.main.list.NoResultsListItemHolderCreator;
import com.tpago.movil.d.ui.main.list.NoResultsListItemItem;
import com.tpago.movil.d.ui.view.widget.LoadIndicator;
import com.tpago.movil.d.ui.view.widget.SwipeRefreshLayoutRefreshIndicator;
import com.tpago.movil.domain.Bank;
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
  extends ChildFragment<SearchOrChooseRecipientContainer>
  implements RecipientCandidateListScreen,
  ListItemHolder.OnClickListener {

  private Unbinder unbinder;
  private LoadIndicator loadIndicator;
  private ListItemAdapter adapter;

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
  protected abstract ListItemHolderCreatorFactory.Builder createHolderCreatorFactoryBuilder();

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  protected abstract BinderFactory.Builder createHolderBinderFactoryBuilder();

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState
  ) {
    return inflater.inflate(R.layout.d_fragment_recipient_candidate_list, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all the annotated views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Prepares the contact item container.
    final ListItemHolderCreatorFactory holderCreatorFactory = createHolderCreatorFactoryBuilder()
      .addCreator(NoResultsListItemItem.class, new NoResultsListItemHolderCreator())
      .build();
    final Context context = getContext();
    final BinderFactory holderBinderFactory = createHolderBinderFactoryBuilder()
      .addBinder(NoResultsListItemItem.class, NoResultsListItemHolder.class,
        new NoResultsListItemHolderBinder(context)
      )
      .build();
    adapter = new ListItemAdapter(holderCreatorFactory, holderBinderFactory);
    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,
      false
    ));
    final RecyclerView.ItemDecoration divider = new HorizontalDividerItemDecoration.Builder(context)
      .drawable(R.drawable.d_divider)
      .marginResId(R.dimen.space_horizontal_normal)
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
  public void clear() {
    adapter.clear();
  }

  @Override
  public void add(@NonNull Object item) {
    adapter.add(item);
  }

  @Nullable
  public LoadIndicator getRefreshIndicator() {
    if (Utils.isNull(loadIndicator) && Utils.isNotNull(swipeRefreshLayout)) {
      loadIndicator = new SwipeRefreshLayoutRefreshIndicator(swipeRefreshLayout);
    }
    return loadIndicator;
  }

  @NonNull
  @Override
  public Observable<String> onQueryChanged() {
    return getContainer().onQueryChanged();
  }

  @Override
  public void onClick(int position) {
    final Object item = adapter.get(position);
    if (item instanceof Contact) {
      getContainer().onContactClicked((Contact) item);
    } else if (item instanceof Partner) {
      getContainer().onPartnerClicked(((Partner) item));
    } else if (item instanceof Bank) {
      getContainer().onBankClicked((Bank) item);
    }
  }
}
