package com.tpago.movil.d.ui.main.transaction;

import static com.tpago.movil.d.misc.Utils.isNotNull;
import static com.tpago.movil.dep.Objects.checkIfNull;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tpago.movil.dep.App;
import com.tpago.movil.R;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductRecipient;
import com.tpago.movil.d.misc.Utils;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.RecipientType;
import com.tpago.movil.d.ui.ChildFragment;
import com.tpago.movil.d.ui.SwitchableContainerActivity;
import com.tpago.movil.d.ui.main.transaction.bills.BillTransactionCreationFragment;
import com.tpago.movil.d.ui.main.transaction.contacts.PhoneNumberTransactionCreationFragment;
import com.tpago.movil.d.ui.main.transaction.products.CreditCardTransactionCreationFragment;
import com.tpago.movil.d.ui.main.transaction.products.LoanTransactionCreationFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public class TransactionCreationActivity
  extends SwitchableContainerActivity<TransactionCreationComponent>
  implements TransactionCreationContainer {

  private static final String KEY_RECIPIENT = "recipient";
  private static final String KEY_TRANSACTION_ID = "transactionId";
  private static final String KEY_TRANSACTION_CATEGORY = "transactionCategory";

  private Unbinder unbinder;
  private TransactionCreationComponent component;

  @Inject
  TransactionCategory transactionCategory;
  @Inject
  Recipient recipient;

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  private static Intent serializeResult(Recipient recipient, String transactionId) {
    final Intent intent = new Intent();
    intent.putExtra(KEY_RECIPIENT, recipient);
    intent.putExtra(KEY_TRANSACTION_ID, transactionId);
    return intent;
  }

  @NonNull
  public static Intent getLaunchIntent(
    @NonNull Context context,
    @NonNull TransactionCategory transactionCategory,
    @NonNull Recipient recipient
  ) {
    final Intent intent = new Intent(context, TransactionCreationActivity.class);
    intent.putExtra(KEY_TRANSACTION_CATEGORY, transactionCategory.name());
    intent.putExtra(KEY_RECIPIENT, recipient);
    return intent;
  }


  public static Pair<Recipient, String> deserializeResult(Intent intent) {
    if (checkIfNull(intent)) {
      return null;
    } else {
      final Recipient recipient = intent.getParcelableExtra(KEY_RECIPIENT);
      final String transactionId = intent.getStringExtra(KEY_TRANSACTION_ID);
      return Pair.create(recipient, transactionId);
    }
  }

  @Override
  protected int layoutResId() {
    return R.layout.d_activity_transaction_creation;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    unbinder = ButterKnife.bind(this);
    // Asserts all the required arguments.
    final Bundle bundle = isNotNull(savedInstanceState) ? savedInstanceState : getIntent()
      .getExtras();
    if (Utils.isNull(bundle)) {
      throw new NullPointerException("bundle == null");
    } else if (!bundle.containsKey(KEY_TRANSACTION_CATEGORY)) {
      throw new NullPointerException(
        "Argument '" + KEY_TRANSACTION_CATEGORY + "' must be provided");
    } else if (!bundle.containsKey(KEY_RECIPIENT)) {
      throw new NullPointerException("Argument '" + KEY_RECIPIENT + "' must be provided");
    } else {
      final TransactionCategory transactionCategory = TransactionCategory
        .valueOf(bundle.getString(KEY_TRANSACTION_CATEGORY));
      final Recipient recipient = bundle.getParcelable(KEY_RECIPIENT);
      // Injects all the annotated dependencies.
      component = DaggerTransactionCreationComponent.builder()
        .appComponent(((App) getApplication()).getComponent())
        .transactionCreationModule(new TransactionCreationModule(transactionCategory, recipient))
        .build();
      component.inject(this);
      // Prepares the app bar.
      setSupportActionBar(toolbar);
      final ActionBar actionBar = getSupportActionBar();
      if (isNotNull(actionBar)) {
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
      }
      // Resolves the initial sub-screen.
      final RecipientType type = recipient.getType();
      final ChildFragment<TransactionCreationContainer> fragment;
      switch (type) {
        case PHONE_NUMBER:
          fragment = PhoneNumberTransactionCreationFragment.newInstance();
          break;
        case NON_AFFILIATED_PHONE_NUMBER:
          fragment = PhoneNumberTransactionCreationFragment.newInstance();
          break;
        case BILL:
          fragment = BillTransactionCreationFragment.create();
          break;
        case PRODUCT:
          final ProductRecipient productRecipient = (ProductRecipient) recipient;
          if (Product.checkIfCreditCard(productRecipient.getProduct())) {
            fragment = CreditCardTransactionCreationFragment.create();
          } else {
            fragment = LoanTransactionCreationFragment.create();
          }
          break;
        case ACCOUNT:
          fragment = PhoneNumberTransactionCreationFragment.newInstance();
          break;
        case USER:
          fragment = PhoneNumberTransactionCreationFragment.newInstance();
          break;
        default:
          fragment = null;
          break;
      }
      if (checkIfNull(fragment)) {
        finish();
      } else {
        setChildFragment(fragment, false, false);
      }
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    // Sets the title and subtitle.
    final String title;
    final String subtitle;
    final String label = recipient.getLabel();
    if (isNotNull(label)) {
      title = label;
      subtitle = recipient.getIdentifier();
    } else {
      title = recipient.getIdentifier();
      subtitle = null;
    }
    toolbar.post(new Runnable() {
      @Override
      public void run() {
        toolbar.setTitle(
          getString(
            R.string.format_transaction_title,
            getString(transactionCategory.stringId),
            title
          )
        );
        toolbar.setSubtitle(subtitle);
      }
    });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unbinder.unbind();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
  }

  @Nullable
  @Override
  public TransactionCreationComponent getComponent() {
    return component;
  }

  @Override
  public void setChildFragment(ChildFragment<TransactionCreationContainer> fragment) {
    setChildFragment(fragment, true, true);
  }

  @Override
  public void finish(boolean succeeded, String transactionId) {
    if (succeeded) {
      setResult(RESULT_OK, serializeResult(recipient, transactionId));
    } else {
      setResult(RESULT_CANCELED);
    }
    finish();
  }
}
