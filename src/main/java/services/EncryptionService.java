package services;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptionService {
    static byte[] key = "1234567890123456".getBytes();

    public static String EncryptAES(String plaintext)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] cipherText = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String DecryptAES(String cipherText)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decodedCipherText = Base64.getDecoder().decode(cipherText);

        try{
            FileOutputStream fos = new FileOutputStream(new File("/home/target/bytes"));
            fos.write(decodedCipherText);
            fos.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        byte[] decryptedText = cipher.doFinal(decodedCipherText);

        return new String(decryptedText, StandardCharsets.UTF_8);
    }
}
