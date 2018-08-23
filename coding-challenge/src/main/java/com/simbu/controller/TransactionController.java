package com.simbu.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.simbu.dto.StatisticsDto;
import com.simbu.dto.TransactionDto;
import com.simbu.service.TransactionService;

@Controller
public class TransactionController {

	@Autowired
	TransactionService transactionService;
	

	@RequestMapping(value = "/transactions", produces = { "application/json" }, method = RequestMethod.POST)
	public ResponseEntity<String> saveTransaction(@RequestBody TransactionDto transactionDto) {


			if (transactionDto.getAmount() == null || transactionDto.getTimestamp() == null) {
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			}
			return transactionService.save(transactionDto);
			
		// 200
		// 204 more than 60.204 No Content //HttpStatus.NO_CONTENT
		// 400 – if the JSON is invalid //HttpStatus.BAD_REQUEST
		// 422 – if any of the fields are not parsable or the transaction date is in the
		// future//HttpStatus.UNPROCESSABLE_ENTITY
	}

	@RequestMapping(value = "/statistics", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<StatisticsDto> getStatistics() {
		return transactionService.getStatistics();
	}
	
	@RequestMapping(value = "/transactions", produces = { "application/json" }, method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteTransaction() {
		return transactionService.delete();
	}
}
