package com.tpago.movil.app.ui.main.code;

import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.di.ContainerKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * @author hecvasro
 */
@Module(
  subcomponents = {
    CodeCreatorPresentationComponent.class
  }
)
public abstract class CodeCreatorPresentationComponentBuilderModule {

  @Binds
  @IntoMap
  @ContainerKey(CodeCreatorDialogFragment.class)
  public abstract ComponentBuilder codeCreatorComponentBuilder(
    CodeCreatorPresentationComponent.Builder builder
  );
}
