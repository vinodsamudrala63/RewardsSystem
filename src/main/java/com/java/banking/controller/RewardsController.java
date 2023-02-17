package com.java.banking.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.banking.exceptions.CustomerNotFoundException;
import com.java.banking.model.Customer;
import com.java.banking.model.Rewards;
import com.java.banking.service.RewardsService;

@RestController
@RequestMapping("/rewardsprogram")
public class RewardsController {

 	@Autowired
    RewardsService rewardsService;

    @GetMapping(value = "customerid/{customerId}/rewards",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Rewards> getRewardsByCustomerId(@PathVariable("customerId") Long customerId){
    	Optional<Customer> customerOptional = rewardsService.findByCustomerId(customerId);
        if(!customerOptional.isPresent())
        {
        	throw new CustomerNotFoundException();
        }
        Rewards customerRewards = rewardsService.getRewardsByCustomerIdPerMonth(customerId);
        return new ResponseEntity<>(customerRewards,HttpStatus.OK);
    }
    
   
    
}

