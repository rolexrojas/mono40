package com.tpago.movil.app.ui.main.settings;

import com.tpago.movil.R;
import com.tpago.movil.dep.ConfigManager;
import com.tpago.movil.dep.User;
import com.tpago.movil.app.ui.Presenter;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.data.StringMapper;
import com.tpago.movil.session.SessionManager;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
final class SettingsPresenter extends Presenter<SettingsPresentation> {

  static Builder builder() {
    return new Builder();
  }

  private final SessionManager sessionManager;
  private final StringMapper stringMapper;

  private final User user;
  private final ProductManager productManager;
  private final ConfigManager configManager;

  private SettingsPresenter(Builder builder) {
    super(builder.presentation);

    this.sessionManager = builder.sessionManager;
    this.stringMapper = builder.stringMapper;

    this.user = builder.user;
    this.productManager = builder.productManager;
    this.configManager = builder.configManager;
  }

  @Override
  public void onPresentationResumed() {
    super.onPresentationResumed();

    // Initializes the edit profile option.
    this.presentation.setProfileSettingsOptionSecondaryText(user.name());

    // Initializes the alternative authentication method option.
    final int sessionOpeningMethodStringId;
    if (this.sessionManager.isSessionOpeningMethodEnabled()) {
      sessionOpeningMethodStringId = this.sessionManager.getSessionOpeningMethod()
        .stringId;
    } else {
      sessionOpeningMethodStringId = R.string.noneUsePassword;
    }
    this.presentation.setAltAuthMethodOption(this.stringMapper.apply(sessionOpeningMethodStringId));
  }

  static final class Builder {

    private SessionManager sessionManager;
    private StringMapper stringMapper;

    private SettingsPresentation presentation;

    private User user;
    private ProductManager productManager;
    private ConfigManager configManager;

    private Builder() {
    }

    final Builder sessionManager(SessionManager sessionManager) {
      this.sessionManager = ObjectHelper.checkNotNull(sessionManager, "sessionManager");
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

    final Builder user(User user) {
      this.user = ObjectHelper.checkNotNull(user, "userStore");
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
        .addPropertyNameIfMissing("sessionManager", ObjectHelper.isNull(this.sessionManager))
        .addPropertyNameIfMissing("stringMapper", ObjectHelper.isNull(this.stringMapper))
        .addPropertyNameIfMissing("presentation", ObjectHelper.isNull(this.presentation))
        .addPropertyNameIfMissing("user", ObjectHelper.isNull(this.user))
        .addPropertyNameIfMissing("productManager", ObjectHelper.isNull(this.productManager))
        .addPropertyNameIfMissing("configManager", ObjectHelper.isNull(this.configManager))
        .checkNoMissingProperties();

      return new SettingsPresenter(this);
    }
  }
}
