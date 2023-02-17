package com.java.banking.model;

import java.util.List;

public class Rewards {
    private long customerId;
    private long totalRewards;
    private List<MonthlyRewards> monthRewardsList;
    

    public List<MonthlyRewards> getMonthlyRewardsList() {
		return monthRewardsList;
	}

	public void setMonthlyRewardsList(List<MonthlyRewards> monthRewardsList) {
		this.monthRewardsList = monthRewardsList;
	}

	public long getCustomerId() {
        return customerId;
    }

	public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getTotalRewards() {
        return totalRewards;
    }

    public void setTotalRewards(long totalRewards) {
        this.totalRewards = totalRewards;
    }
}