package com.rabo.paymentinitiation.util;

import javax.crypto.Cipher;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

public class DigitalSignatureWithMessageDigestAndCipherVerifying {

    public static void main(String args[]) throws Exception {

        PublicKey publicKey = Utils.getPublicKey();
        PrivateKey privateKey = Utils.getPrivateKey();

        byte[] messageBytes = "Kalaiselvan".getBytes();//Files.readAllBytes(Paths.get("src/test/resources/digitalsignature/message.txt"));

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] newMessageHash = md.digest(messageBytes);

        //byte[] encryptedMessageHash = Files.readAllBytes(Paths.get("target/digital_signature_1"));
        
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] digitalSignatureEncryption = cipher.doFinal(newMessageHash);

        //digitalSignatureEncryption[0] = (byte) (a + b);
        //Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] decryptedMessageHash = cipher.doFinal(digitalSignatureEncryption);

        boolean isCorrect = Arrays.equals(decryptedMessageHash, newMessageHash);
        System.out.println("Signature " + (isCorrect ? "correct" : "incorrect"));
        
        System.out.println("My String".startsWith("My"));
        System.out.println("My String".startsWith(""));
        
    }

}
