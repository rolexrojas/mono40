package com.gbh.movil;

import com.gbh.movil.data.DataModule;
import com.gbh.movil.data.MessageHelper;
import com.gbh.movil.data.net.NetworkHelper;
import com.gbh.movil.data.net.NetworkStatusBroadcastReceiver;
import com.gbh.movil.domain.BalanceManager;
import com.gbh.movil.domain.DataLoader;

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
  /**
   * TODO
   *
   * @param receiver
   *   TODO
   */
  void inject(NetworkStatusBroadcastReceiver receiver);

  /**
   * TODO
   *
   * @return TODO
   */
  MessageHelper provideMessageHelper();

  /**
   * TODO
   *
   * @return TODO
   */
  NetworkHelper provideNetworkManager();

  /**
   * TODO
   *
   * @return TODO
   */
  DataLoader provideDataLoader();

  /**
   * TODO
   *
   * @return TODO
   */
  BalanceManager provideBalanceManager();
}
