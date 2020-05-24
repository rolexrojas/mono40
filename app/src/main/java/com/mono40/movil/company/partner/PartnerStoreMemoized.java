package com.mono40.movil.company.partner;

import com.mono40.movil.company.CompanyStoreMemoized;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * @author hecvasro
 */
final class PartnerStoreMemoized implements PartnerStore {

  static PartnerStoreMemoized create() {
    return new PartnerStoreMemoized();
  }

  private final CompanyStoreMemoized<Partner> store;

  private PartnerStoreMemoized() {
    this.store = CompanyStoreMemoized.create();
  }

  @Override
  public Completable sync(List<Partner> partners) {
    return this.store.sync(partners);
  }

  @Override
  public Maybe<List<Partner>> getAll() {
    return this.store.getAll();
  }

  @Override
  public Maybe<Partner> findByCode(int code) {
    return this.store.findByCode(code);
  }

  @Override
  public Completable clear() {
    return this.store.clear();
  }

  private Maybe<List<Partner>> getAllFilteredByType(@Partner.Type String type) {
    return this.getAll()
      .flatMapObservable(Observable::fromIterable)
      .filter((partner) -> type.equals(partner.type()))
      .toList()
      .toMaybe();
  }

  @Override
  public Maybe<List<Partner>> getCarriers() {
    return this.getAllFilteredByType(Partner.Type.CARRIER);
  }

  @Override
  public Maybe<List<Partner>> getProviders() {
    return this.getAllFilteredByType(Partner.Type.PROVIDER);
  }
}
