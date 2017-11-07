package com.tpago.movil.company;

import com.tpago.movil.function.Supplier;

import java.util.List;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public interface CompanyListSupplier<T extends Company> extends Supplier<Single<List<T>>> {
}
