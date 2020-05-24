package com.mono40.movil.company;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

/**
 * @author hecvasro
 */
public interface CompanyStore<T extends Company> {

  Completable sync(List<T> companies);

  Maybe<List<T>> getAll();

  Maybe<T> findByCode(int code);

  Completable clear();
}
