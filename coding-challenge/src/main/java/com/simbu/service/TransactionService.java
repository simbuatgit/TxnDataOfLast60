package com.simbu.service;

import org.springframework.http.ResponseEntity;

import com.simbu.dto.StatisticsDto;
import com.simbu.dto.TransactionDto;

public interface TransactionService {

	ResponseEntity<String> save(TransactionDto transactionDto);

	ResponseEntity<StatisticsDto> getStatistics();

	ResponseEntity<String> delete();
}
