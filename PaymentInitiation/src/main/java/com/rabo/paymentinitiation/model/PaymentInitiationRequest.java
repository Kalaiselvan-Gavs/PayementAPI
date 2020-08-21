package com.rabo.paymentinitiation.model;

import javax.validation.constraints.NotEmpty;

public class PaymentInitiationRequest {

	@NotEmpty(message = "Debtor IBAN is required")
	private String debtorIBAN;

	@NotEmpty(message = "Creditor IBAN is required")
	private String creditorIBAN;

	@NotEmpty(message = "Amount is required")
	private String amount;

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
