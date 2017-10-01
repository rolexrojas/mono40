package com.tpago.movil.app.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tpago.movil.R;
import com.tpago.movil.app.App;
import com.tpago.movil.app.ui.main.FragmentCreator;
import com.tpago.movil.util.ObjectHelper;

import javax.inject.Inject;

/**
 * @author hecvasro
 */
public final class FragmentActivity extends BaseToolbarActivity {

  private static final String KEY_CREATOR = "creator";

  public static Intent getLaunchIntent(Context context, FragmentCreator creator) {
    ObjectHelper.checkNotNull(context, "context");
    ObjectHelper.checkNotNull(creator, "creator");

    final Intent intent = new Intent(context, FragmentActivity.class);
    intent.putExtra(KEY_CREATOR, creator);
    return intent;
  }

  private FragmentActivityComponent component;

  @Inject @ActivityQualifier FragmentReplacer fragmentReplacer;

  @Override
  protected int layoutResId() {
    return R.layout.activity_fragment;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Initializes the dependency injector.
    this.component = App.get(this)
      .componentBuilderSupplier()
      .get(FragmentActivity.class, FragmentActivityComponent.Builder.class)
      .fragmentActivityModule(
        FragmentActivityModule.create(
          this.getSupportFragmentManager(),
          R.id.containerFrameLayout
        )
      )
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
}
