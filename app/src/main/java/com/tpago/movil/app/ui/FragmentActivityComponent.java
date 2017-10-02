package com.tpago.movil.app.ui;

import com.tpago.movil.app.di.ComponentBuilder;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@ActivityScope
@Subcomponent(
  modules = {
    FragmentActivityComponentBuilderModule.class,
    ActivityModule.class,
    ToolbarActivityModule.class,
    FragmentActivityModule.class
  }
)
public interface FragmentActivityComponent {

  void inject(FragmentActivity activity);

  @Subcomponent.Builder
  interface Builder extends ComponentBuilder<FragmentActivityComponent> {

    Builder activityModule(ActivityModule module);

    Builder toolbarActivityModule(ToolbarActivityModule module);

    Builder fragmentActivityModule(FragmentActivityModule module);
  }
}
