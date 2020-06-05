package com.mono40.movil.util;


import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.commons.codec.DecoderException;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import okio.ByteString;

public class QrDecryptUtil {
    static {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }


    public static QrJWT decryptData(String data, String key) throws DecoderException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        Log.d("com.cryptoqr.mobile", "decryptData - " + data + " | " + key);
        byte[] decodedKey = Base64.decode(key, 0);
        SecretKey llave = new SecretKeySpec(decodedKey, "AES");
        byte[] bytesDataEncriptada = Base64.decode(data, 0);
        byte[] bytesDesencriptados = decrypt(llave, bytesDataEncriptada);
        String jwtStr = Base64.encodeToString(bytesDesencriptados, 0);
        Log.d("com.cryptoqr.mobile", "jwtStr=" + jwtStr);
        return decodeJwt(jwtStr);
    }

    public static QrJwtMnt decryptQrData(String data, String key) throws DecoderException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        Log.d("com.cryptoqr.mobile", "decryptData - " + data + " | " + key);
        byte[] decodedKey = Base64.decode(key, 0);
        SecretKey llave = new SecretKeySpec(decodedKey, "AES");
        byte[] bytesDataEncriptada = Base64.decode(data, 0);
        byte[] bytesDesencriptados = decrypt(llave, bytesDataEncriptada);
        String jwtStr = Base64.encodeToString(bytesDesencriptados, 0);
        Log.d("com.cryptoqr.mobile", "jwtStr=" + jwtStr);

        return decodeQrJwt(jwtStr);
    }

    private static byte[] decrypt(SecretKey secretKey, byte[] encryptedData)
            throws NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidAlgorithmParameterException,
            InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException,
            InvalidKeySpecException {


        //Wrap the data into a byte buffer to ease the reading process
        ByteBuffer byteBuffer = ByteBuffer.wrap(encryptedData);

        int noonceSize = byteBuffer.getInt();

        //Make sure that the file was encrypted properly
        if (noonceSize < 12 || noonceSize >= 16) {
            throw new IllegalArgumentException("Nonce size is incorrect. Make sure that the incoming data is an AES encrypted file.");
        }
        byte[] iv = new byte[12];
        byteBuffer.get(iv);

        //Prepare your key/password
        //SecretKey secretKey = generateSecretKey(key, iv);

        //get the rest of encrypted data
        byte[] cipherBytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherBytes);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");


        GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);

        //Encryption mode on!
        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

        //Encrypt the data
        return cipher.doFinal(cipherBytes);

    }

    public static QrJWT decodeJwt(String jwtToken) {
        String[] split_string = ByteString.decodeBase64(jwtToken.replaceAll("\n", "")).string(Charset.forName("utf-8")).split("\\.");
        String base64EncodedBody = split_string[1];

        Log.d("com.cryptoqr.mobile", "JWT Token encoded = " + base64EncodedBody);

        String body = ByteString.decodeBase64(base64EncodedBody).string(Charset.forName("utf-8"));

        Log.d("com.cryptoqr.mobile", "JWT Token = " + base64EncodedBody);
        Gson gson = new Gson();
        return gson.fromJson(gson.fromJson(body, QrJWT.class).getSub(), QrJWT.class);
    }

    public static QrJwtMnt decodeQrJwt(String jwtToken) {

        String body = ByteString.decodeBase64(jwtToken.replaceAll("\n", "")).string(Charset.forName("utf-8"));
       // String base64EncodedBody = split_string;
        //Log.d("com.cryptoqr.mobile", "JWT Token encoded = " + split_string);

       // String body = ByteString.decodeBase64(split_string).string(Charset.forName("utf-8"));
        //String body = ByteString.decodeBase64(base64EncodedBody).string(Charset.forName("utf-8"));
       Log.d("com.cryptoqr.mobile", "JWT body = " + body);
        Gson gson = new Gson();
        return gson.fromJson(body, QrJwtMnt.class);
    }
}
