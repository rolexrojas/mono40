package com.tpago.movil.session;

import android.util.Base64;

import com.tpago.movil.Code;
import com.tpago.movil.store.Store;
import com.tpago.movil.util.ObjectHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

/**
 * @author hecvasro
 */
final class CodeSessionOpeningMethodStore {

  private static final String CHAR_SET = "UTF-8";

  private static final String STORE_KEY_CODE = "CodeSessionOpeningMethodStore.Code";

  static CodeSessionOpeningMethodStore create(
    Store store,
    SessionOpeningMethodConfigData configData
  ) {
    return new CodeSessionOpeningMethodStore(store, configData);
  }

  private final Store store;
  private final SessionOpeningMethodConfigData configData;

  private CodeSessionOpeningMethodStore(Store store, SessionOpeningMethodConfigData configData) {
    this.store = ObjectHelper.checkNotNull(store, "store");
    this.configData = ObjectHelper.checkNotNull(configData, "configData");
  }

  final void set(PublicKey key, Code code) throws Exception {
    ObjectHelper.checkNotNull(key, "key");
    ObjectHelper.checkNotNull(code, "code");
    if (this.store.isSet(STORE_KEY_CODE)) {
      throw new IllegalStateException(String.format("store.isSet(\"%1$s\")", STORE_KEY_CODE));
    }
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    final Cipher cipher = Cipher
      .getInstance(this.configData.codeCipherTransformation());
    cipher.init(Cipher.ENCRYPT_MODE, key);
    final CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);
    cipherOutputStream.write(
      code.value()
        .getBytes(CHAR_SET)
    );
    cipherOutputStream.close();
    this.store
      .set(STORE_KEY_CODE, Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT));
  }

  final Code get(PrivateKey key) throws Exception {
    ObjectHelper.checkNotNull(key, "key");
    if (!this.store.isSet(STORE_KEY_CODE)) {
      throw new IllegalStateException(String.format("!store.isSet(\"%1$s\")", STORE_KEY_CODE));
    }
    final String encryptedCode = this.store.get(STORE_KEY_CODE, String.class);
    final Cipher cipher = Cipher
      .getInstance(this.configData.codeCipherTransformation());
    cipher.init(Cipher.DECRYPT_MODE, key);
    final CipherInputStream cipherInputStream = new CipherInputStream(
      new ByteArrayInputStream(Base64.decode(encryptedCode, Base64.DEFAULT)),
      cipher
    );
    final ArrayList<Byte> valueList = new ArrayList<>();
    int nextByte;
    while ((nextByte = cipherInputStream.read()) != -1) {
      valueList.add((byte) nextByte);
    }
    final byte[] byteArray = new byte[valueList.size()];
    for (int i = 0; i < byteArray.length; i++) {
      byteArray[i] = valueList.get(i);
    }
    return Code.create(new String(byteArray, 0, byteArray.length, CHAR_SET));
  }

  final void clear() {
    this.store.remove(STORE_KEY_CODE);
  }
}
