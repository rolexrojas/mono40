package com.tpago.movil.d.domain;

import com.tpago.movil.dep.Resettable;

import java.util.Set;

/**
 * @author hecvasro
 */
@Deprecated
public interface BankRepo extends Resettable {
  Set<Bank> getAll();
  void saveAll(Set<Bank> bankSet);
}
