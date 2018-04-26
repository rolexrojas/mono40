package com.tpago.movil.purchase;

import com.cube.sdk.storage.operation.AddCardParams;
import com.tpago.movil.Code;
import com.tpago.movil.product.Product;
import com.tpago.movil.session.User;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;

/**
 * Builder of {@link AddCardParams} instances
 */
final class Cub3AddCardParamsBuilder {

  static Cub3AddCardParamsBuilder create() {
    return new Cub3AddCardParamsBuilder();
  }

  private User user;
  private Product product;
  private Code pin;

  private Cub3AddCardParamsBuilder() {
  }

  final Cub3AddCardParamsBuilder user(User user) {
    this.user = ObjectHelper.checkNotNull(user, "user");
    return this;
  }

  final Cub3AddCardParamsBuilder product(Product product) {
    this.product = ObjectHelper.checkNotNull(product, "product");
    return this;
  }

  final Cub3AddCardParamsBuilder pin(Code pin) {
    this.pin = ObjectHelper.checkNotNull(pin, "pin");
    return this;
  }

  final AddCardParams build() {
    BuilderChecker.create()
      .addPropertyNameIfMissing("user", ObjectHelper.isNull(this.user))
      .addPropertyNameIfMissing("product", ObjectHelper.isNull(this.product))
      .addPropertyNameIfMissing("pin", ObjectHelper.isNull(this.pin))
      .checkNoMissingProperties();
    final AddCardParams params = new AddCardParams();
    params.setMsisdn(Cub3PosServiceUtils.userIdentifier(this.user));
    params.setAlias(Cub3PosServiceUtils.productAlias(this.product));
    params.setOtp(this.pin.value());
    return params;
  }
}
