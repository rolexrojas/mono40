package com.tpago.movil.app.ui.main.settings;

import com.tpago.movil.ConfigManager;
import com.tpago.movil.UserStore;
import com.tpago.movil.app.FragmentScope;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.data.StringMapper;
import com.tpago.movil.util.ObjectHelper;

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
    UserStore userStore,
    ProductManager productManager,
    ConfigManager configManager,
    StringMapper stringMapper
  ) {
    return SettingsPresenter.builder()
      .userStore(userStore)
      .productManager(productManager)
      .configManager(configManager)
      .stringMapper(stringMapper)
      .presentation(this.presentation)
      .build();
  }
}
