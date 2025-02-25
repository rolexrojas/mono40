package com.tpago.movil.app.ui.picture;

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
    PictureCreatorPresentationComponent.class
  }
)
public abstract class PictureCreatorPresentationComponentBuilderModule {

  @Binds
  @IntoMap
  @ContainerKey(PictureCreatorDialogFragment.class)
  public abstract ComponentBuilder pictureCreatorPresentationComponentBuilder(
    PictureCreatorPresentationComponent.Builder builder
  );
}
