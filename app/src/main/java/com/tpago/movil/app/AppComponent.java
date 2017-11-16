package com.tpago.movil.app;

import com.tpago.movil.bank.BankModule;
import com.tpago.movil.company.CompanyModule;
import com.tpago.movil.data.DataModule;
import com.tpago.movil.gson.GsonModule;
import com.tpago.movil.job.JobModule;
import com.tpago.movil.net.NetModule;
import com.tpago.movil.session.SessionModule;
import com.tpago.movil.session.UpdateUserCarrierJob;
import com.tpago.movil.session.UpdateUserNameJob;
import com.tpago.movil.session.UpdateUserPictureJob;
import com.tpago.movil.store.StoreModule;
import com.tpago.movil.time.TimeModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
  modules = {
    AppComponentBuilderModule.class,
    AppModule.class,
    BankModule.class,
    CompanyModule.class,
    DataModule.class,
    GsonModule.class,
    JobModule.class,
    NetModule.class,
    SessionModule.class,
    StoreModule.class,
    TimeModule.class,
    com.tpago.movil.dep.AppModule.class
  }
)
public interface AppComponent extends com.tpago.movil.dep.AppComponent {

  void inject(UpdateUserNameJob job);

  void inject(UpdateUserPictureJob job);

  void inject(UpdateUserCarrierJob job);
}
