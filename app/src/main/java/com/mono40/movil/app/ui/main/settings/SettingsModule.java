package com.mono40.movil.app.ui.main.settings;

import com.mono40.movil.dep.ConfigManager;
import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.d.domain.ProductManager;
import com.mono40.movil.app.StringMapper;
import com.mono40.movil.dep.User;
import com.mono40.movil.product.ProductHelper;
import com.mono40.movil.session.SessionManager;
import com.mono40.movil.util.ObjectHelper;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class SettingsModule {

  static SettingsModule create(SettingsPresentation presentation) {
    return new SettingsModule(presentation);
  }

  private final SettingsPresentation presentation;

  private SettingsModule(SettingsPresentation presentation) {
    this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
  }

  @Provides
  @FragmentScope
  SettingsPresenter presenter(
    StringMapper stringMapper,
    ProductHelper productHelper,
    SessionManager sessionManager,
    User user,
    ProductManager productManager,
    ConfigManager configManager
  ) {
    return SettingsPresenter.builder()
      .stringMapper(stringMapper)
      .productHelper(productHelper)
      .sessionManager(sessionManager)
      .user(user)
      .productManager(productManager)
      .configManager(configManager)
      .presentation(this.presentation)
      .build();
  }
}
