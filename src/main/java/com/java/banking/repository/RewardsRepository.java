package com.java.banking.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.java.banking.model.Customer;

public interface RewardsRepository extends CrudRepository<Customer,Long> {
    public Optional<Customer> findByCustomerId(Long customerId);
}