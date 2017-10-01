package com.tpago.movil.app.ui;

import com.tpago.movil.app.ComponentBuilder;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@ActivityScope
@Subcomponent(
  modules = {
    BaseActivityModule.class,
    ToolbarActivityModule.class,
    FragmentActivityModule.class
  }
)
public interface FragmentActivityComponent {

  void inject(FragmentActivity activity);

  @Subcomponent.Builder
  interface Builder extends ComponentBuilder<FragmentActivityComponent> {

    Builder baseActivityModule(BaseActivityModule module);

    Builder toolbarActivityModule(ToolbarActivityModule module);

    Builder fragmentActivityModule(FragmentActivityModule module);
  }
}
