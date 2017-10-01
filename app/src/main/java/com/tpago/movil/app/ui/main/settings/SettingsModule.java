package com.tpago.movil.app.ui.main.settings;

import com.tpago.movil.dep.ConfigManager;
import com.tpago.movil.dep.UserStore;
import com.tpago.movil.app.ui.FragmentScope;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.data.StringMapper;
import com.tpago.movil.domain.auth.alt.AltAuthManager;
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
    StringMapper stringMapper,
    AltAuthManager altAuthManager,
    UserStore userStore,
    ProductManager productManager,
    ConfigManager configManager
  ) {
    return SettingsPresenter.builder()
      .stringMapper(stringMapper)
      .altAuthManager(altAuthManager)
      .presentation(this.presentation)
      .userStore(userStore)
      .productManager(productManager)
      .configManager(configManager)
      .build();
  }
}
