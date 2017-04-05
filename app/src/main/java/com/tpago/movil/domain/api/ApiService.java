package com.tpago.movil.domain.api;

import com.tpago.movil.domain.Bank;
import com.tpago.movil.domain.Result;

import java.util.Set;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public interface ApiService {
  Single<Result<Set<Bank>, ApiCode>> fetchBankSet();
}
