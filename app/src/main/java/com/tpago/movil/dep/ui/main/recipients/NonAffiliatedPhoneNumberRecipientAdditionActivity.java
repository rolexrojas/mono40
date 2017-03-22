package com.tpago.movil.dep.ui.main.recipients;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tpago.movil.R;
import com.tpago.movil.dep.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.dep.domain.Recipient;
import com.tpago.movil.dep.ui.BaseActivity;
import com.tpago.movil.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public class NonAffiliatedPhoneNumberRecipientAdditionActivity extends BaseActivity {
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
    String phoneNumber) {
    return getLaunchIntent(context, new NonAffiliatedPhoneNumberRecipient(phoneNumber));
  }

  public static Recipient deserializeResult(Intent data) {
    if (Objects.isNull(data)) {
      return null;
    } else {
      return (Recipient) data.getSerializableExtra(KEY_RECIPIENT);
    }
  }

  @Override
  protected int layoutResourceIdentifier() {
    return R.layout.d_activity_non_affiliated_phone_number_recipient_addition;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    unbinder = ButterKnife.bind(this);
    setSupportActionBar(toolbar);
    final ActionBar actionBar = getSupportActionBar();
    if (Objects.isNotNull(actionBar)) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setTitle(R.string.add_recipient_title);
    }
    recipient = (NonAffiliatedPhoneNumberRecipient) getIntent().getSerializableExtra(KEY_RECIPIENT);
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
