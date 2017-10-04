package com.tpago.movil.app;

import com.tpago.movil.data.DataModule;
import com.tpago.movil.net.NetModule;
import com.tpago.movil.session.SessionModule;
import com.tpago.movil.user.UserModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
  modules = {
    AppComponentBuilderModule.class,
    AppModule.class,
    DataModule.class,
    NetModule.class,
    SessionModule.class,
    UserModule.class,
    com.tpago.movil.dep.AppModule.class
  }
)
public interface AppComponent extends com.tpago.movil.dep.AppComponent {
}
