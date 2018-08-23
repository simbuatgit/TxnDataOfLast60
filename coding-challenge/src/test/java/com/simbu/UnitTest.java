package com.simbu;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.simbu.controller.TransactionController;
import com.simbu.dto.StatisticsDto;
import com.simbu.dto.TransactionDto;
import com.simbu.service.TransactionServiceImpl;

public class UnitTest {

	@Test
	public void ValidateInvalidJson() {

		TransactionDto txnDto = new TransactionDto();
		txnDto.setAmount("23.12");
	      TransactionController transactionController = new TransactionController();
	      ResponseEntity<String> response= transactionController.saveTransaction(txnDto);
	      
	      assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	public void ValidateFutureDate() {

		TransactionDto txnDto = new TransactionDto();
		txnDto.setAmount("23.12");
		txnDto.setTimestamp("2025-08-06T09:48:56.243Z");
		TransactionServiceImpl transactionServiceImpl = new TransactionServiceImpl();
	      ResponseEntity<String> response= transactionServiceImpl.save(txnDto);
	      
	      assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
	}
	
	@Test
	public void ValidatePastDate() {

		TransactionDto txnDto = new TransactionDto();
		txnDto.setAmount("23.12");
		txnDto.setTimestamp("2010-08-06T09:48:56.243Z");
		TransactionServiceImpl transactionServiceImpl = new TransactionServiceImpl();
	      ResponseEntity<String> response= transactionServiceImpl.save(txnDto);
	      
	      assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}
	@Test
	public void ValidateDelete() {

		TransactionDto txnDto = new TransactionDto();
		TransactionServiceImpl transactionServiceImpl = new TransactionServiceImpl();
	      ResponseEntity<String> response= transactionServiceImpl.delete();
	      
	      assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}
	@Test
	public void ValidateGet() {

		TransactionServiceImpl transactionServiceImpl = new TransactionServiceImpl();
	      ResponseEntity<StatisticsDto> response= transactionServiceImpl.getStatistics();
	      
	      assertEquals(HttpStatus.OK, response.getStatusCode());
	}
}
