package com.db.awmd.challenge.domain;

import java.math.BigDecimal;

@Data
public class AmountTransfer {

	
	  @NotNull
	  @NotEmpty
	  private String toAccountId;
	  private String fromAccountId;
	  @NotNull
	  @Min(value = 1, message = "Transfer amount should be greater than zero")
	  private BigDecimal transferAmount;

	public BigDecimal getTransferAmount() {
		return transferAmount;
	}

	public void setTransferAmount(BigDecimal transferAmount) {
		this.transferAmount = transferAmount;
	}

	public String getToAccountId() {
		return toAccountId;
	}

	public String getFromAccountId() {
		return fromAccountId;
	}

	public void setToAccountId(String toAccountId) {
		this.toAccountId = toAccountId;
	}

	public void setFromAccountId(String fromAccountId) {
		this.fromAccountId = fromAccountId;
	}

	
	  

	  
}
