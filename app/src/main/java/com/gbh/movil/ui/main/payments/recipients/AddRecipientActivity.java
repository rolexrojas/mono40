package com.gbh.movil.ui.main.payments.recipients;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.gbh.movil.App;
import com.gbh.movil.R;
import com.gbh.movil.Utils;
import com.gbh.movil.domain.PhoneNumber;
import com.gbh.movil.ui.ActivityModule;
import com.gbh.movil.ui.BaseActivity;
import com.gbh.movil.ui.Container;
import com.gbh.movil.ui.SubFragment;
import com.gbh.movil.ui.UiUtils;
import com.gbh.movil.ui.view.widget.FullScreenRefreshIndicator;
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
public class AddRecipientActivity extends BaseActivity implements AddRecipientContainer,
  AddRecipientScreen {
  /**
   * TODO
   */
  private static final String EXTRA_PHONE_NUMBER = "contact";

  private AddRecipientComponent component;
  private Unbinder unbinder;
  private RefreshIndicator refreshIndicator;

  @Inject
  AddRecipientPresenter presenter;

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
    intent.putExtra(EXTRA_PHONE_NUMBER, phoneNumber);
    return intent;
  }

  /**
   * TODO
   *
   * @param fragment
   *   TODO
   * @param addToBackStack
   *   TODO
   * @param animate
   *   TODO
   */
  private void replaceFragment(@NonNull Fragment fragment, boolean addToBackStack,
    boolean animate) {
    final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    if (animate) {
      transaction.setCustomAnimations(R.anim.fragment_transition_enter_sibling,
        R.anim.fragment_transition_exit_sibling, R.anim.fragment_transition_enter_sibling,
        R.anim.fragment_transition_exit_sibling);
    }
    transaction.replace(R.id.container, fragment);
    if (addToBackStack) {
      transaction.addToBackStack(null);
    }
    transaction.commit();
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
    //
    final Bundle bundle = Utils.isNotNull(savedInstanceState) ? savedInstanceState
      : getIntent().getExtras();
    if (Utils.isNotNull(bundle) && bundle.containsKey(EXTRA_PHONE_NUMBER)) {
      // TODO: Start the phone number validation process.
    } else {
      replaceFragment(SearchOrChooseRecipientFragment.newInstance(), false, false);
    }
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
  public void setSubScreen(
    @NonNull SubFragment<? extends Container<AddRecipientComponent>> fragment) {
    replaceFragment(fragment, true, true);
  }

  @Override
  public void onContactClicked(@NonNull Contact contact) {
    presenter.add(contact);
  }

  @Nullable
  @Override
  public RefreshIndicator getRefreshIndicator() {
    if (Utils.isNull(refreshIndicator)) {
      refreshIndicator = new FullScreenRefreshIndicator(getSupportFragmentManager());
    }
    return refreshIndicator;
  }

  @Override
  public void terminate() {
    finish();
  }

  @Override
  public void showNotSupportedOperationMessage() {
    UiUtils.createDialog(this, getString(R.string.sorry),
      getString(R.string.not_available_not_affiliated_recipient_addition), getString(R.string.ok),
      null, null, null).show();
  }
}
