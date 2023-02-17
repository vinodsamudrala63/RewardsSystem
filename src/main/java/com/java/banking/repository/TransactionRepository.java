
package com.java.banking.repository;


import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.java.banking.model.Transaction;


@Repository
@Transactional
public interface TransactionRepository extends CrudRepository<Transaction,Long> {
    public List<Transaction> findAllByCustomerId(Long customerId);

    @Query(value = "select * from Transaction t where t.CUSTOMER_ID = ?1 and t.TRANSACTION_DATE >= ?2 and t.TRANSACTION_DATE <= ?3 ",
    		nativeQuery = true)
    public List<Transaction> findAllByCustomerIdAndTransactionDatewithin(Long customerId, Timestamp from,Timestamp to);
    
    
}