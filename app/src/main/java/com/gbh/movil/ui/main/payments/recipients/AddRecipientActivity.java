package com.gbh.movil.ui.main.payments.recipients;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.gbh.movil.R;
import com.gbh.movil.Utils;
import com.gbh.movil.data.StringHelper;
import com.gbh.movil.domain.Contact;
import com.gbh.movil.domain.PhoneNumber;
import com.gbh.movil.ui.BaseActivity;
import com.gbh.movil.ui.view.widget.RefreshIndicator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * TODO
 *
 * @author hecvasro
 */
public class AddRecipientActivity extends BaseActivity implements AddRecipientScreen {
  /**
   * TODO
   */
  private static final String EXTRA_CONTACT = "contact";

  private Unbinder unbinder;

  @Inject
  StringHelper stringHelper;

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.container)
  FrameLayout containerFrameLayout;

  /**
   * TODO
   *
   * @param context
   *   {@link android.app.Activity Caller}'s context.
   *
   * @return TODO
   */
  @NonNull
  public static Intent getLaunchIntent(@NonNull Context context) {
    return new Intent(context, AddRecipientActivity.class);
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
    intent.putExtra(EXTRA_CONTACT, new Contact(phoneNumber));
    return intent;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Sets the content layout identifier.
    setContentView(R.layout.activity_add_recipient);
    // Injects all the dependencies.
    // Binds all the annotated views and methods.
    unbinder = ButterKnife.bind(this);
    // Prepares the action bar.
    setSupportActionBar(toolbar);
    final ActionBar actionBar = getSupportActionBar();
    if (Utils.isNotNull(actionBar)) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowTitleEnabled(true);
      actionBar.setTitle(R.string.add_recipient_title);
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
  public RefreshIndicator getRefreshIndicator() {
    return null;
  }
}
