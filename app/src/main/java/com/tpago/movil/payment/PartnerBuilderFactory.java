package com.tpago.movil.payment;

import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
public final class PartnerBuilderFactory {

  public static PartnerBuilder make(Partner.Type type) {
    switch (ObjectHelper.checkNotNull(type, "type")) {
      case CARRIER:
        return Carrier.builder();
      case PROVIDER:
        return Provider.builder();
      default:
        throw new UnsupportedOperationException(
          String.format("Partner type \"%1$s\" not supported", type)
        );
    }
  }

  private PartnerBuilderFactory() {
  }
}
