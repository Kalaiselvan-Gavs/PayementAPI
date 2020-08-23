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
