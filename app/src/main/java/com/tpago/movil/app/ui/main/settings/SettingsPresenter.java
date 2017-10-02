package com.tpago.movil.app.ui.main.settings;

import com.tpago.movil.R;
import com.tpago.movil.dep.ConfigManager;
import com.tpago.movil.dep.User;
import com.tpago.movil.dep.UserStore;
import com.tpago.movil.app.ui.Presenter;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.data.StringMapper;
import com.tpago.movil.domain.auth.alt.AltAuthMethodManager;
import com.tpago.movil.domain.auth.alt.AltAuthMethod;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
final class SettingsPresenter extends Presenter<SettingsPresentation> {

  static Builder builder() {
    return new Builder();
  }

  private final StringMapper stringMapper;
  private final AltAuthMethodManager altAuthMethodManager;

  private final UserStore userStore;
  private final ProductManager productManager;
  private final ConfigManager configManager;

  private SettingsPresenter(Builder builder) {
    super(builder.presentation);

    this.stringMapper = builder.stringMapper;
    this.altAuthMethodManager = builder.altAuthMethodManager;

    this.userStore = builder.userStore;
    this.productManager = builder.productManager;
    this.configManager = builder.configManager;
  }

  @Override
  public void onPresentationResumed() {
    super.onPresentationResumed();

    // Initializes the edit profile option.
    final User user = this.userStore.get();
    this.presentation.setProfileSettingsOptionSecondaryText(user.name());

    // Initializes the alternative authentication method option.
    final AltAuthMethod method = this.altAuthMethodManager.getActiveMethod();
    final int altAuthMethodStringId;
    if (ObjectHelper.isNull(method)) {
      altAuthMethodStringId = R.string.usePassword;
    } else {
      altAuthMethodStringId = method.stringId;
    }
    this.presentation.setAltAuthMethodOption(this.stringMapper.apply(altAuthMethodStringId));
  }

  static final class Builder {

    private StringMapper stringMapper;
    private AltAuthMethodManager altAuthMethodManager;
    private SettingsPresentation presentation;

    private UserStore userStore;
    private ProductManager productManager;
    private ConfigManager configManager;

    private Builder() {
    }

    final Builder stringMapper(StringMapper stringMapper) {
      this.stringMapper = ObjectHelper.checkNotNull(stringMapper, "stringMapper");
      return this;
    }

    final Builder altAuthManager(AltAuthMethodManager altAuthMethodManager) {
      this.altAuthMethodManager = ObjectHelper.checkNotNull(
        altAuthMethodManager,
        "altAuthMethodManager"
      );
      return this;
    }

    final Builder presentation(SettingsPresentation presentation) {
      this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
      return this;
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

    final SettingsPresenter build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("stringMapper", ObjectHelper.isNull(this.stringMapper))
        .addPropertyNameIfMissing(
          "altAuthMethodManager",
          ObjectHelper.isNull(this.altAuthMethodManager)
        )
        .addPropertyNameIfMissing("presentation", ObjectHelper.isNull(this.presentation))
        .addPropertyNameIfMissing("userStore", ObjectHelper.isNull(this.userStore))
        .addPropertyNameIfMissing("productManager", ObjectHelper.isNull(this.productManager))
        .addPropertyNameIfMissing("configManager", ObjectHelper.isNull(this.configManager))
        .checkNoMissingProperties();

      return new SettingsPresenter(this);
    }
  }
}
