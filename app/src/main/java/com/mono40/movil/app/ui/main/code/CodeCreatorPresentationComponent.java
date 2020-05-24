package com.mono40.movil.app.ui.main.code;

import com.mono40.movil.app.di.ComponentBuilder;
import com.mono40.movil.app.ui.fragment.FragmentScope;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@FragmentScope
@Subcomponent(modules = CodeCreatorPresentationModule.class)
public interface CodeCreatorPresentationComponent {

  void inject(CodeCreatorDialogFragment fragment);

  @Subcomponent.Builder
  interface Builder extends ComponentBuilder<CodeCreatorPresentationComponent> {

    Builder codeCreatorModule(CodeCreatorPresentationModule module);
  }
}
