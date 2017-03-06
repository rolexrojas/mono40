package com.tpago.movil.nfc;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class NfcModule {
  @Provides
  @Singleton
  NfcHelper provideNfcAdapter(Context context) {
    return new NfcHelper(context);
  }
}
