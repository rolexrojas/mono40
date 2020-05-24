package com.mono40.movil.app.ui.main;

import com.mono40.movil.app.di.ComponentBuilder;
import com.mono40.movil.app.ui.activity.base.ActivityModule;
import com.mono40.movil.app.ui.activity.ActivityScope;
import com.mono40.movil.app.ui.activity.fragment.ActivityModuleFragment;
import com.mono40.movil.app.ui.main.code.CodeCreatorModule;
import com.mono40.movil.app.ui.main.code.CodeCreatorPresentationComponentBuilderModule;
import com.mono40.movil.d.ui.DepActivityModule;
import com.mono40.movil.d.ui.main.DepMainComponent;
import com.mono40.movil.d.ui.main.DepMainModule;
import com.mono40.movil.dep.main.MainModule;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@Deprecated
@ActivityScope
@Subcomponent(modules = {
  MainComponentBuilderModule.class,
  ActivityModule.class,
  ActivityModuleFragment.class,
  MainModule.class,
  DepActivityModule.class,
  DepMainModule.class,
  CodeCreatorModule.class,
  CodeCreatorPresentationComponentBuilderModule.class,
})
public interface MainComponent extends DepMainComponent {

  @Subcomponent.Builder
  interface Builder extends ComponentBuilder<MainComponent> {

    Builder activityModule(ActivityModule module);

    Builder depActivityModule(DepActivityModule module);

    Builder fragmentActivityModule(ActivityModuleFragment module);

    Builder mainModule(MainModule module);

    Builder depMainModule(DepMainModule module);
  }
}
