package com.sayem.accounts.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sayem.accounts.entity.Customer;

@Repository
public interface CustomerReposotiry extends JpaRepository<Customer, Long> {

	Optional<Customer> findByMobileNumber(String mobileNumber);

}
