package com.tpago.movil.product;

import com.tpago.movil.R;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

/**
 * @author hecvasro
 */
public final class ProductTypeNameMapper {

  static ProductTypeNameMapper create(StringMapper stringMapper) {
    return new ProductTypeNameMapper(stringMapper);
  }

  private final StringMapper stringMapper;

  private ProductTypeNameMapper(StringMapper stringMapper) {
    this.stringMapper = ObjectHelper.checkNotNull(stringMapper, "stringMapper");
  }

  public final String apply(@Product.Type String type) {
    StringHelper.checkIsNotNullNorEmpty(type, type);
    int stringId;
    if (type.equals(Product.Type.LOAN)) {
      stringId = R.string.loan;
    } else if (type.equals(Product.Type.DDA)) {
      stringId = R.string.current;
    } else if (type.equals(Product.Type.PPA)) {
      stringId = R.string.prepaid;
    } else if (type.equals(Product.Type.TBD)) {
      stringId = R.string.debit;
    } else if (type.equals(Product.Type.CC) || type.equals(Product.Type.AMEX)) {
      stringId = R.string.credit;
    } else {
      stringId = R.string.savings;
    }
    return this.stringMapper.apply(stringId);
  }
}
