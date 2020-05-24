package com.mono40.movil.product;

/**
 * @author hecvasro
 */
public interface ProductHelper {

  String getTypeName(Product product);

  String getTypeNameAndNumber(Product product);

  String getMaskedNumber(Product product);

  String getBankNameAndTypeName(Product product);

  String getBankNameAndTypeNameAndNumber(Product product);
}
