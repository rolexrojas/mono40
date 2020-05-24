package com.mono40.movil.session;

import android.util.Base64;

import com.mono40.movil.Code;
import com.mono40.movil.store.DiskStore;
import com.mono40.movil.util.ObjectHelper;

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
final class CodeStore {

  private static final String CHAR_SET = "UTF-8";

  private static final String STORE_KEY_CODE = "CodeStore.Code";

  static CodeStore create(UnlockMethodConfigData configData, DiskStore diskStore) {
    return new CodeStore(configData, diskStore);
  }

  private final UnlockMethodConfigData configData;
  private final DiskStore diskStore;

  private CodeStore(UnlockMethodConfigData configData, DiskStore diskStore) {
    this.configData = ObjectHelper.checkNotNull(configData, "configData");
    this.diskStore = ObjectHelper.checkNotNull(diskStore, "diskStore");
  }

  final void set(PublicKey key, Code code) throws Exception {
    ObjectHelper.checkNotNull(key, "key");
    ObjectHelper.checkNotNull(code, "code");
    if (this.diskStore.isSet(STORE_KEY_CODE)) {
      throw new IllegalStateException(String.format("diskStore.isSet(\"%1$s\")", STORE_KEY_CODE));
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
    this.diskStore
      .set(STORE_KEY_CODE, Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT))
      .blockingAwait();
  }

  final Code get(PrivateKey key) throws Exception {
    ObjectHelper.checkNotNull(key, "key");
    if (!this.diskStore.isSet(STORE_KEY_CODE)) {
      throw new IllegalStateException(String.format("!diskStore.isSet(\"%1$s\")", STORE_KEY_CODE));
    }
    final String encryptedCode = this.diskStore.get(STORE_KEY_CODE, String.class)
      .blockingGet();
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
    this.diskStore.remove(STORE_KEY_CODE)
      .blockingAwait();
  }
}
