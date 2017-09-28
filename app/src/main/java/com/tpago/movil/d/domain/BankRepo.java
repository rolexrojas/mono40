package com.tpago.movil.d.domain;

import com.tpago.movil.util.Resettable;

import java.util.Set;

/**
 * @author hecvasro
 */
public interface BankRepo extends Resettable {
  Set<Bank> getAll();
  void saveAll(Set<Bank> bankSet);
}
