package com.tpago.movil.app.ui.main.transaction.insurance.micro.purchase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.tpago.movil.R;
import com.tpago.movil.app.App;
import com.tpago.movil.app.ui.activity.ActivityQualifier;
import com.tpago.movil.app.ui.activity.base.ActivityModule;
import com.tpago.movil.app.ui.activity.toolbar.ActivityToolbarBase;
import com.tpago.movil.app.ui.fragment.FragmentReplacer;
import com.tpago.movil.app.ui.main.transaction.insurance.micro.purchase.plan.MicroInsurancePurchasePlanFragment;
import com.tpago.movil.util.ObjectHelper;

import javax.inject.Inject;

public final class MicroInsurancePurchaseActivity extends ActivityToolbarBase {

  private static final String KEY_PURCHASE = "PURCHASE";

  public static MicroInsurancePurchaseActivity get(Context context) {
    ObjectHelper.checkNotNull(context, "context");
    if (!(context instanceof MicroInsurancePurchaseActivity)) {
      throw new IllegalArgumentException("!(activity instanceof MicroInsurancePurchaseActivity)");
    }
    return (MicroInsurancePurchaseActivity) context;
  }

  public static Intent createLaunchIntent(Context context, MicroInsurancePurchase purchase) {
    final Intent intent = new Intent(context, MicroInsurancePurchaseActivity.class);
    intent.putExtra(KEY_PURCHASE, purchase);
    return intent;
  }

  private MicroInsurancePurchaseComponent component;

  @Inject
  @ActivityQualifier
  FragmentReplacer fragmentReplacer;

  public final MicroInsurancePurchaseComponent component() {
    return this.component;
  }

  @Override
  protected int layoutResId() {
    return R.layout.activity_toolbar;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Retrieves extras.
    final MicroInsurancePurchase purchase = this.getIntent()
      .getParcelableExtra(KEY_PURCHASE);

    // Instantiates the dependency injector.
    this.component = App.get(this.getApplicationContext())
      .componentBuilderSupplier()
      .get(MicroInsurancePurchaseActivity.class, MicroInsurancePurchaseComponent.Builder.class)
      .activityModule(ActivityModule.create(this))
      .microInsurancePurchaseModule(MicroInsurancePurchaseModule.create(purchase))
      .build();

    // Injects all annotated dependencies.
    this.component.inject(this);

    // Shows initial fragment.
    this.fragmentReplacer.begin(MicroInsurancePurchasePlanFragment.create())
      .commit();
  }
}
