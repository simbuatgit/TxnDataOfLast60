package com.simbu.dto;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

@Validated
public class TransactionDto {
	 @JsonProperty("amount")
    private String amount;
	 @JsonProperty("timestamp")
    private String timestamp;
	 
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}


    
}
