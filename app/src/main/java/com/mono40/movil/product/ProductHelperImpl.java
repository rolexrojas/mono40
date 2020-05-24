package com.mono40.movil.product;

import com.mono40.movil.R;
import com.mono40.movil.app.StringMapper;
import com.mono40.movil.util.BuilderChecker;
import com.mono40.movil.util.ObjectHelper;
import com.mono40.movil.util.StringHelper;

/**
 * @author hecvasro
 */
final class ProductHelperImpl implements ProductHelper {

  static Builder builder() {
    return new Builder();
  }

  private final StringMapper stringMapper;
  private final ProductTypeNameMapper productTypeNameMapper;

  private ProductHelperImpl(Builder builder) {
    this.stringMapper = builder.stringMapper;
    this.productTypeNameMapper = builder.productTypeNameMapper;
  }

  @Override
  public String getTypeName(Product product) {
    ObjectHelper.checkNotNull(product, "product");
    return this.productTypeNameMapper.apply(product.type());
  }

  @Override
  public String getTypeNameAndNumber(Product product) {
    ObjectHelper.checkNotNull(product, "product");
    return StringHelper.builder()
      .append(this.getTypeName(product))
      .append(' ')
      .append(product.numberSanitized())
      .toString();
  }

  @Override
  public String getMaskedNumber(Product product) {
    ObjectHelper.checkNotNull(product, "product");
    int stringId;
    @Product.Type final String type = product.type();
    if (type.equals(Product.Type.CC) || type.equals(Product.Type.AMEX)) {
      stringId = R.string.format_productNumber_masked_creditCard;
    } else {
      stringId = R.string.format_productNumber_masked;
    }
    return String.format(this.stringMapper.apply(stringId), product.numberSanitized());
  }

  @Override
  public String getBankNameAndTypeName(Product product) {
    ObjectHelper.checkNotNull(product, "product");
    return StringHelper.builder()
      .append(
        product.bank()
          .name()
      )
      .append(' ')
      .append(this.getTypeName(product))
      .toString();
  }

  @Override
  public String getBankNameAndTypeNameAndNumber(Product product) {
    ObjectHelper.checkNotNull(product, "product");
    return StringHelper.builder()
      .append(this.getBankNameAndTypeName(product))
      .append(' ')
      .append(product.numberSanitized())
      .toString();
  }

  static final class Builder {

    private StringMapper stringMapper;
    private ProductTypeNameMapper productTypeNameMapper;

    private Builder() {
    }

    final Builder stringMapper(StringMapper mapper) {
      this.stringMapper = ObjectHelper.checkNotNull(mapper, "stringMapper");
      return this;
    }

    final Builder productTypeNameMapper(ProductTypeNameMapper mapper) {
      this.productTypeNameMapper = ObjectHelper
        .checkNotNull(mapper, "productTypeNameMapper");
      return this;
    }

    final ProductHelperImpl build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("stringMapper", ObjectHelper.isNull(this.stringMapper))
        .addPropertyNameIfMissing(
          "productTypeNameMapper",
          ObjectHelper.isNull(this.productTypeNameMapper)
        )
        .checkNoMissingProperties();
      return new ProductHelperImpl(this);
    }
  }
}
