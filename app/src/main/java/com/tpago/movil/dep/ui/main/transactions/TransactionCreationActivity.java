package com.tpago.movil.dep.ui.main.transactions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.tpago.movil.dep.ui.main.transactions.contacts.PhoneNumberTransactionCreationFragment;

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
  private static final String EXTRA_RECIPIENT = "recipient";

  private TransactionCreationComponent component;

  @Inject
  Recipient recipient;

  private Unbinder unbinder;

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @NonNull
  private static Intent serializeResult(@NonNull Recipient recipient) {
    final Intent intent = new Intent();
    intent.putExtra(EXTRA_RECIPIENT, recipient);
    return intent;
  }

  @NonNull
  private static Intent getLaunchIntent(@NonNull Context context) {
    return new Intent(context, TransactionCreationActivity.class);
  }

  @NonNull
  public static Intent getLaunchIntent(@NonNull Context context, @NonNull Recipient recipient) {
    final Intent intent = getLaunchIntent(context);
    intent.putExtra(EXTRA_RECIPIENT, recipient);
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

  /**
   * TODO
   *
   * @param intent
   *   TODO
   *
   * @return TODO
   */
  @Nullable
  public static Recipient deserializeResult(@Nullable Intent intent) {
    if (Utils.isNotNull(intent)) {
      return (Recipient) intent.getSerializableExtra(EXTRA_RECIPIENT);
    } else {
      return null;
    }
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Asserts all the required arguments.
    final Bundle bundle = Utils.isNotNull(savedInstanceState) ? savedInstanceState : getIntent()
      .getExtras();
    if (Utils.isNull(bundle) || !bundle.containsKey(EXTRA_RECIPIENT)) {
      throw new NullPointerException("Argument '" + EXTRA_RECIPIENT + "' must be provided");
    } else {
      final Recipient recipient = (Recipient) bundle.getSerializable(EXTRA_RECIPIENT);
      // Injects all the annotated dependencies.
      component = DaggerTransactionCreationComponent.builder()
        .appComponent(((App) getApplication()).getComponent())
        .transactionCreationModule(new TransactionCreationModule(recipient))
        .build();
      component.inject(this);
      // Sets the content layout identifier.
      setContentView(R.layout.activity_transaction_creation);
      // Binds all the annotated views and methods.
      unbinder = ButterKnife.bind(this);
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
        default:
          throw new UnsupportedOperationException("Transaction type '" + type + "' not supported");
      }
      setChildFragment(fragment, false, false);
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
        toolbar.setTitle(String.format(getString(R.string.transaction_creation_title), title));
        toolbar.setSubtitle(subtitle);
      }
    });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    // Unbinds all the annotated views and methods.
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
  public void finish(boolean succeeded) {
    if (succeeded) {
      setResult(RESULT_OK, serializeResult(recipient));
    } else {
      setResult(RESULT_CANCELED);
    }
    finish();
  }
}
