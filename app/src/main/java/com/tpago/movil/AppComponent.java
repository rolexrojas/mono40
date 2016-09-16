package com.tpago.movil;

import com.tpago.movil.data.DataModule;
import com.tpago.movil.data.MessageHelper;
import com.tpago.movil.data.net.NetworkHelper;
import com.tpago.movil.data.net.NetworkStatusBroadcastReceiver;
import com.tpago.movil.domain.BalanceManager;
import com.tpago.movil.domain.DataLoader;

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
