package swtp12.modulecrediting.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import swtp12.modulecrediting.service.AuthService;

/**
 * This class has functions for encrypting and decrypting Token strings
 */
public class SecurityCipher {
    private static final String KEYVALUE = "JPnJqOfaEge";
    private static SecretKeySpec secretKey;
    private static byte[] key;

    private SecurityCipher() {
        throw new AssertionError("Static!");
    }

    /**
     * This function sets the {@link #secretKey} 
     * @see SecretKeySpec
     */
    public static void setKey() {
        MessageDigest sha;
        try {
            key = KEYVALUE.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function decrypts the given String
     */
    public static String encrypt(String strToEncrypt) {
        if (strToEncrypt == null) return null;

        try {
            setKey();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This function decrypts the given String
     */
    public static String decrypt(String strToDecrypt) {
        if (strToDecrypt == null) return null;

        /*
         * There seems to be an issue with "setKey()" while decrypting. Therefore I had to 
         * let the code below run multiple times, if the decrypt was not succesfull, due to the diffrent
         * generated key
         * 
         * Somebody more expirenced with this, might find the issue, why sometimes "setKey()"
         * generates a diffrent key. 
         * 
         * EDIT: it now deletes the "refreshToken" if the error persists. This helps with 'old' 
         *          "refreshToken" generated on an old backend version.
         * 
         * Frederik Kluge
         */
        int counter = 0;
        int finished = 3;
        while (counter < finished) {
            try {
                setKey();
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, secretKey);

                //Debug:
                //LogUtil.printLog("Debug: SecurityCiper -> SecretKey: " + secretKey + " / HashCode: " + secretKey.hashCode() + " / Try:" + counter);


                return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
            } catch (Exception e) {
                LogUtil.printLog("The specific error in SecurityCipher: " + counter);
                counter ++;
            }
        }
        AuthService.deleteRefreshCookie();
        AuthService.logout();
        return null;
    }
}