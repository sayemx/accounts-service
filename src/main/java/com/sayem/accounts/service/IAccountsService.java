package com.sayem.accounts.service;

import com.sayem.accounts.dto.AccountsDto;
import com.sayem.accounts.dto.CustomerDto;


public interface IAccountsService {
	
	/**
	 * 
	 * @param customerDto
	 */
	void createAccount(CustomerDto customerDto);
	
	AccountsDto fetchAccountDetails(String mobileNumber);
	
	boolean updateAccount(CustomerDto customerDto);

	boolean deleteAccount(String mobileNumber);
	
}
