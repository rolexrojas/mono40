package com.tpago.movil.dep.domain;

import com.tpago.movil.R;

/**
 * {@link Product} type enumeration.
 *
 * @author hecvasro
 */
@Deprecated
public enum ProductType {
  LOAN, // PRESTAMO
  PPA, // CUENTA PREPAGO  (Los monederos tienen este tipo de cuenta)
  SAVELLA, // BANCO UNION  AHORRO (CREDITO Y DEBITO)
  TBD, // BANCO UNION TARJETA DE DEBITO VISA
  SAV, // SAVING CUENTA DE AHORRO
  CDA, // CITIBANK CUENTA  QUE SOLO PERMITE CREDITO (Pagos suplidores citi)
  DDA, // CUENTA CORRIENTE (DEBIT DIRECT ACCOUNT)
  AMEX, // TARJETA AMEX
  CC, // TARJETA VISA/MASTERCARD
  SAVCLARO; // BANCO UNION AHORRO (SOLO PERMITE CREDITOS)

  public static int findStringId(Product product) {
    switch (product.getType()) {
      case LOAN:
        return R.string.loan;
      case SAV:
      case SAVELLA:
      case SAVCLARO:
        return R.string.savings;
      case TBD:
        return R.string.debit;
      case PPA:
        return R.string.prepay;
      case DDA:
        return R.string.current;
      case CC:
      case AMEX:
        return R.string.credit;
      default:
        return R.string.unknown;
    }
  }
}
