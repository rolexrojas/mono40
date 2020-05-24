package com.mono40.movil.d.ui.main.recipient.addition;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.mono40.movil.PhoneNumber;
import com.mono40.movil.R;
import com.mono40.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.mono40.movil.d.domain.Recipient;
import com.mono40.movil.d.ui.DepActivityBase;
import com.mono40.movil.util.ObjectHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public class NonAffiliatedPhoneNumberRecipientAdditionActivityBase extends DepActivityBase {

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
    NonAffiliatedPhoneNumberRecipient recipient
  ) {
    final Intent i = new Intent(context, NonAffiliatedPhoneNumberRecipientAdditionActivityBase.class);
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
    if (ObjectHelper.isNull(data)) {
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
    if (ObjectHelper.isNotNull(actionBar)) {
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
