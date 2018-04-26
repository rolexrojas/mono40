package com.tpago.movil.purchase;

import com.cube.sdk.storage.operation.SelectCardParams;
import com.tpago.movil.product.Product;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

/**
 * Builder of {@link SelectCardParams} instances
 */
final class Cub3SelectCardParamsBuilder {

  static Cub3SelectCardParamsBuilder create() {
    return new Cub3SelectCardParamsBuilder();
  }

  private Product product;
  private String altPan;

  private Cub3SelectCardParamsBuilder() {
  }

  final Cub3SelectCardParamsBuilder product(Product product) {
    this.product = ObjectHelper.checkNotNull(product, "product");
    return this;
  }

  final Cub3SelectCardParamsBuilder altPan(String altPan) {
    this.altPan = StringHelper.checkIsNotNullNorEmpty(altPan, "altPan");
    return this;
  }

  final SelectCardParams build() {
    BuilderChecker.create()
      .addPropertyNameIfMissing("product", ObjectHelper.isNull(this.product))
      .addPropertyNameIfMissing("altPan", StringHelper.isNullOrEmpty(this.altPan))
      .checkNoMissingProperties();
    final SelectCardParams params = new SelectCardParams();
    params.setAlias(Cub3PosServiceUtils.productAlias(this.product));
    params.setAltpan(this.altPan);
    return params;
  }
}
