package com.java.banking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.banking.model.Customer;
import com.java.banking.model.MonthlyRewards;
import com.java.banking.model.Rewards;
import com.java.banking.model.Transaction;
import com.java.banking.repository.RewardsRepository;
import com.java.banking.repository.TransactionRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import static java.time.temporal.TemporalAdjusters.*;

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
				getFirstDateOfMonth(PREVIOUSMONTHS-1), getLastDateOfMonths(0));
		return transactions.stream()
				.collect(Collectors.groupingBy(tran -> getMonthYearFromTimeStamp(tran.getTransactionDate()),
						Collectors.summingLong(transaction -> calculateRewards(transaction))));

	}

	private Long calculateRewards(Transaction t) {
		if (t.getTransactionAmount() > FIRSTREWARDLIMIT && t.getTransactionAmount() <= SECONDREWARDLIMIT) {
			return Math.round(t.getTransactionAmount() - FIRSTREWARDLIMIT);
		} else if (t.getTransactionAmount() > SECONDREWARDLIMIT) {
			return Math.round(t.getTransactionAmount() - SECONDREWARDLIMIT) * 2
					+ (SECONDREWARDLIMIT - FIRSTREWARDLIMIT);
		} else
			return 0l;

	}

	public String getMonthYearFromTimeStamp(Timestamp timestamp) {
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.setTime((Date) timestamp.clone());
		return mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + "-"
				+ mCalendar.get(Calendar.YEAR);
	}

	public String getMonthNameAndYear(int previousMonth) {
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.add(Calendar.MONTH, -previousMonth);
		return mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + "-"
				+ mCalendar.get(Calendar.YEAR);
	}

	public Timestamp getFirstDateOfMonth(int previousMonth) {
		LocalDate localDate = LocalDate.now();
		LocalDateTime startOfDay = LocalDateTime.of(localDate, LocalTime.MIDNIGHT);
		return Timestamp.valueOf(startOfDay.minusMonths(previousMonth).with(firstDayOfMonth()));
	}

	public Timestamp getLastDateOfMonths(int previousMonth) {
		LocalDate localDate = LocalDate.now();
		LocalDateTime endOfDay = LocalDateTime.of(localDate, LocalTime.MAX);
		return Timestamp.valueOf(endOfDay.minusMonths(previousMonth).with(lastDayOfMonth()));
	}

	public Optional<Customer> findByCustomerId(Long customerId) {
		return rewardsRepository.findByCustomerId(customerId);
	}
}
