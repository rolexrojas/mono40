package com.tpago.movil.app.ui.main.settings.index;

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
public final class SettingsIndexModule {

  static SettingsIndexModule create(SettingsIndexPresentation presentation) {
    return new SettingsIndexModule(presentation);
  }

  private final SettingsIndexPresentation presentation;

  private SettingsIndexModule(SettingsIndexPresentation presentation) {
    this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
  }

  @Provides
  @FragmentScope
  SettingsIndexPresenter presenter(
    UserStore userStore,
    ProductManager productManager,
    ConfigManager configManager,
    StringMapper stringMapper
  ) {
    return SettingsIndexPresenter.builder()
      .userStore(userStore)
      .productManager(productManager)
      .configManager(configManager)
      .stringMapper(stringMapper)
      .presentation(this.presentation)
      .build();
  }
}
