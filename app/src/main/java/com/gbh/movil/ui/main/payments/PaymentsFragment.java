package com.gbh.movil.ui.main.payments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
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
import com.gbh.movil.data.util.BinderFactory;
import com.gbh.movil.domain.PhoneNumber;
import com.gbh.movil.domain.Recipient;
import com.gbh.movil.ui.UiUtils;
import com.gbh.movil.ui.main.MainContainer;
import com.gbh.movil.ui.main.list.ListItemAdapter;
import com.gbh.movil.ui.main.list.ListItemHolder;
import com.gbh.movil.ui.main.list.ListItemHolderCreatorFactory;
import com.gbh.movil.ui.main.list.NoResultsListItemItem;
import com.gbh.movil.ui.main.list.NoResultsListItemHolder;
import com.gbh.movil.ui.main.list.NoResultsListItemHolderBinder;
import com.gbh.movil.ui.main.list.NoResultsListItemHolderCreator;
import com.gbh.movil.ui.main.payments.recipients.AddRecipientActivity;
import com.gbh.movil.ui.main.transactions.TransactionCreationActivity;
import com.gbh.movil.ui.view.widget.FullScreenRefreshIndicator;
import com.gbh.movil.ui.view.widget.LoadIndicator;
import com.gbh.movil.ui.ChildFragment;
import com.gbh.movil.ui.view.widget.SearchView;
import com.gbh.movil.ui.view.widget.SwipeRefreshLayoutRefreshIndicator;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;

/**
 * {@link PaymentsScreen Screen} implementation that uses a {@link ChildFragment fragment} as
 * container.
 *
 * @author hecvasro
 */
public class PaymentsFragment extends ChildFragment<MainContainer>
  implements PaymentsScreen, ListItemHolder.OnClickListener,
  ConfirmationDialogFragment.OnSaveButtonClickedListener {
  /**
   * TODO
   */
  private static final int REQUEST_CODE_RECIPIENT_ADDITION = 0;
  /**
   * TODO
   */
  private static final int REQUEST_CODE_TRANSACTION_CREATION = 1;

  private Unbinder unbinder;
  private ListItemAdapter adapter;

  private LoadIndicator loadIndicator;
  private LoadIndicator fullScreenLoadIndicator;
  private LoadIndicator currentLoadIndicator;

  private Pair<Integer, Recipient> requestResult;

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
      .mainComponent(getContainer().getComponent())
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
    final ListItemHolderCreatorFactory holderCreatorFactory = new ListItemHolderCreatorFactory.Builder()
      .addCreator(Recipient.class, new RecipientListItemHolderCreator(this))
      .addCreator(Action.class, new ActionListItemHolderCreator(this))
      .addCreator(NoResultsListItemItem.class, new NoResultsListItemHolderCreator())
      .build();
    final Context context = getContext();
    final BinderFactory binderFactory = new BinderFactory.Builder()
      .addBinder(Recipient.class, RecipientListItemHolder.class,
        new RecipientListItemHolderBinder())
      .addBinder(Action.class, ActionListItemHolder.class,
        new ActionListItemHolderBinder(stringHelper))
      .addBinder(NoResultsListItemItem.class, NoResultsListItemHolder.class,
        new NoResultsListItemHolderBinder(context))
      .build();
    adapter = new ListItemAdapter(holderCreatorFactory, binderFactory);
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
    getContainer().setTitle(getString(R.string.payments_title));
    // Starts the presenter.
    presenter.start();
  }

  @Override
  public void onResume() {
    super.onResume();
    if (Utils.isNotNull(requestResult)) {
      final int code = requestResult.first;
      final Recipient recipient = requestResult.second;
      if (code == REQUEST_CODE_RECIPIENT_ADDITION) {
        presenter.addRecipient(recipient);
      } else if (code == REQUEST_CODE_TRANSACTION_CREATION) {
        presenter.showTransactionConfirmation(recipient);
      }
      requestResult = null;
    }
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
          requestResult = Pair.create(requestCode, recipient);
        }
      }
    } else if (requestCode == REQUEST_CODE_TRANSACTION_CREATION) {
      if (resultCode == Activity.RESULT_OK) {
        final Recipient recipient = TransactionCreationActivity.deserializeResult(data);
        if (Utils.isNotNull(recipient)) {
          requestResult = Pair.create(requestCode, recipient);
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
    adapter.updateOrAdd(item);
  }

  @Override
  public void showConfirmationDialog(@NonNull Recipient recipient, @NonNull String title,
    @Nullable String message) {
    ConfirmationDialogFragment.newInstance(recipient, title, message)
      .show(getChildFragmentManager(), null);
  }

  @Override
  public void showUnaffiliatedRecipientAdditionNotAvailableMessage() {
    UiUtils.createDialog(getContext(), getString(R.string.sorry),
      getString(R.string.info_not_available_unaffiliated_contact_recipient_addition),
      getString(R.string.ok), null, null, null).show();
  }

  @Override
  public void showPaymentToUnaffiliatedRecipientNotAvailableMessage() {
    UiUtils.createDialog(getContext(), getString(R.string.sorry),
      getString(R.string.info_not_available_payment_to_unaffiliated_recipient),
      getString(R.string.ok), null, null, null).show();
  }

  @Override
  public void startTransfer(@NonNull PhoneNumber phoneNumber) {
    startActivityForResult(TransactionCreationActivity.getLaunchIntent(getContext(), phoneNumber),
      REQUEST_CODE_TRANSACTION_CREATION);
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
          presenter.startTransfer(((PhoneNumberAction) item).getPhoneNumber());
          break;
      }
    }
  }

  @Override
  public void onSaveButtonClicked(@NonNull Recipient recipient, @Nullable String label) {
    presenter.updateRecipient(recipient, label);
  }
}
