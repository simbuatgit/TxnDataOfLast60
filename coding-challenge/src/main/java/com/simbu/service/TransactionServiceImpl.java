package com.simbu.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.simbu.dto.StatisticsDto;
import com.simbu.dto.TransactionDto;
import com.simbu.model.StatisticsModel;

@Component
public class TransactionServiceImpl implements TransactionService {

	private static final int TIMEOUT = 60000;
	private ConcurrentHashMap<Long, StatisticsModel> storeMap = new ConcurrentHashMap<Long, StatisticsModel>();

	public ResponseEntity<String> save(TransactionDto transactionDto) {
		try {

			Calendar givenCalender = convertToCalender(transactionDto);

			Calendar currentCalender = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

			long diff = currentCalender.getTime().getTime() - (givenCalender.getTime().getTime());

			if (diff > TIMEOUT) { // 60 sec
				return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
			} else if (diff < 0) { // future txn
				return new ResponseEntity<String>(HttpStatus.UNPROCESSABLE_ENTITY);
			}

			// cleaning old data
			Iterator<Long> it = storeMap.keySet().iterator();
			while (it.hasNext()) {
				Long next = it.next();

				long currentTime = currentCalender.getTime().getTime();
				if (currentTime - next > TIMEOUT) {
					storeMap.remove(next);

				}

			}
			Calendar calender;
			try {
				calender = convertToCalender(transactionDto);
			} catch (ParseException e) {
				return new ResponseEntity<String>(HttpStatus.UNPROCESSABLE_ENTITY);
			}
			long time = calender.getTime().getTime();
			BigDecimal amount = new BigDecimal(transactionDto.getAmount());
			if (storeMap.containsKey(time)) {
				StatisticsModel statisticsModelRetrived = storeMap.get(time);

				statisticsModelRetrived.addSum(amount);
				if (statisticsModelRetrived.getMax().compareTo(amount) < 0) {
					statisticsModelRetrived.setMax(amount);
				}
				if (statisticsModelRetrived.getMin().compareTo(amount) > 0) {
					statisticsModelRetrived.setMin(amount);
				}
				statisticsModelRetrived.incrementCount();
			} else {
				StatisticsModel statisticsModelAdd = new StatisticsModel();
				statisticsModelAdd.setSum(amount);
				statisticsModelAdd.setMin(amount);
				statisticsModelAdd.setMax(amount);
				statisticsModelAdd.incrementCount();
				storeMap.put(time, statisticsModelAdd);
			}
			// storeList.add(transactionDto);

		} catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.UNPROCESSABLE_ENTITY);

		}
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}

	private Calendar convertToCalender(TransactionDto transactionDto) throws ParseException {
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
		Calendar givenCalender = Calendar.getInstance();
		givenCalender.setTime(df1.parse(transactionDto.getTimestamp()));
		return givenCalender;
	}

	public ResponseEntity<StatisticsDto> getStatistics() {
		StatisticsDto statisticsDto = new StatisticsDto();
		Calendar currentCalender = Calendar.getInstance();

		BigDecimal sum = BigDecimal.ZERO;
		BigDecimal max = null;
		BigDecimal min = null;
		int count = 0;
		// cleaning old data
		Iterator<Long> it = storeMap.keySet().iterator();
		while (it.hasNext()) {
			Long next = it.next();
			long currentTime = currentCalender.getTime().getTime();
			if (currentTime - next > TIMEOUT) {
				storeMap.remove(next);

			} else {
				StatisticsModel statisticsModelRetrived = storeMap.get(next);
				sum = sum.add(statisticsModelRetrived.getSum());
				if (max == null || statisticsModelRetrived.getMax().compareTo(max) > 0) {
					max = statisticsModelRetrived.getMax();
				}
				if (min == null || statisticsModelRetrived.getMin().compareTo(min) < 0) {
					min = statisticsModelRetrived.getMin();
				}
				count = count + statisticsModelRetrived.getCount();
			}

		}

		sum = sum == null ? BigDecimal.ZERO : sum;
		max = max == null ? BigDecimal.ZERO : max;
		min = min == null ? BigDecimal.ZERO : min;

		sum = sum.setScale(2, RoundingMode.HALF_UP);
		max = max.setScale(2, RoundingMode.HALF_UP);
		min = min.setScale(2, RoundingMode.HALF_UP);

		if (count > 0)
			statisticsDto.setAvg(sum.divide(new BigDecimal(count), 2, RoundingMode.HALF_UP).toPlainString());
		else
			statisticsDto.setAvg(sum.toPlainString());

		statisticsDto.setSum(sum.toPlainString());
		statisticsDto.setMax(max.toPlainString());
		statisticsDto.setMin(min.toPlainString());
		statisticsDto.setCount(count);
		return ResponseEntity.ok(statisticsDto);

	}

	public ResponseEntity<String> delete() {
		storeMap = new ConcurrentHashMap<Long, StatisticsModel>();
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}

}
