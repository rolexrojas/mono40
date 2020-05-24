package com.mono40.movil.app.ui;

import com.mono40.movil.app.di.ComponentBuilder;
import com.mono40.movil.app.di.ContainerKey;
import com.mono40.movil.app.ui.activity.ActivityQualifier;
import com.mono40.movil.app.ui.activity.ActivityScope;
import com.mono40.movil.app.ui.main.settings.auth.alt.AltAuthMethodComponent;
import com.mono40.movil.app.ui.main.settings.auth.alt.AltAuthMethodFragment;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * @author hecvasro
 */
@Deprecated
@Module(subcomponents = {
  AltAuthMethodComponent.class
})
public abstract class FragmentActivityComponentBuilderModule {

  @Binds
  @IntoMap
  @ContainerKey(AltAuthMethodFragment.class)
  @ActivityScope
  @ActivityQualifier
  public abstract ComponentBuilder altAuthMethodComponentBuilder(
    AltAuthMethodComponent.Builder builder
  );
}
