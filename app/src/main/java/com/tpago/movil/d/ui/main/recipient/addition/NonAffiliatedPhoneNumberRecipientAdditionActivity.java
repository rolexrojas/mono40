package com.tpago.movil.d.ui.main.recipient.addition;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tpago.movil.domain.PhoneNumber;
import com.tpago.movil.R;
import com.tpago.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.ui.DepBaseActivity;
import com.tpago.movil.dep.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public class NonAffiliatedPhoneNumberRecipientAdditionActivity extends DepBaseActivity {
  private static final String KEY_RECIPIENT = "recipient";

  private Unbinder unbinder;

  protected NonAffiliatedPhoneNumberRecipient recipient;

  @BindView(R.id.toolbar) Toolbar toolbar;

  protected static Intent serializeResult(Recipient recipient) {
    final Intent intent = new Intent();
    intent.putExtra(KEY_RECIPIENT, recipient);
    return intent;
  }

  public static Intent getLaunchIntent(
    Context context,
    NonAffiliatedPhoneNumberRecipient recipient) {
    final Intent i = new Intent(context, NonAffiliatedPhoneNumberRecipientAdditionActivity.class);
    i.putExtra(KEY_RECIPIENT, recipient);
    return i;
  }

  public static Intent getLaunchIntent(
    Context context,
    PhoneNumber phoneNumber
  ) {
    return getLaunchIntent(context, new NonAffiliatedPhoneNumberRecipient(phoneNumber));
  }

  public static Recipient deserializeResult(Intent data) {
    if (Objects.checkIfNull(data)) {
      return null;
    } else {
      return data.getParcelableExtra(KEY_RECIPIENT);
    }
  }

  @Override
  protected int layoutResId() {
    return R.layout.d_activity_non_affiliated_phone_number_recipient_addition;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    unbinder = ButterKnife.bind(this);
    setSupportActionBar(toolbar);
    final ActionBar actionBar = getSupportActionBar();
    if (Objects.checkIfNotNull(actionBar)) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setTitle(R.string.add_recipient_title);
    }
    recipient = getIntent().getParcelableExtra(KEY_RECIPIENT);
    getSupportFragmentManager().beginTransaction()
      .replace(R.id.container, new NonAffiliatedPhoneNumberRecipientAddition1Fragment())
      .commit();
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

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unbinder.unbind();
  }
}
