package com.gbh.movil.ui.main.recipients;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.gbh.movil.App;
import com.gbh.movil.R;
import com.gbh.movil.misc.Utils;
import com.gbh.movil.domain.Recipient;
import com.gbh.movil.ui.ActivityModule;
import com.gbh.movil.ui.SwitchableContainerActivity;
import com.gbh.movil.ui.misc.UiUtils;
import com.gbh.movil.ui.view.widget.FullScreenRefreshIndicator;
import com.gbh.movil.ui.view.widget.LoadIndicator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * TODO
 *
 * @author hecvasro
 */
public class AddRecipientActivity extends SwitchableContainerActivity<AddRecipientComponent>
  implements AddRecipientContainer, AddRecipientScreen {
  /**
   * TODO
   */
  private static final String EXTRA_RECIPIENT = "recipient";

  private AddRecipientComponent component;
  private Unbinder unbinder;
  private LoadIndicator loadIndicator;

  @Inject
  AddRecipientPresenter presenter;

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.container)
  FrameLayout containerFrameLayout;

  /**
   * TODO
   *
   * @param recipient
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  private static Intent serializeResult(@Nullable Recipient recipient) {
    final Intent intent = new Intent();
    if (Utils.isNotNull(recipient)) {
      intent.putExtra(EXTRA_RECIPIENT, recipient);
    }
    return intent;
  }

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
    // Sets the content layout identifier.
    setContentView(R.layout.activity_add_recipient);
    // Injects all the annotated dependencies.
    component = DaggerAddRecipientComponent.builder()
      .appComponent(((App) getApplication()).getComponent())
      .activityModule(new ActivityModule(this))
      .build();
    component.inject(this);
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
    // Sets the initial screen.
    setChildFragment(SearchOrChooseRecipientFragment.newInstance(), false, false);
    // Attaches the presenter to the fragment.
    presenter.attachScreen(this);
  }

  @Override
  protected void onStop() {
    super.onStop();
    // Stops the presenter.
    presenter.stop();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    // Detaches the presenter for the fragment.
    presenter.detachScreen();
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

  @NonNull
  @Override
  public AddRecipientComponent getComponent() {
    return component;
  }

  @Override
  public void onContactClicked(@NonNull Contact contact) {
    presenter.add(contact);
  }

  @Nullable
  public LoadIndicator getRefreshIndicator() {
    if (Utils.isNull(loadIndicator)) {
      loadIndicator = new FullScreenRefreshIndicator(getSupportFragmentManager());
    }
    return loadIndicator;
  }

  @Override
  public void finish(@Nullable Recipient recipient) {
    setResult(Utils.isNotNull(recipient) ? RESULT_OK : RESULT_CANCELED, serializeResult(recipient));
    finish();
  }

  @Override
  public void showNotSupportedOperationMessage() {
    UiUtils.createDialog(this, getString(R.string.sorry),
      getString(R.string.info_not_available_unaffiliated_contact_recipient_addition),
      getString(R.string.ok), null, null, null).show();
  }
}
