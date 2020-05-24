package com.mono40.movil.product.disbursable;

import androidx.annotation.StringRes;
import android.util.Log;

import com.mono40.movil.R;
import com.mono40.movil.company.bank.Bank;
import com.mono40.movil.app.StringMapper;
import com.mono40.movil.product.ProductTypeNameMapper;
import com.mono40.movil.util.BuilderChecker;
import com.mono40.movil.util.ObjectHelper;
import com.mono40.movil.util.StringHelper;

/**
 * @author hecvasro
 */
final class DisbursableProductHelperImpl implements DisbursableProductHelper {

  static Builder builder() {
    return new Builder();
  }

  private final StringMapper stringMapper;
  private final ProductTypeNameMapper productTypeNameMapper;

  private DisbursableProductHelperImpl(Builder builder) {
    this.stringMapper = builder.stringMapper;
    this.productTypeNameMapper = builder.productTypeNameMapper;
  }

  @Override
  public String getTypeName(Disbursable disbursable) {
    ObjectHelper.checkNotNull(disbursable, "disbursable");
    @StringRes final int stringId;
    if (Bank.Transaction.Type.EXTRA_CREDIT.toLowerCase().equals(disbursable.type().toLowerCase())) {
      stringId = R.string.extraCredit;
    } else {
      stringId = R.string.salaryAdvance;
    }
    return this.stringMapper.apply(stringId);
  }

  @Override
  public String getTypeName(DisbursableProduct product) {
    ObjectHelper.checkNotNull(product, "product");
    return this.getTypeName(product.disbursable());
  }

  @Override
  public String getTypeNameAndNumber(Disbursable disbursable) {
    ObjectHelper.checkNotNull(disbursable, "disbursable");
    return StringHelper.builder()
      .append(this.getTypeName(disbursable))
      .append(' ')
      .append(disbursable.numberSanitized())
      .toString();
  }

  @Override
  public String getTypeNameAndNumber(DisbursableProduct product) {
    ObjectHelper.checkNotNull(product, "product");
    return this.getTypeNameAndNumber(product.disbursable());
  }

  @Override
  public String getDestinationTypeName(Disbursable disbursable) {
    ObjectHelper.checkNotNull(disbursable, "disbursable");
    return this.productTypeNameMapper.apply(disbursable.destinationProductType());
  }

  @Override
  public String getDestinationTypeName(DisbursableProduct product) {
    ObjectHelper.checkNotNull(product, "product");
    return this.getDestinationTypeName(product.disbursable());
  }

  @Override
  public String getDestinationTypeNameAndNumber(Disbursable disbursable) {
    ObjectHelper.checkNotNull(disbursable, "disbursable");
    return StringHelper.builder()
      .append(this.getDestinationTypeName(disbursable))
      .append(' ')
      .append(disbursable.destinationProductNumberSanitized())
      .toString();
  }

  @Override
  public String getDestinationTypeNameAndNumber(DisbursableProduct product) {
    ObjectHelper.checkNotNull(product, "product");
    return this.getDestinationTypeNameAndNumber(product.disbursable());
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

    final DisbursableProductHelperImpl build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("stringMapper", ObjectHelper.isNull(this.stringMapper))
        .addPropertyNameIfMissing(
          "productTypeNameMapper",
          ObjectHelper.isNull(this.productTypeNameMapper)
        )
        .checkNoMissingProperties();
      return new DisbursableProductHelperImpl(this);
    }
  }
}
