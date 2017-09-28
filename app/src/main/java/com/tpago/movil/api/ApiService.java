package com.tpago.movil.api;

import com.tpago.movil.d.domain.Bank;
import com.tpago.movil.d.domain.Result;

import java.util.Set;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
@Deprecated
public interface ApiService {
  Single<Result<Set<Bank>, ApiCode>> fetchBankSet();
}
