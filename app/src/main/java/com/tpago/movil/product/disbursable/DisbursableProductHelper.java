package com.tpago.movil.product.disbursable;

/**
 * @author hecvasro
 */
public interface DisbursableProductHelper {

  String getTypeName(Disbursable disbursable);

  String getTypeName(DisbursableProduct product);

  String getTypeNameAndNumber(Disbursable disbursable);

  String getTypeNameAndNumber(DisbursableProduct product);

  String getDestinationTypeName(Disbursable disbursable);

  String getDestinationTypeName(DisbursableProduct product);

  String getDestinationTypeNameAndNumber(Disbursable disbursable);

  String getDestinationTypeNameAndNumber(DisbursableProduct product);
}
