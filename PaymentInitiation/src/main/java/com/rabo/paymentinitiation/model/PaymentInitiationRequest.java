package com.rabo.paymentinitiation.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class PaymentInitiationRequest {

	@NotEmpty(message = "Debtor IBAN is required")
	@Pattern(regexp = "[A-Z]{2}[0-9]{2}[a-zA-Z0-9]{1,30}", message = "Debtor IBAN must be a valid number")
	private String debtorIBAN;

	@NotEmpty(message = "Creditor IBAN is required")
	@Pattern(regexp = "[A-Z]{2}[0-9]{2}[a-zA-Z0-9]{1,30}", message = "Creditor IBAN must be a valid number")
	private String creditorIBAN;

	@NotEmpty(message = "Amount is required")
	@Pattern(regexp = "-?[0-9]+(\\\\.[0-9]{1,3})?}", message = "The amount must be a valid number")
	private String amount;

	@NotEmpty(message = "End to End id is required")
	private String endToEndId;

	public String getCreditorIBAN() {
		return creditorIBAN;
	}

	public void setCreditorIBAN(String creditorIBAN) {
		this.creditorIBAN = creditorIBAN;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getEndToEndId() {
		return endToEndId;
	}

	public void setEndToEndId(String endToEndId) {
		this.endToEndId = endToEndId;
	}

	public String getDebtorIBAN() {
		return debtorIBAN;
	}

	public void setDebtorIBAN(String debtorIBAN) {
		this.debtorIBAN = debtorIBAN;
	}

	@Override
	public String toString() {
		return "PaymentInitiationRequest [debtorIBAN=" + debtorIBAN + ", creditorIBAN=" + creditorIBAN + ", amount="
				+ amount + ", endToEndId=" + endToEndId + "]";
	}

}
