# Payment Initiation Sandbox API

Payment Initiation is a REST service which will validate all the payment request details given by the customer.

The application takes Payment details as input and validates the following:

Check the request validation
Check whether the Amount limit exceeded.
Check the White listed certificates validation, If a CN start with `Sandbox-TPP`
Check the input signature validation.

Steps to run the application:

Import the project in Eclipse/STS [Maven Project]

Perform maven build - clean package.

Run the PaymentInitiationApplication.java file.

Hit the http://localhost:8443/swagger-ui.html URL in browser.

Expand the Payment-Initation-Controller operation and input the valid payment details.

Click on execute.
============================================================================
Unit testing result : UnitTestingResults.pdf 
============================================================================

# Validation Handling

White listed certificates validation => CustomFilter.java

Verify Singationature => CustomFilter.java

Request validation , GENERAL_ERROR => CustomExceptionHandler.java

Bean Level validations also present.

======================================================================

# New Certificate Creation Steps

1. Getting a KeyPair

keytool -genkeypair -alias senderKeyPair -keyalg RSA -keysize 2048 -dname "CN=Sandbox-TPP" -validity 365 -storetype PKCS12 -keystore sender_keystore.p12 -storepass payment
keytool -genkeypair -alias senderKeyPair -keyalg RSA -keysize 2048 -dname "CN=Non-TPP" -validity 365 -storetype PKCS12 -keystore sender_keystore.p12 -storepass payment

2.Loading the Private Key for Signing

keytool -exportcert -alias senderKeyPair -storetype PKCS12 -keystore sender_keystore.p12 -file sender_certificate.cer -rfc -storepass payment

3. Publishing the Public Key

keytool -importcert -alias receiverKeyPair -storetype PKCS12 -keystore receiver_keystore.p12 -file sender_certificate.cer -rfc -storepass payment

