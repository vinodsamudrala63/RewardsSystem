package com.java.banking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.banking.model.Customer;
import com.java.banking.model.MonthlyRewards;
import com.java.banking.model.Rewards;
import com.java.banking.model.Transaction;
import com.java.banking.repository.RewardsRepository;
import com.java.banking.repository.TransactionRepository;
import com.java.banking.util.RewardsDateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RewardsService {

	public static final int FIRSTREWARDLIMIT = 50;
	public static final int SECONDREWARDLIMIT = 100;
	public static final int PREVIOUSMONTHS = 3;
	

	@Autowired
	TransactionRepository transactionRepository;

	@Autowired
	RewardsRepository rewardsRepository;

	public Rewards getRewardsByCustomerIdPerMonth(Long customerId) {

		Rewards customerRewards = new Rewards();
		customerRewards.setCustomerId(customerId);
		Map<String, Long> map = getTotalRewardsPerMonth(customerId);
		List<MonthlyRewards> monthRewardsList = new ArrayList<>();
		map.forEach((monthYear, rewards) -> {
			MonthlyRewards monthRewards = new MonthlyRewards();
			monthRewards.setMonth_year(monthYear);
			monthRewards.setRewards(rewards);
			monthRewardsList.add(monthRewards);

			customerRewards.setTotalRewards(customerRewards.getTotalRewards() + rewards);
		});
		customerRewards.setMonthlyRewardsList(monthRewardsList);
		return customerRewards;
	}

	private Map<String, Long> getTotalRewardsPerMonth(Long customerId) {
		List<Transaction> transactions = transactionRepository.findAllByCustomerIdAndTransactionDatewithin(customerId,
				RewardsDateUtil.getFirstDateOfMonth(PREVIOUSMONTHS-1), RewardsDateUtil.getLastDateOfMonths(0));
		return transactions.stream()
				.collect(Collectors.groupingBy(tran -> RewardsDateUtil.getMonthYearFromTimeStamp(tran.getTransactionDate()),
						Collectors.summingLong(transaction -> calculateRewards(transaction))));

	}

	private Long calculateRewards(Transaction t) {
		if (t.getTransactionAmount() > FIRSTREWARDLIMIT && t.getTransactionAmount() <= SECONDREWARDLIMIT) {
			return Math.round(t.getTransactionAmount() - FIRSTREWARDLIMIT);
		} else if (t.getTransactionAmount() > SECONDREWARDLIMIT) {
			return Math.round(t.getTransactionAmount() - SECONDREWARDLIMIT) * 2
					+ (SECONDREWARDLIMIT - FIRSTREWARDLIMIT);
		} else
			return 0l;// 0L long type casting

	}

	
	public Optional<Customer> findByCustomerId(Long customerId) {
		return rewardsRepository.findByCustomerId(customerId);
	}
}
