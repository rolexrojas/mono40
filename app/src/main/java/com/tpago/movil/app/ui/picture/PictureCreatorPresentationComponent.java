package com.tpago.movil.app.ui.picture;

import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.ui.ChildFragmentScope;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@ChildFragmentScope
@Subcomponent
public interface PictureCreatorPresentationComponent {

  void inject(PictureCreatorDialogFragment fragment);

  @Subcomponent.Builder
  interface Builder extends ComponentBuilder<PictureCreatorPresentationComponent> {
  }
}
