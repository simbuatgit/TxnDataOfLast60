package com.simbu.model;

import java.math.BigDecimal;

public class StatisticsModel {

	private BigDecimal sum;

	private BigDecimal avg;

	private BigDecimal max;

	private BigDecimal min;

	private int count;

	public BigDecimal getSum() {
		return sum;
	}

	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}

	public BigDecimal getAvg() {
		return avg;
	}

	public void setAvg(BigDecimal avg) {
		this.avg = avg;
	}

	public BigDecimal getMax() {
		return max;
	}

	public void setMax(BigDecimal max) {
		this.max = max;
	}

	public BigDecimal getMin() {
		return min;
	}

	public void setMin(BigDecimal min) {
		this.min = min;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void addSum(BigDecimal sum) {
		this.sum = this.sum.add(sum);
	}

	public void incrementCount() {
		this.count++;
	}

}
