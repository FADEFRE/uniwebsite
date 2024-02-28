package swtp12.modulecrediting.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class SecurityCipher {
    private static final String KEYVALUE = "JPnJqOfaEge";
    private static SecretKeySpec secretKey;
    private static byte[] key;

    private SecurityCipher() {
        throw new AssertionError("Static!");
    }

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


    public static String decrypt(String strToDecrypt) throws IncorrectKeyOnDecryptException {
        if (strToDecrypt == null) return null;

        /*
         * There seems to be an issue with "setKey()" while decrypting. Therefore I had to 
         * let the code below run twice, if the decrypt was not succesfull, due to the diffrent
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
                //System.out.println("Debug: SecurityCiper -> SecretKey: " + secretKey + " / HashCode: " + secretKey.hashCode() + " / Try:" + counter);


                return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
            } catch (Exception e) {
                System.out.println(LocalDateTime.now() + " Der Fehler im Backend: " + counter);
                counter ++;
            }
        }
        throw new IncorrectKeyOnDecryptException("there seems to be a problem with your authentication Tokens, please try again");
    }
}