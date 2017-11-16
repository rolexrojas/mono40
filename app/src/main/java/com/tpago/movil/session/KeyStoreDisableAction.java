package com.tpago.movil.session;

import com.tpago.movil.util.ObjectHelper;

import java.security.KeyStore;

/**
 * @author hecvasro
 */
final class KeyStoreDisableAction implements UnlockMethodDisableAction {

  static KeyStoreDisableAction create(UnlockMethodConfigData configData, KeyStore keyStore) {
    return new KeyStoreDisableAction(configData, keyStore);
  }

  private final UnlockMethodConfigData configData;
  private final KeyStore keyStore;

  private KeyStoreDisableAction(UnlockMethodConfigData configData, KeyStore keyStore) {
    this.configData = ObjectHelper.checkNotNull(configData, "unlockMethodConfigData");
    this.keyStore = ObjectHelper.checkNotNull(keyStore, "keyStore");
  }

  @Override
  public void run() throws Exception {
    final String alias = this.configData.keyAlias();
    if (this.keyStore.containsAlias(alias)) {
      this.keyStore.deleteEntry(alias);
    }
  }
}
