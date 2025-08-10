package com.sayem.accounts.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AccountsDto {
	
	@NotEmpty(message = "Acount Number can not be null or empty")
	private Long accountNumber;
	
	@NotEmpty(message = "Acount Type can not be null or empty")
	private String accountType;
	
	@NotEmpty(message = "Branch can not be null or empty")
	private String branchAddress;
	
	private CustomerDto customerDto;
	
}
