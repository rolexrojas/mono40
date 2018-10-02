package com.tpago.movil.dep.init;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tpago.movil.app.ui.activity.base.ActivityModule;
import com.tpago.movil.dep.ActivityBase;
import com.tpago.movil.app.ui.activity.ActivityQualifier;
import com.tpago.movil.dep.App;
import com.tpago.movil.R;
import com.tpago.movil.app.ui.fragment.FragmentReplacer;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.RootUtil;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author hecvasro
 */
@Deprecated
public final class InitActivityBase extends ActivityBase implements InitContainer {

  public static Intent getLaunchIntent(Context context) {
    return new Intent(context, InitActivityBase.class);
  }

  public static InitActivityBase get(Activity activity) {
    ObjectHelper.checkNotNull(activity, "activity");
    if (!(activity instanceof InitActivityBase)) {
      throw new ClassCastException("!(activity instanceof DepMainActivityBase)");
    }
    return (InitActivityBase) activity;
  }

  private InitComponent component;

  @Inject
  @ActivityQualifier
  FragmentReplacer fragmentReplacer;

  @BindView(android.R.id.content) View rootView;
  @BindView(R.id.view_placeholder) View placeholderView;
  @BindView(R.id.view_container) View screenContainerView;
  @BindView(R.id.logo) Logo logo;

  @Override
  protected int layoutResourceIdentifier() {
    return R.layout.activity_init;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Initializes the dependency injector.
    this.component = App.get(this)
      .component()
      .plus(
        ActivityModule.create(this),
        new com.tpago.movil.dep.ActivityModule(this),
        new InitModule()
      );
    // Injects all the annotated dependencies.
    this.component.inject(this);
    // Initializes the application.
    this.fragmentReplacer.begin(InitFragment.create())
      .commit();
  }

  @Override
  protected void onResume() {
    super.onResume();

    if(RootUtil.isDeviceRooted()) {
      RootUtil.showRootErrorDialog(this, this);
    }
  }

  @Override
  public InitComponent getInitComponent() {
    return component;
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
  }
}
