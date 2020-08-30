package com.rabo.paymentinitiation.util;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.util.ResourceUtils;

public class PaymentUtil {

	

    private static final String STORE_TYPE = "PKCS12";
    private static char[] PASSWORD;
    private static final String SENDER_KEYSTORE = "classpath:ssl/sender_keystore.p12";
    private static final String SENDER_ALIAS = "senderKeyPair";

    private static final String RECEIVER_KEYSTORE = "classpath:ssl/receiver_keystore.p12";
    private static final String RECEIVER_ALIAS = "receiverKeyPair";

    private PaymentUtil() {
		
	}

    /**
     * Sum all the digits & omitted letters.
     */
    public static int sumOfDigits(String input) {

        Pattern pattern = Pattern.compile("[\\d]");
        Matcher matcher = pattern.matcher(input);
        int sum = 0;
        while(matcher.find()) {
            sum += Integer.parseInt(matcher.group());
        }
        return sum;
    }

    public static PrivateKey getPrivateKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(STORE_TYPE);
        keyStore.load(new FileInputStream(ResourceUtils.getFile(SENDER_KEYSTORE)), PASSWORD);
        return (PrivateKey) keyStore.getKey(SENDER_ALIAS, PASSWORD);
    }

    public static PublicKey getPublicKey() throws Exception {
    	KeyStore keyStore = KeyStore.getInstance(STORE_TYPE);
        keyStore.load(new FileInputStream(ResourceUtils.getFile(RECEIVER_KEYSTORE)), PASSWORD);
        Certificate certificate = keyStore.getCertificate(RECEIVER_ALIAS);
        return certificate.getPublicKey();
    }

    public static void setKeyStorePassword(String keyStorePassword) {
		PASSWORD = keyStorePassword.toCharArray();
	}
    
    public static Timestamp getCurrentTimeStamp() {
    	return new Timestamp(System.currentTimeMillis());
    }
}
