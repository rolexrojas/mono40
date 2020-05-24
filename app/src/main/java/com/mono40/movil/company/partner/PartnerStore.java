package com.mono40.movil.company.partner;

import com.mono40.movil.company.CompanyStore;

import java.util.List;

import io.reactivex.Maybe;

/**
 * @author hecvasro
 */
public interface PartnerStore extends CompanyStore<Partner> {

  Maybe<List<Partner>> getCarriers();

  Maybe<List<Partner>> getProviders();
}
