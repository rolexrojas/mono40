package com.gbh.movil.ui.main.payments.transactions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.gbh.movil.App;
import com.gbh.movil.R;
import com.gbh.movil.Utils;
import com.gbh.movil.domain.PhoneNumber;
import com.gbh.movil.domain.Recipient;
import com.gbh.movil.ui.ContainerActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * TODO
 *
 * @author hecvasro
 */
public class TransactionCreationActivity extends ContainerActivity<TransactionCreationComponent>
  implements TransactionCreationContainer {
  /**
   * TODO
   */
  private static final String ARGUMENT_PHONE_NUMBER = "phoneNumber";
  /**
   * TODO
   */
  private static final String ARGUMENT_RECIPIENT = "recipient";

  private TransactionCreationComponent component;

  private Unbinder unbinder;

  @BindView(R.id.container_app_bar_toolbar)
  Toolbar toolbar;

  /**
   * TODO
   *
   * @param context
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  private static Intent getLaunchIntent(@NonNull Context context) {
    return new Intent(context, TransactionCreationActivity.class);
  }

  /**
   * TODO
   *
   * @param context
   *   TODO
   * @param phoneNumber
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  public static Intent getLaunchIntent(@NonNull Context context, @NonNull PhoneNumber phoneNumber) {
    final Intent intent = getLaunchIntent(context);
    intent.putExtra(ARGUMENT_PHONE_NUMBER, phoneNumber);
    return intent;
  }

  /**
   * TODO
   *
   * @param context
   *   TODO
   * @param recipient
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  public static Intent getLaunchIntent(@NonNull Context context, @NonNull Recipient recipient) {
    final Intent intent = getLaunchIntent(context);
    intent.putExtra(ARGUMENT_RECIPIENT, recipient);
    return intent;
  }

  @Override
  protected int getContainerId() {
    return R.id.container_content;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final Bundle bundle = Utils.isNotNull(savedInstanceState) ? savedInstanceState : getIntent()
      .getExtras();
    // Asserts all the required arguments.
    if (Utils.isNull(bundle) || !bundle.containsKey(ARGUMENT_RECIPIENT)) {
      throw new NullPointerException("Argument '" + ARGUMENT_RECIPIENT + "' is missing");
    } else {
      // Sets the content layout identifier.
      setContentView(R.layout.activity_transaction_creation);
      // Injects all the annotated dependencies.
      component = DaggerTransactionCreationComponent.builder()
        .appComponent(((App) getApplication()).getComponent())
        .build();
      component.inject(this);
      // Binds all the annotated views and methods.
      unbinder = ButterKnife.bind(this);
      // Prepares the app bar.
      setSupportActionBar(toolbar);
      final ActionBar actionBar = getSupportActionBar();
      if (Utils.isNotNull(actionBar)) {
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
      }
    }
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
  public void setTitle(@Nullable String title) {
    final ActionBar actionBar = getSupportActionBar();
    if (Utils.isNotNull(actionBar)) {
      actionBar.setTitle(title);
    }
  }

  @Override
  public void setSubTitle(@Nullable String subtitle) {
    final ActionBar actionBar = getSupportActionBar();
    if (Utils.isNotNull(actionBar)) {
      actionBar.setSubtitle(subtitle);
    }
  }
}
