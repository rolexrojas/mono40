package com.mono40.movil.app.ui;

import com.mono40.movil.app.di.ComponentBuilder;
import com.mono40.movil.app.ui.activity.base.ActivityModule;
import com.mono40.movil.app.ui.activity.fragment.ActivityModuleFragment;
import com.mono40.movil.app.ui.activity.ActivityScope;
import com.mono40.movil.app.ui.activity.toolbar.ActivityModuleToolbar;
import com.mono40.movil.app.ui.init.unlock.ChangePassword.ChangePasswordPresenter;
import com.mono40.movil.app.ui.main.code.CodeCreatorModule;
import com.mono40.movil.app.ui.main.code.CodeCreatorPresentationComponentBuilderModule;
import com.mono40.movil.app.ui.main.settings.primaryPaymentMethod.PrimaryPaymentMethodFragment;
import com.mono40.movil.app.ui.main.settings.primaryPaymentMethod.PrimaryPaymentMethodPresenter;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@Deprecated
@ActivityScope
@Subcomponent(modules = {
  ActivityModule.class,
  CodeCreatorModule.class,
  CodeCreatorPresentationComponentBuilderModule.class,
  FragmentActivityComponentBuilderModule.class,
  ActivityModuleFragment.class,
  ActivityModuleToolbar.class
})
public interface FragmentActivityComponent {

  void inject(FragmentActivityBase activity);

  void inject(PrimaryPaymentMethodFragment fragment);

  void inject(PrimaryPaymentMethodPresenter presenter);

  void inject(ChangePasswordPresenter presenter);

  @Subcomponent.Builder
  interface Builder extends ComponentBuilder<FragmentActivityComponent> {

    Builder activityModule(ActivityModule module);

    Builder toolbarActivityModule(ActivityModuleToolbar module);

    Builder fragmentActivityModule(ActivityModuleFragment module);
  }
}
