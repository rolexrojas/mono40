package com.gbh.movil.ui.main.payments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.gbh.movil.domain.Recipient;
import com.gbh.movil.ui.UiUtils;
import com.gbh.movil.ui.main.MainContainer;
import com.gbh.movil.ui.main.list.Adapter;
import com.gbh.movil.ui.main.list.Holder;
import com.gbh.movil.ui.main.list.HolderBinderFactory;
import com.gbh.movil.ui.main.list.HolderCreatorFactory;
import com.gbh.movil.ui.main.list.NoResultsItem;
import com.gbh.movil.ui.main.list.NoResultsHolder;
import com.gbh.movil.ui.main.list.NoResultsHolderBinder;
import com.gbh.movil.ui.main.list.NoResultsHolderCreator;
import com.gbh.movil.ui.main.payments.recipients.AddRecipientActivity;
import com.gbh.movil.ui.main.payments.transactions.TransactionCreationActivity;
import com.gbh.movil.ui.view.widget.FullScreenRefreshIndicator;
import com.gbh.movil.ui.view.widget.LoadIndicator;
import com.gbh.movil.ui.SubFragment;
import com.gbh.movil.ui.view.widget.SearchView;
import com.gbh.movil.ui.view.widget.SwipeRefreshLayoutRefreshIndicator;
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
public class PaymentsFragment extends SubFragment<MainContainer>
  implements PaymentsScreen, Holder.OnClickListener,
  RecipientAdditionConfirmationDialogFragment.OnSaveRecipientButtonClickedListener {
  /**
   * TODO
   */
  private static final int REQUEST_CODE_RECIPIENT_ADDITION = 0;
  /**
   * TODO
   */
  private static final int REQUEST_CODE_TRANSACTION_CREATION = 1;

  private Unbinder unbinder;
  private Adapter adapter;

  private LoadIndicator loadIndicator;
  private LoadIndicator fullScreenLoadIndicator;
  private LoadIndicator currentLoadIndicator;

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
      .mainComponent(container.getComponent())
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
    final HolderCreatorFactory holderCreatorFactory = new HolderCreatorFactory.Builder()
      .addCreator(Recipient.class, new RecipientHolderCreator(this))
      .addCreator(Action.class, new ActionHolderCreator(this))
      .addCreator(NoResultsItem.class, new NoResultsHolderCreator())
      .build();
    final Context context = getContext();
    final HolderBinderFactory binderFactory = new HolderBinderFactory.Builder()
      .addBinder(Recipient.class, RecipientHolder.class, new RecipientHolderBinder())
      .addBinder(Action.class, ActionHolder.class, new ActionHolderBinder(stringHelper))
      .addBinder(NoResultsItem.class, NoResultsHolder.class, new NoResultsHolderBinder(context))
      .build();
    adapter = new Adapter(holderCreatorFactory, binderFactory);
    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,
      false));
    final RecyclerView.ItemDecoration divider = new HorizontalDividerItemDecoration.Builder(context)
      .drawable(R.drawable.divider)
      .marginResId(R.dimen.space_horizontal_normal)
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
    container.setTitle(getString(R.string.payments_title));
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
        UiUtils.createDialog(getContext(), getString(R.string.sorry),
          getString(R.string.info_not_available_remove_recipients), getString(R.string.ok), null,
          null, null).show();
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

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE_RECIPIENT_ADDITION) {
      if (resultCode == Activity.RESULT_OK) {
        final Recipient recipient = AddRecipientActivity.deserializeResult(data);
        if (Utils.isNotNull(recipient)) {
          presenter.addRecipient(recipient);
        } else {
          // TODO: Let the user know that the recipient couldn't be added.
        }
      }
    }
  }

  @NonNull
  @Override
  public Observable<String> onQueryChanged() {
    return searchView.onQueryChanged();
  }

  @Override
  public void clearQuery() {
    searchView.clear();
  }

  @Override
  public void showLoadIndicator(boolean fullscreen) {
    if (fullscreen) {
      if (Utils.isNull(fullScreenLoadIndicator)) {
        fullScreenLoadIndicator = new FullScreenRefreshIndicator(getChildFragmentManager());
      }
      currentLoadIndicator = fullScreenLoadIndicator;
    } else {
      if (Utils.isNull(loadIndicator)) {
        loadIndicator = new SwipeRefreshLayoutRefreshIndicator(swipeRefreshLayout);
      }
      currentLoadIndicator = loadIndicator;
    }
    currentLoadIndicator.show();
  }

  @Override
  public void hideLoadIndicator() {
    if (Utils.isNotNull(currentLoadIndicator)) {
      currentLoadIndicator.hide();
      currentLoadIndicator = null;
    }
  }

  @Override
  public void clear() {
    adapter.clear();
  }

  @Override
  public void add(@NonNull Object item) {
    adapter.add(item);
  }

  @Override
  public void update(@NonNull Object item) {
    final int index = adapter.indexOf(item);
    if (index >= 0) {
      adapter.notifyItemChanged(index);
    } else {
      add(item);
    }
  }

  @Override
  public void showRecipientAdditionConfirmationDialog(@NonNull Recipient recipient) {
    RecipientAdditionConfirmationDialogFragment.newInstance(recipient)
      .show(getChildFragmentManager(), null);
  }

  @Override
  public void showUnaffiliatedRecipientAdditionNotAvailableMessage() {
    UiUtils.createDialog(getContext(), getString(R.string.sorry),
      getString(R.string.info_not_available_unaffiliated_contact_recipient_addition),
      getString(R.string.ok), null, null, null).show();
  }

  @Override
  public void onClick(int position) {
    final Context context = getContext();
    final Object item = adapter.get(position);
    if (item instanceof Recipient) {
      startActivityForResult(TransactionCreationActivity.getLaunchIntent(context, (Recipient) item),
        REQUEST_CODE_TRANSACTION_CREATION);
    } else if (item instanceof Action) {
      switch (((Action) item).getType()) {
        case ActionType.ADD_PHONE_NUMBER:
          presenter.addRecipient(((PhoneNumberAction) item).getPhoneNumber());
          break;
        case ActionType.TRANSACTION_WITH_PHONE_NUMBER:
          startActivityForResult(TransactionCreationActivity.getLaunchIntent(context,
            ((PhoneNumberAction) item).getPhoneNumber()), REQUEST_CODE_TRANSACTION_CREATION);
          break;
      }
    }
  }

  @Override
  public void onSaveRecipientButtonClicked(@NonNull Recipient recipient, @Nullable String label) {
    presenter.updateRecipient(recipient, label);
  }
}
