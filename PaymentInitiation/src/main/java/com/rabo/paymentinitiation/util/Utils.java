package com.rabo.paymentinitiation.util;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Arrays;

public class Utils {

    private static final String STORE_TYPE = "PKCS12";
    private static final char[] PASSWORD = "payment".toCharArray();
    private static final String SENDER_KEYSTORE = "E:\\Project\\Testing\\Payment\\PaymentInitiation\\src\\main\\resources\\ssl\\sender_keystore.p12";
    private static final String SENDER_ALIAS = "senderKeyPair";

    public static final String SIGNING_ALGORITHM = "SHA256withRSA";

    private static final String RECEIVER_KEYSTORE = "E:\\Project\\Testing\\Payment\\PaymentInitiation\\src\\main\\resources\\ssl\\receiver_keystore.p12";
    private static final String RECEIVER_ALIAS = "receiverKeyPair";

    public static PrivateKey getPrivateKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(STORE_TYPE);
        keyStore.load(new FileInputStream(SENDER_KEYSTORE), PASSWORD);
        return (PrivateKey) keyStore.getKey(SENDER_ALIAS, PASSWORD);
    }

    public static PublicKey getPublicKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(STORE_TYPE);
        keyStore.load(new FileInputStream(RECEIVER_KEYSTORE), PASSWORD);
        Certificate certificate = keyStore.getCertificate(RECEIVER_ALIAS);
        return certificate.getPublicKey();
    }

    /**
     * Verify the signature
     */
    public static boolean verifySignature(String xRequestId, String requestBody) throws Exception {
		/*KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048); // KeySize
		KeyPair keyPair = keyPairGenerator.generateKeyPair();

		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();

		byte[] data = (xRequestId + requestBody).getBytes();
		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initSign(privateKey);
		signature.update(data);
		byte[] signedData = signature.sign();

		signature.initVerify(publicKey);
		signature.update(data);

		return signature.verify(signedData);*/
        PublicKey publicKey = getPublicKey();
        PrivateKey privateKey = getPrivateKey();
System.out.println((xRequestId + requestBody));
        byte[] messageBytes = (xRequestId + requestBody).getBytes();//Files.readAllBytes(Paths.get("src/test/resources/digitalsignature/message.txt"));

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
        return isCorrect;
    }

    public static void main(String[] args) throws Exception {
        verifySignature("29318e25-cebd-498c-888a-f77672f66449","{\"debtorIBAN\":\"NL02RABO7134384551\",\"creditorIBAN\":\"NL94ABNA1008270121\",\"amount\":\"1.00\",\"endToEndId\":\"1\"}");
    }
}
