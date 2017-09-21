package com.tpago.movil.d.ui.main.recipient.index.category;

import static com.tpago.movil.d.ui.main.recipient.index.category.Category.TRANSFER;

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
import android.widget.Toast;

import com.tpago.movil.R;
import com.tpago.movil.d.domain.AccountRecipient;
import com.tpago.movil.d.domain.UserRecipient;
import com.tpago.movil.d.misc.Utils;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.data.util.BinderFactory;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.ui.Dialogs;
import com.tpago.movil.d.ui.main.DepMainActivity;
import com.tpago.movil.d.ui.main.MainContainer;
import com.tpago.movil.d.ui.main.PinConfirmationDialogFragment;
import com.tpago.movil.d.ui.main.list.ListItemAdapter;
import com.tpago.movil.d.ui.main.list.ListItemHolder;
import com.tpago.movil.d.ui.main.list.ListItemHolderCreatorFactory;
import com.tpago.movil.d.ui.main.list.NoResultsListItemItem;
import com.tpago.movil.d.ui.main.list.NoResultsListItemHolder;
import com.tpago.movil.d.ui.main.list.NoResultsListItemHolderBinder;
import com.tpago.movil.d.ui.main.list.NoResultsListItemHolderCreator;
import com.tpago.movil.d.ui.main.recipient.addition.AddRecipientActivity;
import com.tpago.movil.d.ui.main.transaction.TransactionCategory;
import com.tpago.movil.d.ui.main.transaction.TransactionCreationActivity;
import com.tpago.movil.d.ui.main.transaction.own.OwnTransactionCreationActivity;
import com.tpago.movil.d.ui.view.widget.FullScreenLoadIndicator;
import com.tpago.movil.d.ui.view.widget.LoadIndicator;
import com.tpago.movil.d.ui.ChildFragment;
import com.tpago.movil.d.ui.view.widget.SearchView;
import com.tpago.movil.d.ui.view.widget.SwipeRefreshLayoutRefreshIndicator;
import com.tpago.movil.init.InitActivity;
import com.tpago.movil.util.Objects;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;

/**
 * {@link RecipientCategoryScreen Screen} implementation that uses a {@link ChildFragment fragment}
 * as container.
 *
 * @author hecvasro
 */
public class RecipientCategoryFragment
  extends ChildFragment<MainContainer>
  implements RecipientCategoryScreen,
  ListItemHolder.OnClickListener,
  OnSaveButtonClickedListener {

  private static final int REQUEST_CODE_RECIPIENT_ADDITION = 0;
  private static final int REQUEST_CODE_TRANSACTION_CREATION = 1;
  private static final int REQUEST_CODE_OWN_TRANSACTION_CREATION = 3;

  private static final String KEY_CATEGORY = "category";

  private Unbinder unbinder;
  private ListItemAdapter adapter;

  private LoadIndicator loadIndicator;
  private LoadIndicator fullScreenLoadIndicator;
  private LoadIndicator currentLoadIndicator;

  private Pair<Integer, Pair<Recipient, String>> requestResult;

  private RecipientListItemHolderBinder recipientBinder;

  @BindView(R.id.search_view)
  SearchView searchView;
  @BindView(R.id.swipe_refresh_layout)
  SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;

  @Inject
  StringHelper stringHelper;
  @Inject
  Category category;
  @Inject
  RecipientCategoryPresenter presenter;

  @NonNull
  public static RecipientCategoryFragment create(Category category) {
    final Bundle bundle = new Bundle();
    bundle.putString(KEY_CATEGORY, category.name());
    final RecipientCategoryFragment fragment = new RecipientCategoryFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Prepares the fragment.
    setHasOptionsMenu(true);
    // Injects all the annotated dependencies.
    final String categoryName = getArguments()
      .getString(KEY_CATEGORY);
    final Category category = Category.valueOf(categoryName);
    final RecipientCategoryComponent component = DaggerRecipientCategoryComponent.builder()
      .depMainComponent(getContainer().getComponent())
      .recipientCategoryModule(new RecipientCategoryModule(category))
      .build();
    component.inject(this);
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState
  ) {
    return inflater.inflate(R.layout.d_fragment_payments, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all the annotated views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Prepares the actions and recipients list.
    final ListItemHolderCreatorFactory holderCreatorFactory = new ListItemHolderCreatorFactory
      .Builder()
      .addCreator(Recipient.class, new RecipientListItemHolderCreator(this))
      .addCreator(Action.class, new ActionListItemHolderCreator(this))
      .addCreator(NoResultsListItemItem.class, new NoResultsListItemHolderCreator())
      .build();
    final Context context = getContext();
    recipientBinder = new RecipientListItemHolderBinder(this.category);
    final BinderFactory binderFactory = new BinderFactory.Builder()
      .addBinder(
        Recipient.class,
        RecipientListItemHolder.class,
        recipientBinder
      )
      .addBinder(
        Action.class,
        ActionListItemHolder.class,
        new ActionListItemHolderBinder(stringHelper, category)
      )
      .addBinder(
        NoResultsListItemItem.class,
        NoResultsListItemHolder.class,
        new NoResultsListItemHolderBinder(context)
      )
      .build();
    adapter = new ListItemAdapter(holderCreatorFactory, binderFactory);
    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
    recyclerView
      .setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    final RecyclerView.ItemDecoration divider = new HorizontalDividerItemDecoration.Builder(context)
      .drawable(R.drawable.d_divider)
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
    this.getContainer()
      .setTitle(this.getString(this.category.stringId));
    // Starts the presenter.
    presenter.start();
  }

  @Override
  public void onResume() {
    super.onResume();
    if (Utils.isNotNull(requestResult)) {
      final Recipient recipient = requestResult.second.first;
      final int code = requestResult.first;
      if (recipient instanceof UserRecipient || code == REQUEST_CODE_TRANSACTION_CREATION || code == REQUEST_CODE_OWN_TRANSACTION_CREATION) {
        presenter.showTransactionSummary(recipient, requestResult.second.second);
      } else if (code == REQUEST_CODE_RECIPIENT_ADDITION) {
        presenter.addRecipient(recipient);
      }
      requestResult = null;
    }
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);

    inflater.inflate(R.menu.recipient_index_category, menu);

    final String categoryDeletionString = this.getString(this.category.subjectStringId)
      .toLowerCase();
    final MenuItem addMenuItem = menu.findItem(R.id.recipientIndexCategory_menuItem_add);
    addMenuItem
      .setTitle(String.format(this.getString(R.string.format_add), categoryDeletionString));
    final MenuItem removeMenuItem = menu.findItem(R.id.recipientIndexCategory_menuItem_remove);
    removeMenuItem
      .setTitle(String.format(this.getString(R.string.format_remove), categoryDeletionString));
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.recipientIndexCategory_menuItem_add:
        startActivityForResult(
          AddRecipientActivity.getLaunchIntent(this.getContext(), this.category),
          REQUEST_CODE_RECIPIENT_ADDITION
        );
        return true;
      case R.id.recipientIndexCategory_menuItem_remove:
        presenter.startDeleting();
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
          requestResult = Pair.create(requestCode, Pair.create(recipient, (String) null));
        }
      }
    } else if (requestCode == REQUEST_CODE_TRANSACTION_CREATION) {
      if (resultCode == Activity.RESULT_OK) {
        final Pair<Recipient, String> result = TransactionCreationActivity.deserializeResult(data);
        if (Utils.isNotNull(result)) {
          requestResult = Pair.create(requestCode, result);
        }
      }
    } else if (requestCode == REQUEST_CODE_OWN_TRANSACTION_CREATION) {
      if (resultCode == Activity.RESULT_OK) {
        final String transactionId = OwnTransactionCreationActivity.deserializeResult(data);
        if (Objects.checkIfNotNull(transactionId)) {
          requestResult = Pair.create(requestCode, Pair.create((Recipient) null, transactionId));
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
        fullScreenLoadIndicator = new FullScreenLoadIndicator(getChildFragmentManager());
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
  public void openInitScreen() {
    startActivity(InitActivity.getLaunchIntent(getContext()));
  }

  @Override
  public void finish() {
    getActivity().finish();
  }

  @Override
  public void setDeleting(boolean deleting) {
    final DepMainActivity activity = (DepMainActivity) getActivity();
    if (deleting) {
      recipientBinder.setDeleting(true);
      activity.showDeleteLinearLayout();
      activity.setDeleteButtonEnabled(false);
      activity.setOnDeleteButtonClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          presenter.deleteSelectedRecipients();
        }
      });
      activity.setOnCancelButtonClickedListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          presenter.stopDeleting();
        }
      });
      activity.setOnBackPressedListener(new DepMainActivity.OnBackPressedListener() {
        @Override
        public boolean onBackPressed() {
          presenter.stopDeleting();
          return true;
        }
      });
    } else {
      recipientBinder.setDeleting(false);
      activity.hideDeleteLinearLayout();
      activity.setDeleteButtonEnabled(false);
      activity.setOnDeleteButtonClickListener(null);
      activity.setOnCancelButtonClickedListener(null);
      activity.setOnBackPressedListener(null);
    }
    adapter.notifyDataSetChanged();
  }

  @Override
  public void startTransfer(Recipient recipient) {
    startActivityForResult(
      TransactionCreationActivity.getLaunchIntent(
        this.getActivity(),
        TransactionCategory.transform(this.category),
        recipient
      ),
      REQUEST_CODE_TRANSACTION_CREATION
    );
  }

  @Override
  public void showMessage(String message) {
    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT)
      .show();
  }

  @Override
  public void remove(Object item) {
    adapter.remove(item);
  }

  @Override
  public void setDeleteButtonEnabled(boolean enabled) {
    ((DepMainActivity) getActivity()).setDeleteButtonEnabled(enabled);
  }

  @Override
  public void showRecipientAdditionDialog(Recipient recipient) {
    RecipientAdditionDialogFragment.create(recipient)
      .show(getChildFragmentManager(), null);
  }

  @Override
  public void showTransactionSummary(
    Recipient recipient,
    boolean alreadyExists,
    String transactionId
  ) {
    TransactionSummaryDialogFragment.create(recipient, alreadyExists, transactionId)
      .show(getChildFragmentManager(), null);
  }

  @Override
  public void requestPin() {
    final View rootView = ButterKnife.findById(getActivity(), android.R.id.content);
    final int x = Math.round((rootView.getRight() - rootView.getLeft()) / 2);
    final int y = Math.round((rootView.getBottom() - rootView.getTop()) / 2);
    PinConfirmationDialogFragment.show(
      getChildFragmentManager(),
      getString(R.string.remove_recipients),
      new PinConfirmationDialogFragment.Callback() {
        @Override
        public void confirm(@NonNull String pin) {
          presenter.onPinRequestFinished(pin);
        }
      },
      x,
      y
    );
  }

  @Override
  public void showGenericErrorDialog(String message) {
    Dialogs.builder(getContext())
      .setTitle(R.string.error_generic_title)
      .setMessage(message)
      .setPositiveButton(R.string.error_positive_button_text, null)
      .show();
  }

  @Override
  public void showGenericErrorDialog() {
    showGenericErrorDialog(getString(R.string.error_generic));
  }

  @Override
  public void showUnavailableNetworkError() {
    Toast.makeText(getContext(), R.string.error_unavailable_network, Toast.LENGTH_LONG)
      .show();
  }

  @Override
  public void setDeletingResult(boolean result) {
    PinConfirmationDialogFragment.dismiss(getChildFragmentManager(), result);
  }

  @Override
  public void onClick(int position) {
    final Object item = adapter.get(position);
    if (item instanceof Recipient) {
      if (item instanceof UserRecipient) {
        final Context context = this.getContext();
        if (category == TRANSFER) {
          this.startActivityForResult(
            OwnTransactionCreationActivity.createLaunchIntent(context),
            REQUEST_CODE_TRANSACTION_CREATION
          );
        } else {
          this.startActivityForResult(
            TransactionCreationActivity.getLaunchIntent(
              context,
              TransactionCategory.transform(this.category),
              (UserRecipient) item
            ),
            REQUEST_CODE_TRANSACTION_CREATION
          );
        }
      } else {
        presenter.resolve((Recipient) item);
      }
    } else if (item instanceof Action) {
      switch (((Action) item).type()) {
        case ADD_PHONE_NUMBER:
          presenter.addRecipient(((PhoneNumberAction) item).phoneNumber());
          break;
        case TRANSACTION_WITH_PHONE_NUMBER:
          presenter.startTransfer(((PhoneNumberAction) item).phoneNumber());
          break;
        case ADD_ACCOUNT:
          presenter.addRecipient(
            AccountRecipient.builder()
              .number(((AccountAction) item).number())
              .build()
          );
          break;
        case TRANSACTION_WITH_ACCOUNT:
          this.presenter.startTransfer(((AccountAction) item).number());
          break;
      }
    }
  }

  @Override
  public void onSaveButtonClicked(Recipient recipient, String label) {
    presenter.updateRecipient(recipient, label);
  }
}
