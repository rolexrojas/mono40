package com.tpago.movil.dep.ui.main.recipients;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.tpago.movil.Partner;
import com.tpago.movil.app.App;
import com.tpago.movil.R;
import com.tpago.movil.dep.domain.api.DepApiBridge;
import com.tpago.movil.dep.domain.session.SessionManager;
import com.tpago.movil.dep.misc.Utils;
import com.tpago.movil.dep.domain.Recipient;
import com.tpago.movil.dep.ui.ActivityModule;
import com.tpago.movil.dep.ui.SwitchableContainerActivity;
import com.tpago.movil.dep.ui.misc.UiUtils;
import com.tpago.movil.dep.ui.view.widget.FullScreenLoadIndicator;
import com.tpago.movil.dep.ui.view.widget.LoadIndicator;
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
public class AddRecipientActivity
  extends SwitchableContainerActivity<AddRecipientComponent>
  implements AddRecipientContainer,
  AddRecipientScreen {
  private static final String EXTRA_RECIPIENT = "recipient";

  private AddRecipientComponent component;
  private Unbinder unbinder;
  private LoadIndicator loadIndicator;

  @Inject SessionManager sessionManager;
  @Inject DepApiBridge apiBridge;
  @Inject AddRecipientPresenter presenter;

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.container) FrameLayout containerFrameLayout;

  @NonNull
  protected static Intent serializeResult(Recipient recipient) {
    final Intent intent = new Intent();
    if (Utils.isNotNull(recipient)) {
      intent.putExtra(EXTRA_RECIPIENT, recipient);
    }
    return intent;
  }

  @NonNull
  public static Intent getLaunchIntent(@NonNull Context context) {
    return new Intent(context, AddRecipientActivity.class);
  }

  @Nullable
  public static Recipient deserializeResult(@Nullable Intent intent) {
    if (Objects.isNull(intent)) {
      return null;
    } else {
      return (Recipient) intent.getSerializableExtra(EXTRA_RECIPIENT);
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
  public void onContactClicked(Contact contact) {
    presenter.add(contact);
  }

  @Override
  public void onPartnerClicked(Partner partner) {
    getSupportFragmentManager().beginTransaction()
      .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
      .replace(
        R.id.container,
        RecipientBuilderFragment.create(
          getString(R.string.contract).toLowerCase(),
          new BillRecipientBuilder(sessionManager.getSession().getAuthToken(), apiBridge, partner)))
      .addToBackStack(null)
      .commit();
  }

  @Nullable
  public LoadIndicator getRefreshIndicator() {
    if (Utils.isNull(loadIndicator)) {
      loadIndicator = new FullScreenLoadIndicator(getSupportFragmentManager());
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
