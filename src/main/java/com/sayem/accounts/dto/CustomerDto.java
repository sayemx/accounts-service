package com.sayem.accounts.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CustomerDto {
	
	@NotEmpty(message = "Name can not be null or empty")
	private String name;
	
	@NotEmpty(message = "Email can not be null or empty")
	@Email(message = "Emaol address numst be a valid value")
	private String email;
	
	@Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
	private String mobileNumber;
	
	private AccountsDto accountsDto;
}
