package com.tpago.movil.insurance.micro;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface MicroInsurancePartnerStore {

  Completable sync(List<MicroInsurancePartner> entries);

  Maybe<List<MicroInsurancePartner>> getAll();

  Completable clear();
}
