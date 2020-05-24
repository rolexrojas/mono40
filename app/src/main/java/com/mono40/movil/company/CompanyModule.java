package com.mono40.movil.company;

import com.mono40.movil.DisplayDensity;
import com.mono40.movil.company.bank.BankModule;
import com.mono40.movil.company.partner.PartnerModule;

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
