package com.java.banking.model;

public class MonthlyRewards {
	String month_year;
	long rewards;
	public String getMonth_year() {
		return month_year;
	}
	public void setMonth_year(String month_name_year) {
		this.month_year = month_name_year;
	}
	public long getRewards() {
		return rewards;
	}
	public void setRewards(long rewards) {
		this.rewards = rewards;
	}

	
}
