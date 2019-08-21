package com.tpago.movil.app.ui.activity.toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.tpago.movil.R;
import com.tpago.movil.app.App;
import com.tpago.movil.app.di.ComponentBuilderSupplier;
import com.tpago.movil.app.di.ComponentBuilderSupplierContainer;
import com.tpago.movil.app.ui.activity.ActivityQualifier;
import com.tpago.movil.app.ui.activity.base.ActivityModule;
import com.tpago.movil.app.ui.fragment.FragmentHelper;
import com.tpago.movil.app.ui.fragment.FragmentReplacer;
import com.tpago.movil.app.ui.fragment.base.FragmentBase;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;

import javax.inject.Inject;

/**
 * @author hecvasro
 */
public final class ActivityToolbar extends ActivityToolbarBase
  implements ComponentBuilderSupplierContainer {

  private static final String KEY_ARGUMENT = "argument";

  private static final String TAG_FRAGMENT_RETAINED = "fragmentRetained";

  public static IntentBuilder intentBuilder() {
    return new IntentBuilder();
  }

  public static ActivityToolbar get(Context context) {
    ObjectHelper.checkNotNull(context, "context");
    if (!(context instanceof ActivityToolbar)) {
      throw new IllegalArgumentException("!(activity instanceof ActivityToolbar)");
    }
    return (ActivityToolbar) context;
  }

  private ActivityComponentToolbar component;

  @Inject
  @ActivityQualifier
  protected FragmentReplacer fragmentReplacer;

  @Inject
  @ActivityQualifier
  protected ComponentBuilderSupplier componentBuilderSupplier;

  private FragmentToolbarRetained retainedFragment;

  public final FragmentToolbarRetained retainedFragment() {
    return this.retainedFragment;
  }

  @LayoutRes
  @Override
  protected int layoutResId() {
    return R.layout.activity_toolbar;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Instantiates the dependency injector.
    this.component = App.get(this.getApplicationContext())
      .componentBuilderSupplier()
      .get(ActivityToolbar.class, ActivityComponentToolbar.Builder.class)
      .activityModule(ActivityModule.create(this))
      .build();

    // Injects all annotated dependencies.
    this.component.inject(this);

    // Initializes the argument of the fragment.
    final Intent intent = ObjectHelper.checkNotNull(this.getIntent(), "intent");
    final Bundle extras = ObjectHelper.checkNotNull(intent.getExtras(), "extras");
    if (!extras.containsKey(KEY_ARGUMENT)) {
      throw new IllegalArgumentException("!extras.containsKey(KEY_ARGUMENT)");
    }
    final Argument argument = ObjectHelper
      .checkNotNull(extras.getParcelable(KEY_ARGUMENT), "argument");

    // Initializes the fragment that will be retained.
    this.retainedFragment = (FragmentToolbarRetained) FragmentHelper
      .findByTag(this.fragmentReplacer.manager(), TAG_FRAGMENT_RETAINED);
    if (ObjectHelper.isNull(this.retainedFragment)) {
      this.retainedFragment = argument.createFragmentRetained();
      this.fragmentReplacer.manager()
        .beginTransaction()
        .add(this.retainedFragment, TAG_FRAGMENT_RETAINED)
        .commit();
    }

    // Initializes the fragment that will be shown.
    this.fragmentReplacer.begin(argument.createFragment())
      .commit();
  }

  @Override
  public ComponentBuilderSupplier componentBuilderSupplier() {
    return this.componentBuilderSupplier;
  }

  public static abstract class Argument implements Parcelable {

    protected Argument() {
    }

    public abstract FragmentToolbarRetained createFragmentRetained();

    public abstract FragmentBase createFragment();
  }

  public static final class IntentBuilder {

    private Context context;
    private Argument argument;

    private IntentBuilder() {
    }

    public final IntentBuilder context(Context context) {
      this.context = ObjectHelper.checkNotNull(context, "context");
      return this;
    }

    public final IntentBuilder argument(Argument argument) {
      this.argument = ObjectHelper.checkNotNull(argument, "argument");
      return this;
    }

    public final Intent build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("context", ObjectHelper.isNull(this.context))
        .addPropertyNameIfMissing("argument", ObjectHelper.isNull(this.argument))
        .checkNoMissingProperties();
      final Intent intent = new Intent(this.context, ActivityToolbar.class);
      intent.putExtra(KEY_ARGUMENT, this.argument);
      return intent;
    }
  }
}
