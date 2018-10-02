package com.tpago.movil.app.ui.main.settings;

import com.tpago.movil.R;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.dep.ConfigManager;
import com.tpago.movil.dep.User;
import com.tpago.movil.app.ui.Presenter;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.product.ProductHelper;
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

  private final StringMapper stringMapper;
  private final ProductHelper productHelper;

  private final SessionManager sessionManager;
  private final User user;
  private final ProductManager productManager;
  private final ConfigManager configManager;

  private SettingsPresenter(Builder builder) {
    super(builder.presentation);

    this.stringMapper = builder.stringMapper;
    this.productHelper = builder.productHelper;

    this.sessionManager = builder.sessionManager;
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
    if (this.sessionManager.isUnlockMethodEnabled()) {
      sessionOpeningMethodStringId = this.sessionManager.getUnlockMethod()
        .stringId;
    } else {
      sessionOpeningMethodStringId = R.string.noneUsePassword;
    }
    this.presentation.setAltAuthMethodOption(this.stringMapper.apply(sessionOpeningMethodStringId));

    // Initializes the primary payment method option.
    final Product product = this.productManager.getDefaultPaymentOption();
    final String primaryPaymentMethod;
    if (ObjectHelper.isNull(product)) {
      primaryPaymentMethod = this.stringMapper.apply(R.string.none);
    } else {
      primaryPaymentMethod
        = this.productHelper.getBankNameAndTypeNameAndNumber(product.toProduct());
    }
    this.presentation.setPrimaryPaymentMethodSettingsOptionSecondaryText(primaryPaymentMethod);
  }

  static final class Builder {

    private StringMapper stringMapper;
    private ProductHelper productHelper;

    private SessionManager sessionManager;
    private User user;
    private ProductManager productManager;
    private ConfigManager configManager;
    private SettingsPresentation presentation;

    private Builder() {
    }

    final Builder stringMapper(StringMapper stringMapper) {
      this.stringMapper = ObjectHelper.checkNotNull(stringMapper, "stringMapper");
      return this;
    }

    final Builder productHelper(ProductHelper productHelper) {
      this.productHelper = ObjectHelper.checkNotNull(productHelper, "productHelper");
      return this;
    }

    final Builder sessionManager(SessionManager sessionManager) {
      this.sessionManager = ObjectHelper.checkNotNull(sessionManager, "sessionManager");
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

    final Builder presentation(SettingsPresentation presentation) {
      this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
      return this;
    }

    final SettingsPresenter build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("stringMapper", ObjectHelper.isNull(this.stringMapper))
        .addPropertyNameIfMissing("productHelper", ObjectHelper.isNull(this.productHelper))
        .addPropertyNameIfMissing("sessionManager", ObjectHelper.isNull(this.sessionManager))
        .addPropertyNameIfMissing("user", ObjectHelper.isNull(this.user))
        .addPropertyNameIfMissing("productManager", ObjectHelper.isNull(this.productManager))
        .addPropertyNameIfMissing("configManager", ObjectHelper.isNull(this.configManager))
        .addPropertyNameIfMissing("presentation", ObjectHelper.isNull(this.presentation))
        .checkNoMissingProperties();
      return new SettingsPresenter(this);
    }
  }
}
