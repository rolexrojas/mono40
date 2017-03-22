package com.tpago.movil.dep.ui.main.transactions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tpago.movil.app.App;
import com.tpago.movil.R;
import com.tpago.movil.dep.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.dep.misc.Utils;
import com.tpago.movil.dep.domain.PhoneNumberRecipient;
import com.tpago.movil.dep.domain.Recipient;
import com.tpago.movil.dep.domain.RecipientType;
import com.tpago.movil.dep.ui.ChildFragment;
import com.tpago.movil.dep.ui.SwitchableContainerActivity;
import com.tpago.movil.dep.ui.main.transactions.bills.BillTransactionCreationFragment;
import com.tpago.movil.dep.ui.main.transactions.contacts.PhoneNumberTransactionCreationFragment;
import com.tpago.movil.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * TODO
 *
 * @author hecvasro
 */
public class TransactionCreationActivity
  extends SwitchableContainerActivity<TransactionCreationComponent>
  implements TransactionCreationContainer {
  private static final String KEY_RECIPIENT = "recipient";
  private static final String KEY_TRANSACTION_ID = "transactionId";

  private Unbinder unbinder;
  private TransactionCreationComponent component;

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
  private static Intent getLaunchIntent(@NonNull Context context) {
    return new Intent(context, TransactionCreationActivity.class);
  }

  @NonNull
  public static Intent getLaunchIntent(@NonNull Context context, @NonNull Recipient recipient) {
    final Intent intent = getLaunchIntent(context);
    intent.putExtra(KEY_RECIPIENT, recipient);
    return intent;
  }

  @NonNull
  public static Intent getLaunchIntent(
    @NonNull Context context,
    @NonNull String phoneNumber,
    boolean isAffiliated) {
    final Recipient r;
    if (isAffiliated) {
      r = new PhoneNumberRecipient(phoneNumber);
    } else {
      r = new NonAffiliatedPhoneNumberRecipient(phoneNumber);
    }
    return getLaunchIntent(context, r);
  }

  public static Pair<Recipient, String> deserializeResult(Intent intent) {
    if (Objects.isNull(intent)) {
      return null;
    } else {
      final Recipient recipient = (Recipient) intent.getSerializableExtra(KEY_RECIPIENT);
      final String transactionId = intent.getStringExtra(KEY_TRANSACTION_ID);
      return Pair.create(recipient, transactionId);
    }
  }

  @Override
  protected int layoutResourceIdentifier() {
    return R.layout.d_activity_transaction_creation;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    unbinder = ButterKnife.bind(this);
    // Asserts all the required arguments.
    final Bundle bundle = Utils.isNotNull(savedInstanceState) ? savedInstanceState : getIntent()
      .getExtras();
    if (Utils.isNull(bundle) || !bundle.containsKey(KEY_RECIPIENT)) {
      throw new NullPointerException("Argument '" + KEY_RECIPIENT + "' must be provided");
    } else {
      final Recipient recipient = (Recipient) bundle.getSerializable(KEY_RECIPIENT);
      // Injects all the annotated dependencies.
      component = DaggerTransactionCreationComponent.builder()
        .appComponent(((App) getApplication()).getComponent())
        .transactionCreationModule(new TransactionCreationModule(recipient))
        .build();
      component.inject(this);
      // Prepares the app bar.
      setSupportActionBar(toolbar);
      final ActionBar actionBar = getSupportActionBar();
      if (Utils.isNotNull(actionBar)) {
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
        default:
          fragment = null;
          break;
      }
      if (Objects.isNull(fragment)) {
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
    if (Utils.isNotNull(label)) {
      title = label;
      subtitle = recipient.getIdentifier();
    } else {
      title = recipient.getIdentifier();
      subtitle = null;
    }
    toolbar.post(new Runnable() {
      @Override
      public void run() {
        final int stringId;
        if (recipient.getType().equals(RecipientType.BILL)) {
          stringId = R.string.transaction_creation_title_bill;
        } else {
          stringId = R.string.transaction_creation_title;
        }
        toolbar.setTitle(getString(stringId, title));
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
