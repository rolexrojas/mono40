package com.tpago.movil.data.auth.alt;

import android.util.Base64;

import com.tpago.movil.domain.Code;
import com.tpago.movil.domain.KeyValueStore;
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
final class CodeAuthMethodStore {

  static CodeAuthMethodStore create(KeyValueStore store, AltAuthConfigData configData) {
    return new CodeAuthMethodStore(store, configData);
  }

  private final KeyValueStore store;
  private final AltAuthConfigData configData;

  private CodeAuthMethodStore(KeyValueStore store, AltAuthConfigData configData) {
    this.store = ObjectHelper.checkNotNull(store, "store");
    this.configData = ObjectHelper.checkNotNull(configData, "configData");
  }

  final void set(PublicKey publicKey, Code code) throws Exception {
    ObjectHelper.checkNotNull(publicKey, "publicKey");
    ObjectHelper.checkNotNull(code, "code");

    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    final Cipher cipher = Cipher.getInstance(this.configData.keyGenAlgName());
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

    this.store.set(
      this.configData.codeMethodKey(),
      Base64.encodeToString(
        outputStream.toByteArray(),
        Base64.DEFAULT
      )
    );
  }

  final Code get(PrivateKey privateKey) throws Exception {
    ObjectHelper.checkNotNull(privateKey, "privateKey");

    final String key = this.configData.codeMethodKey();
    if (!this.store.isSet(key)) {
      throw new IllegalStateException(String.format("!this.store.isSet(%1$s)", key));
    }

    final String encryptedCode = this.store.get(key);

    final Cipher cipher = Cipher.getInstance(this.configData.keyGenAlgName());
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    final CipherInputStream cipherInputStream = new CipherInputStream(
      new ByteArrayInputStream(
        Base64.decode(
          encryptedCode,
          Base64.DEFAULT
        )
      ),
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
    this.store.remove(this.configData.codeMethodKey());
  }
}
