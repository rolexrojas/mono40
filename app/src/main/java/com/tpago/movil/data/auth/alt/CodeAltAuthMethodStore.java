package com.tpago.movil.data.auth.alt;

import android.util.Base64;

import com.tpago.movil.domain.Code;
import com.tpago.movil.domain.KeyValueStore;
import com.tpago.movil.domain.KeyValueStoreHelper;
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
final class CodeAltAuthMethodStore {

  private static final String KEY = KeyValueStoreHelper
    .createKey(CodeAltAuthMethodStore.class, "Code");

  static CodeAltAuthMethodStore create(
    KeyValueStore keyValueStore,
    AltAuthMethodConfigData altAuthMethodConfigData
  ) {
    return new CodeAltAuthMethodStore(keyValueStore, altAuthMethodConfigData);
  }

  private final KeyValueStore keyValueStore;
  private final AltAuthMethodConfigData altAuthMethodConfigData;

  private CodeAltAuthMethodStore(
    KeyValueStore keyValueStore,
    AltAuthMethodConfigData altAuthMethodConfigData
  ) {
    this.keyValueStore = ObjectHelper.checkNotNull(keyValueStore, "codeAltAuthMethodStore");
    this.altAuthMethodConfigData = ObjectHelper.checkNotNull(
      altAuthMethodConfigData,
      "altAuthMethodConfigData"
    );
  }

  final void set(PublicKey publicKey, Code code) throws Exception {
    ObjectHelper.checkNotNull(publicKey, "publicKey");
    ObjectHelper.checkNotNull(code, "code");

    if (this.keyValueStore.isSet(KEY)) {
      throw new IllegalStateException(String.format("keyValueStore.isSet(%1$s)", KEY));
    }

    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    final Cipher cipher = Cipher.getInstance(this.altAuthMethodConfigData.keyGenAlgName());
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    final CipherOutputStream cipherOutputStream = new CipherOutputStream(
      outputStream,
      cipher
    );
    cipherOutputStream.write(
      code.value()
        .getBytes("UTF-8")
    );
    cipherOutputStream.close();

    this.keyValueStore.set(KEY, Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT));
  }

  final Code get(PrivateKey privateKey) throws Exception {
    ObjectHelper.checkNotNull(privateKey, "privateKey");

    if (!this.keyValueStore.isSet(KEY)) {
      throw new IllegalStateException(String.format("!keyValueStore.isSet(%1$s)", KEY));
    }

    final String encryptedCode = this.keyValueStore.get(KEY);

    final Cipher cipher = Cipher.getInstance(this.altAuthMethodConfigData.keyGenAlgName());
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
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

    return Code.create(new String(byteArray, 0, byteArray.length, "UTF-8"));
  }

  final void clear() {
    this.keyValueStore.remove(KEY);
  }
}
