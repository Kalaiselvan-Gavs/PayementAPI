package com.rabo.paymentinitiation.util;

import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaymentUtil {

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

    /**
     * Get Current Timestamp
     * @return
     */
    public static Timestamp getCurrentTimeStamp() {
    	return new Timestamp(System.currentTimeMillis());
    }
    
    /**
     * Format certificate string to create X509 certificate
     * @param signatureCertificate
     * @return
     */
    public static String formatSignatureCertificate(String signatureCertificate) {
    	
    	StringBuilder certificateBuilder = new StringBuilder();
		certificateBuilder.append(signatureCertificate);
		int beginCertificateIndex = certificateBuilder.indexOf(Constants.BEGIN_CERTIFICATE);
		certificateBuilder.replace(beginCertificateIndex, beginCertificateIndex + Constants.BEGIN_CERTIFICATE.length(), Constants.BEGIN_CERTIFICATE + "\r\n");
		int endCertificateIndex = certificateBuilder.indexOf(Constants.END_CERTIFICATE);
		certificateBuilder.replace(endCertificateIndex, endCertificateIndex + Constants.END_CERTIFICATE.length(), "\r\n" + Constants.END_CERTIFICATE);
		return certificateBuilder.toString();
		
    }
}
