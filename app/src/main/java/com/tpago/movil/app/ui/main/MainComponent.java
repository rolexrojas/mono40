package com.tpago.movil.app.ui.main;

import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.ui.ActivityModule;
import com.tpago.movil.app.ui.ActivityScope;
import com.tpago.movil.app.ui.FragmentActivityModule;
import com.tpago.movil.app.ui.main.code.CodeCreatorModule;
import com.tpago.movil.app.ui.main.code.CodeCreatorPresentationComponentBuilderModule;
import com.tpago.movil.d.ui.DepActivityModule;
import com.tpago.movil.d.ui.main.DepMainComponent;
import com.tpago.movil.d.ui.main.DepMainModule;
import com.tpago.movil.dep.main.MainModule;

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
    FragmentActivityModule.class,
    MainComponentBuilderModule.class,
    DepActivityModule.class,
    DepMainModule.class,
    MainModule.class,
  }
)
public interface MainComponent extends DepMainComponent {

  @Subcomponent.Builder
  interface Builder extends ComponentBuilder<MainComponent> {

    Builder activityModule(ActivityModule module);

    Builder depActivityModule(DepActivityModule module);

    Builder fragmentActivityModule(FragmentActivityModule module);

    Builder mainModule(MainModule module);

    Builder depMainModule(DepMainModule module);
  }
}
