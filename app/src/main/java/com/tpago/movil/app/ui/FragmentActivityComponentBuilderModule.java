package com.tpago.movil.app.ui;

import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.di.ContainerKey;
import com.tpago.movil.app.ui.activity.ActivityQualifier;
import com.tpago.movil.app.ui.activity.ActivityScope;
import com.tpago.movil.app.ui.main.settings.auth.alt.AltAuthMethodComponent;
import com.tpago.movil.app.ui.main.settings.auth.alt.AltAuthMethodFragment;

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
