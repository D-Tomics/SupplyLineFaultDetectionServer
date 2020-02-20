package data;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Encrypt {

    private final static String PBKDF_ALGORITHM = "PBKDF2WithHmacSHA512";
    private final static int SALT_LENGTH = 64;
    private final static int DERIVED_KEY_LENGTH = 64;
    private final static int ITERATION_COUNT = 10000;

    public static Password hashPassword(String password) {

        byte[] salt = new byte[SALT_LENGTH];
        SecureRandom csprng = new SecureRandom();
        csprng.nextBytes(salt);

        return hashPassword(password,salt);      
    }

    public static Password hashPassword(String password, byte[] salt) {
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, DERIVED_KEY_LENGTH * 8);
        SecretKeyFactory keyFactory = null;
        try {
            keyFactory = SecretKeyFactory.getInstance(PBKDF_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hashedArray = null;
        try {
            hashedArray = keyFactory.generateSecret(keySpec).getEncoded();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return new Password(Base64.getEncoder().encodeToString(hashedArray),
                            Base64.getEncoder().encodeToString(salt));
    }


}