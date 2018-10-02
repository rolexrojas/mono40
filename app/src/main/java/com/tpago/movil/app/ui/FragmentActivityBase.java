package com.tpago.movil.app.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.tpago.movil.R;
import com.tpago.movil.app.App;
import com.tpago.movil.app.di.ComponentBuilderSupplier;
import com.tpago.movil.app.di.ComponentBuilderSupplierContainer;
import com.tpago.movil.app.ui.activity.base.ActivityModule;
import com.tpago.movil.app.ui.activity.ActivityQualifier;
import com.tpago.movil.app.ui.activity.toolbar.ActivityToolbarBase;
import com.tpago.movil.app.ui.fragment.FragmentReplacer;
import com.tpago.movil.util.ObjectHelper;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author hecvasro
 */
@Deprecated
public final class FragmentActivityBase extends ActivityToolbarBase implements
  ComponentBuilderSupplierContainer {

  private static final String KEY_CREATOR = "creator";

  public static Intent createLaunchIntent(Context context, FragmentCreator creator) {
    ObjectHelper.checkNotNull(context, "context");
    ObjectHelper.checkNotNull(creator, "creator");

    final Intent intent = new Intent(context, FragmentActivityBase.class);
    intent.putExtra(KEY_CREATOR, creator);
    return intent;
  }

  public static FragmentActivityBase get(Activity activity) {
    ObjectHelper.checkNotNull(activity, "activity");
    if (!(activity instanceof FragmentActivityBase)) {
      throw new ClassCastException("!(activity instanceof FragmentActivityBase)");
    }
    return (FragmentActivityBase) activity;
  }

  private FragmentActivityComponent component;

  @Inject
  @ActivityQualifier
  ComponentBuilderSupplier componentBuilderSupplier;
  @Inject
  @ActivityQualifier
  FragmentReplacer fragmentReplacer;

  @BindView(R.id.linear_layout_change_password)
  LinearLayout changePasswordLinearLayout;
  @BindView(R.id.image_button_ok_change_password)
  ImageButton changePasswordOkButton;
  @BindView(R.id.image_button_cancel_change_password)
  ImageButton changePasswordCancelButton;

  @Override
  protected int layoutResId() {
    return R.layout.activity_fragment;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Initializes the dependency injector.
    this.component = App.get(this.getApplicationContext())
      .componentBuilderSupplier()
      .get(FragmentActivityBase.class, FragmentActivityComponent.Builder.class)
      .activityModule(ActivityModule.create(this))
      .build();

    // Injects all annotated dependencies.
    this.component.inject(this);

    // Extracts the creator from the arguments.
    final FragmentCreator creator = this.getIntent()
      .getExtras()
      .getParcelable(KEY_CREATOR);

    // Initializes the fragment container.
    this.fragmentReplacer.begin(creator.create())
      .commit();
  }

  public final FragmentActivityComponent component() {
    return this.component;
  }

  @Override
  public final ComponentBuilderSupplier componentBuilderSupplier() {
    return this.componentBuilderSupplier;
  }

  public void showChangePasswordLayout(){
    this.changePasswordLinearLayout.setVisibility(View.VISIBLE);
    this.toolbar.setVisibility(View.INVISIBLE);
    this.toolbar.setEnabled(false);
  }

  public void setChangePasswordOkButtonEnabled(boolean enabled) {
    changePasswordOkButton.setEnabled(enabled);
    changePasswordOkButton.setAlpha(enabled ? 1F : 0.5F);
  }

  public void setOnChangePasswordOkButtonClickListener(View.OnClickListener listener) {
    changePasswordOkButton.setOnClickListener(listener);
  }

  public void hideChangePasswordLinearLayout(){
    this.toolbar.setEnabled(true);
    this.toolbar.setVisibility(View.VISIBLE);
    changePasswordLinearLayout.setVisibility(View.GONE);
  }

  public void setOnChangePasswordCancelButtonClickListener(View.OnClickListener listener) {
    changePasswordCancelButton.setOnClickListener(listener);
  }
}
