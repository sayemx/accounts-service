package com.sayem.accounts.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.sayem.accounts.constants.AccountsConstants;
import com.sayem.accounts.dto.AccountsDto;
import com.sayem.accounts.dto.CustomerDto;
import com.sayem.accounts.entity.Accounts;
import com.sayem.accounts.entity.Customer;
import com.sayem.accounts.exception.CustomerAlreadyExistsException;
import com.sayem.accounts.exception.ResourceNotFoundException;
import com.sayem.accounts.mapper.AccountsMapper;
import com.sayem.accounts.mapper.CustomerMapper;
import com.sayem.accounts.repository.AccountsRepository;
import com.sayem.accounts.repository.CustomerReposotiry;
import com.sayem.accounts.service.IAccountsService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService{
	
	private AccountsRepository accountsRepository;
	private CustomerReposotiry customerRepository;

	@Override
	public void createAccount(CustomerDto customerDto) {
		Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
                    +customerDto.getMobileNumber());
        }
        // customer.setCreatedAt(LocalDateTime.now());
        // customer.setCreatedBy("Anonymous");
        Customer savedCustomer = customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer));
	}
	
	private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        // newAccount.setCreatedAt(LocalDateTime.now());
        // newAccount.setCreatedBy("Anonymous");
        return newAccount;
    }

	@Override
	public AccountsDto fetchAccountDetails(String mobileNumber) {
		
		Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
				() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
		Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
				() -> new ResourceNotFoundException("Accounts", "mobileNumber", mobileNumber));
		
		AccountsDto accountsDto = AccountsMapper.mapToAccountsDto(accounts, new AccountsDto());
		CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
		accountsDto.setCustomerDto(customerDto);
		
		return accountsDto;
	}
	
	@Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if(accountsDto !=null ){
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            );
            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDto,customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return  isUpdated;
    }
	
	@Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }
	
	

}
