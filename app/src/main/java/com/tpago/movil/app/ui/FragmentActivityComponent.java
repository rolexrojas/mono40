package com.tpago.movil.app.ui;

import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.ui.main.code.CodeCreatorModule;
import com.tpago.movil.app.ui.main.code.CodeCreatorPresentationComponentBuilderModule;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@ActivityScope
@Subcomponent(
  modules = {
    ActivityModule.class,
    CodeCreatorModule.class,
    CodeCreatorPresentationComponentBuilderModule.class,
    FragmentActivityComponentBuilderModule.class,
    FragmentActivityModule.class,
    ToolbarActivityModule.class,
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
