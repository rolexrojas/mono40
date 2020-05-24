package com.mono40.movil.app.ui.picture;

import com.mono40.movil.app.di.ComponentBuilder;
import com.mono40.movil.app.ui.fragment.FragmentScopeChild;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@FragmentScopeChild
@Subcomponent
public interface PictureCreatorPresentationComponent {

  void inject(PictureCreatorDialogFragment fragment);

  @Subcomponent.Builder
  interface Builder extends ComponentBuilder<PictureCreatorPresentationComponent> {
  }
}
