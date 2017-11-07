package com.tpago.movil.bank;

import java.util.SortedSet;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public interface BankRepo {

  Single<SortedSet<Bank>> getAll();

  Single<Bank> findById(int id);
}
