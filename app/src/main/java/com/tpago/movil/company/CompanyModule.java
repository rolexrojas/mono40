package com.tpago.movil.company;

import com.tpago.movil.DisplayDensity;
import com.tpago.movil.company.bank.BankModule;
import com.tpago.movil.company.partner.PartnerModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module(includes = {
  BankModule.class,
  PartnerModule.class
})
public final class CompanyModule {

  @Provides
  @Singleton
  CompanyHelper companyHelper(DisplayDensity displayDensity) {
    return CompanyHelper.create(displayDensity);
  }
}
