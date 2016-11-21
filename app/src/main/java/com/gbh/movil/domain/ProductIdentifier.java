package com.gbh.movil.domain;

/**
 * {@link Product} identifier enumeration.
 *
 * @author hecvasro
 */
public enum ProductIdentifier {
  LOAN, // PRESTAMO
  PPA, // CUENTA PREPAGO  (Los monederos tienen este tipo de cuenta)
  SAVELLA, // BANCO UNION  AHORRO (CREDITO Y DEBITO)
  TBD, // BANCO UNION TARJETA DE DEBITO VISA
  SAV, // SAVING CUENTA DE AHORRO
  CDA, // CITIBANK CUENTA  QUE SOLO PERMITE CREDITO (Pagos suplidores citi)
  DDA, // CUENTA CORRIENTE (DEBIT DIRECT ACCOUNT)
  AMEX, // TARJETA AMEX
  CC, // TARJETA VISA/MASTERCARD
  SAVCLARO // BANCO UNION  AHORRO (SOLO PERMITE CREDITOS)
}
