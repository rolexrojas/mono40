package com.tpago.movil.app.ui.main.settings;

import com.tpago.movil.d.domain.Banks;
import com.tpago.movil.dep.ConfigManager;
import com.tpago.movil.dep.User;
import com.tpago.movil.dep.UserStore;
import com.tpago.movil.app.ui.Presenter;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.ProductType;
import com.tpago.movil.data.StringMapper;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

import java.util.Arrays;

/**
 * @author hecvasro
 */
final class SettingsPresenter extends Presenter<SettingsPresentation> {

  static Builder builder() {
    return new Builder();
  }

  private final UserStore userStore;
  private final ProductManager productManager;
  private final ConfigManager configManager;
  private final StringMapper stringMapper;

  private SettingsPresenter(Builder builder) {
    super(builder.presentation);

    this.userStore = builder.userStore;
    this.productManager = builder.productManager;
    this.configManager = builder.configManager;
    this.stringMapper = builder.stringMapper;
  }

  @Override
  public void onPresentationResumed() {
    super.onPresentationResumed();

    final User user = this.userStore.get();
    this.presentation.setProfileSettingsOptionSecondaryText(user.name());

    final Product primaryPaymentMethod = this.productManager.getDefaultPaymentOption();
    if (ObjectHelper.isNotNull(primaryPaymentMethod)) {
      this.presentation.setPrimaryPaymentMethodSettingsOptionSecondaryText(
        StringHelper.join(
          " ",
          Arrays.asList(
            this.stringMapper.apply(ProductType.findStringId(primaryPaymentMethod)),
            Banks.getName(primaryPaymentMethod.getBank()),
            primaryPaymentMethod.getAlias()
          )
        )
      );
    }

    this.presentation.setUnlockMethodSettingsOptionSecondaryText(null);

    this.presentation.setTimeoutSettingsOptionSecondaryText(null);

    this.presentation.setLockOnExitSettingsOptionChecked(true);
  }

  static final class Builder {

    private UserStore userStore;
    private ProductManager productManager;
    private ConfigManager configManager;
    private StringMapper stringMapper;
    private SettingsPresentation presentation;

    private Builder() {
    }

    final Builder userStore(UserStore userStore) {
      this.userStore = ObjectHelper.checkNotNull(userStore, "userStore");
      return this;
    }

    final Builder productManager(ProductManager productManager) {
      this.productManager = ObjectHelper.checkNotNull(productManager, "productManager");
      return this;
    }

    final Builder configManager(ConfigManager configManager) {
      this.configManager = ObjectHelper.checkNotNull(configManager, "configManager");
      return this;
    }

    final Builder stringMapper(StringMapper stringMapper) {
      this.stringMapper = ObjectHelper.checkNotNull(stringMapper, "stringMapper");
      return this;
    }

    final Builder presentation(SettingsPresentation presentation) {
      this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
      return this;
    }

    final SettingsPresenter build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("userStore", ObjectHelper.isNull(this.userStore))
        .addPropertyNameIfMissing("productManager", ObjectHelper.isNull(this.productManager))
        .addPropertyNameIfMissing("configManager", ObjectHelper.isNull(this.configManager))
        .addPropertyNameIfMissing("stringMapper", ObjectHelper.isNull(this.stringMapper))
        .addPropertyNameIfMissing("presentation", ObjectHelper.isNull(this.presentation))
        .checkNoMissingProperties();

      return new SettingsPresenter(this);
    }
  }
}
