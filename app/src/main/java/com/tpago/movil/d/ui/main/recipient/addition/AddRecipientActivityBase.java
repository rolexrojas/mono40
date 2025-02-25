package com.tpago.movil.d.ui.main.recipient.addition;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.tpago.movil.app.ui.activity.base.ActivityModule;
import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.company.partner.Partner;
import com.tpago.movil.dep.App;
import com.tpago.movil.R;
import com.tpago.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.ui.DepActivityModule;
import com.tpago.movil.d.ui.SwitchableContainerActivityBase;
import com.tpago.movil.d.ui.main.recipient.index.category.Category;
import com.tpago.movil.d.ui.view.widget.FullScreenLoadIndicator;
import com.tpago.movil.d.ui.view.widget.LoadIndicator;
import com.tpago.movil.util.LogoutTimerService;
import com.tpago.movil.util.ObjectHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public class AddRecipientActivityBase extends
    SwitchableContainerActivityBase<AddRecipientComponent> implements AddRecipientContainer,
    AddRecipientScreen {

  private static final String KEY_CATEGORY = "category";

  private static final String EXTRA_RECIPIENT = "recipient";

  private static final int REQUEST_CODE = 0;

  private Unbinder unbinder;
  private AddRecipientComponent component;
  private LoadIndicator loadIndicator;

  @Inject
  DepApiBridge apiBridge;
  @Inject
  AddRecipientPresenter presenter;
  @Inject
  Category category;

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.containerFrameLayout)
  FrameLayout containerFrameLayout;

  private Recipient requestResult = null;

  @NonNull
  protected static Intent serializeResult(Recipient recipient) {
    final Intent intent = new Intent();
    if (ObjectHelper.isNotNull(recipient)) {
      intent.putExtra(EXTRA_RECIPIENT, recipient);
    }
    return intent;
  }

  @NonNull
  public static Intent getLaunchIntent(@NonNull Context context, Category category) {
    final Intent intent = new Intent(context, AddRecipientActivityBase.class);
    intent.putExtra(KEY_CATEGORY, category.name());
    return intent;
  }

  @Nullable
  public static Recipient deserializeResult(@Nullable Intent intent) {
    if (ObjectHelper.isNull(intent)) {
      return null;
    } else {
      return (Recipient) intent.getParcelableExtra(EXTRA_RECIPIENT);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE) {
      requestResult = NonAffiliatedPhoneNumberRecipientAdditionActivityBase.deserializeResult(data);
    }
  }

  @Override
  protected int layoutResId() {
    return R.layout.d_activity_add_recipient;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    unbinder = ButterKnife.bind(this);
    // Injects all the annotated dependencies.
    final String categoryName = this.getIntent()
        .getExtras()
        .getString(KEY_CATEGORY);
    final Category category = Category.valueOf(categoryName);
    component = DaggerAddRecipientComponent.builder()
        .appComponent(((App) getApplication()).component())
        .activityModule(ActivityModule.create(this))
        .depActivityModule(new DepActivityModule(this))
        .addRecipientModule(new AddRecipientModule(category))
        .build();
    component.inject(this);
    // Prepares the action bar.
    setSupportActionBar(toolbar);
    final ActionBar actionBar = getSupportActionBar();
    if (ObjectHelper.isNotNull(actionBar)) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowTitleEnabled(true);
      actionBar.setTitle(
          String.format(
              this.getString(R.string.format_add),
              this.getString(this.category.subjectStringId)
          )
      );
    }
    // Sets the initial screen.
    setChildFragment(SearchOrChooseRecipientFragment.newInstance(), false, false);
    // Attaches the presenter to the fragment.
    presenter.attachScreen(this);
  }

  @Override
  protected void onStart() {
    super.onStart();
    if (ObjectHelper.isNotNull(requestResult)) {
      finish(requestResult);
      requestResult = null;
    }
  }

  @Override
  protected void onStop() {
    this.presenter.onStop();
    super.onStop();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    // Detaches the presenter for the fragment.
    presenter.detachScreen();
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
            R.id.containerFrameLayout,
            RecipientBuilderFragment.create(
                getString(R.string.contract)
                    .toLowerCase(),
                partner
            )
        )
        .addToBackStack(null)
        .commit();
  }

  @Override
  public void onBankClicked(Bank bank) {
    if (bank.code() == 4) {
      getSupportFragmentManager().beginTransaction()
          .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
          .replace(
              R.id.containerFrameLayout,
              NonAffiliatedBanreservasPhoneNumberRecipientAdditionFragment.create(
                  getString(R.string.account)
                      .toLowerCase(),
                  bank
              )
          )
          .addToBackStack(null)
          .commit();

    } else {
      getSupportFragmentManager().beginTransaction()
          .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
          .replace(
              R.id.containerFrameLayout,
              RecipientBuilderFragment.create(
                  getString(R.string.account)
                      .toLowerCase(),
                  bank
              )
          )
          .addToBackStack(null)
          .commit();
    }
  }

  @Nullable
  public LoadIndicator getRefreshIndicator() {
    if (ObjectHelper.isNull(loadIndicator)) {
      loadIndicator = new FullScreenLoadIndicator(getSupportFragmentManager());
    }
    return loadIndicator;
  }

  @Override
  public void startNonAffiliatedProcess(NonAffiliatedPhoneNumberRecipient recipient) {
    startActivityForResult(
        NonAffiliatedPhoneNumberRecipientAdditionActivityBase.getLaunchIntent(this, recipient),
        REQUEST_CODE
    );
  }

  @Override
  public void finish(Recipient recipient) {
    setResult(
        ObjectHelper.isNotNull(recipient) ? RESULT_OK : RESULT_CANCELED,
        serializeResult(recipient)
    );
    finish();
  }
}
