package com.q42.qlassified;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.q42.qlassified.Storage.QlassifiedSharedPreferencesService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by maz on 9/23/2017 AD.
 */

public class EncryptedPreferrences {

    private static final String TAG = EncryptedPreferrences.class.getSimpleName();

    private static final String AES_KEY = "AES_KEY";
    private static final String AES_VECTOR = "AES_VECTOR";
    private static SharedPreferences sharedPreferences;
    private static boolean isInit = false;

    /**
     * Convenient for init Qlassified.Service
     * @param context - Should be Application Context Only
     */
    public static void init(Context context, SharedPreferences sharedPreferences) {
        EncryptedPreferrences.sharedPreferences = sharedPreferences;
        Qlassified.Service.start(context);
        Qlassified.Service.setStorageService(new QlassifiedSharedPreferencesService(sharedPreferences));
        initAes();
        isInit = true;
    }

    private static void initAes() {
        //TODO: Init AES Keypair. Then encrypt with RSA key that keeped in Android Keystore System. Then save in Preferences with Base64.
        //TODO: Use this technic because AES is support encryption for a long length data more than 46. (If length more than 46. It will throw exception of RSA block size)

        try {
            String keyAes = md5Key(AES_KEY);
            if (!sharedPreferences.contains(keyAes)) {
                KeyGenerator keyGen = KeyGenerator.getInstance("AES");
                keyGen.init(256);
                SecretKey secretKey = keyGen.generateKey();
                Qlassified.Service.put(keyAes, Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT));
            }

            String keyVector = md5Key(AES_VECTOR);
            if (!sharedPreferences.contains(keyVector)) {
                KeyGenerator keyGen = KeyGenerator.getInstance("AES");
                keyGen.init(128);
                SecretKey secretKey = keyGen.generateKey();
                Qlassified.Service.put(keyVector, Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(TAG, "initAes Exception: "+e.getMessage());
        }
    }

    public static void putInt(String key, int value) throws Exception {
        putString(key, value+"");
    }

    public static int getInt(String key) throws Exception {
        String value = getString(key);
        if(TextUtils.isEmpty(value)){
            return -1;
        }else{
            return Integer.parseInt(value);
        }
    }

    public static void putFloat(String key, float value) throws Exception {
        putString(key, value+"");
    }

    public static float getFloat(String key) throws Exception {
        String value = getString(key);
        if(TextUtils.isEmpty(value)){
            return -1;
        }else{
            return Float.parseFloat(value);
        }
    }

    public static void putDouble(String key, double value) throws Exception {
        putString(key, value+"");
    }

    public static double getDouble(String key) throws Exception {
        String value = getString(key);
        if(TextUtils.isEmpty(value)){
            return -1;
        }else{
            return Double.parseDouble(value);
        }
    }

    public static void putLong(String key, double value) throws Exception {
        putString(key, value+"");
    }

    public static long getLong(String key) throws Exception {
        String value = getString(key);
        if(TextUtils.isEmpty(value)){
            return -1;
        }else{
            return Long.parseLong(value);
        }
    }

    public static void putBoolean(String key, String value) throws Exception {
        putString(key, value+"");
    }

    public static boolean getBoolean(String key) throws Exception {
        String value = getString(key);
        if(TextUtils.isEmpty(value)){
            return false;
        }else{
            return Boolean.parseBoolean(value);
        }
    }

    public static void putString(String key, String value) throws Exception {
        checkInit();
        key = md5Key(key);
        sharedPreferences.edit().putString(key, encryptWithAes(value)).commit();
    }

    public static String getString(String key) throws Exception {
        checkInit();
        key = md5Key(key);
        String value = "";
        if (sharedPreferences.contains(key)) value = decryptWithAes(sharedPreferences.getString(key, ""));
        return value;
    }

    private static void checkInit() throws Exception{
        if(!isInit) throw new Exception("Please initial EncryptedPreferences before using.");
    }

    private static String encryptWithAes(String plainText) {
        String message = plainText;
        try {
            String aesKey = Qlassified.Service.getString(md5Key(AES_KEY));
            String aesVec = Qlassified.Service.getString(md5Key(AES_VECTOR));

            SecretKeySpec sKeySpec = new SecretKeySpec(Base64.decode(aesKey, Base64.DEFAULT), "AES");
            IvParameterSpec iv = new IvParameterSpec(Base64.decode(aesVec, Base64.DEFAULT));

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, iv);

            byte[] encrypted = cipher.doFinal(plainText.getBytes());

            message =  Base64.encodeToString(encrypted, Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }

    private static String decryptWithAes(String cipherStr) {
        String message = cipherStr;
        try {
            String aesKey = Qlassified.Service.getString(md5Key(AES_KEY));
            String aesVec = Qlassified.Service.getString(md5Key(AES_VECTOR));

            SecretKeySpec sKeySpec = new SecretKeySpec(Base64.decode(aesKey, Base64.DEFAULT), "AES");
            IvParameterSpec iv = new IvParameterSpec(Base64.decode(aesVec, Base64.DEFAULT));

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, iv);

            byte[] plainText = cipher.doFinal(Base64.decode(cipherStr, Base64.DEFAULT));

            message = new String(plainText, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }

    private static synchronized String md5Key(String key) {
        final String MD5 = "MD5";
        String newKey = key;
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(key.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            newKey = hexString.toString();
            newKey = newKey.replaceAll("\n","");
            if(newKey.length()>150) {
                //TODO: Handle maximum for length of shared preferences key.
                newKey = newKey.substring(newKey.length()-150);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Logger.e(TAG, "NoSuchAlgorithmException: "+e.getMessage());
        }
        return newKey;
    }
}

