package com.gbh.movil;

import com.gbh.movil.data.DataModule;
import com.gbh.movil.data.MessageHelper;
import com.gbh.movil.data.net.NetworkStatusBroadcastReceiver;
import com.gbh.movil.domain.BalanceManager;
import com.gbh.movil.domain.DataLoader;
import com.gbh.movil.domain.NetworkHelper;

import javax.inject.Singleton;

import dagger.Component;

/**
 * TODO
 *
 * @author hecvasro
 */
@Singleton
@Component(modules = { AppModule.class, DataModule.class })
public interface AppComponent {
  void inject(NetworkStatusBroadcastReceiver receiver);

  MessageHelper provideMessageHelper();
  NetworkHelper provideNetworkhelper();
  DataLoader provideDataLoader();
  BalanceManager provideBalanceManager();
}
