package com.tpago.movil.app;

import com.tpago.movil.data.DataModule;
import com.tpago.movil.gson.GsonModule;
import com.tpago.movil.job.JobModule;
import com.tpago.movil.net.NetModule;
import com.tpago.movil.session.SessionModule;
import com.tpago.movil.store.StoreModule;
import com.tpago.movil.user.UpdateCarrierManagerJob;
import com.tpago.movil.user.UpdateNameUserManagerJob;
import com.tpago.movil.user.UpdatePictureUserManagerJob;
import com.tpago.movil.user.UserModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
  modules = {
    AppComponentBuilderModule.class,
    AppModule.class,
    DataModule.class,
    GsonModule.class,
    JobModule.class,
    NetModule.class,
    SessionModule.class,
    StoreModule.class,
    UserModule.class,
    com.tpago.movil.dep.AppModule.class
  }
)
public interface AppComponent extends com.tpago.movil.dep.AppComponent {

  void inject(UpdateNameUserManagerJob job);

  void inject(UpdatePictureUserManagerJob job);

  void inject(UpdateCarrierManagerJob job);
}
