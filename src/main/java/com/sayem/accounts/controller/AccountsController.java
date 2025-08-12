package com.sayem.accounts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sayem.accounts.constants.AccountsConstants;
import com.sayem.accounts.dto.AccountsContactInfoDto;
import com.sayem.accounts.dto.AccountsDto;
import com.sayem.accounts.dto.CustomerDto;
import com.sayem.accounts.dto.ResponseDto;
import com.sayem.accounts.service.IAccountsService;

import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(
		name = "CRUD REST APIs for Accounts Controller",
		description = "CREATE, UPDATE, FETCH AND DELETE"
)
@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
//@AllArgsConstructor
@Validated
@Slf4j
public class AccountsController {
	
	private IAccountsService accountsService;
	
	@Value("${build.version}")
	private String buildVersion;
	
	@Autowired
	private Environment environment;
	
	@Autowired
	AccountsContactInfoDto accountsContactInfoDto;
	
	public AccountsController(IAccountsService accountsService) {
		this.accountsService = accountsService;
	}
	
	@Operation(
			summary = "Create Account"
	)
	@ApiResponse(
			responseCode = "201",
			description = "HTTP Status CREATED"
	)
	@PostMapping("/create")
	public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto){
		
		accountsService.createAccount(customerDto);
		
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
	}
	
	@GetMapping("/fetch")
	public ResponseEntity<AccountsDto> fetchAcount(@RequestParam 
													@Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
													String mobileNumber){
		
		AccountsDto accountsDto = accountsService.fetchAccountDetails(mobileNumber);
		
		return new ResponseEntity<AccountsDto>(accountsDto, HttpStatus.OK);
	}
	
	@PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody CustomerDto customerDto) {
        boolean isUpdated = accountsService.updateAccount(customerDto);
        if(isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_UPDATE));
        }
    }
	
	@DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccountDetails(@RequestParam 
    														@Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
												    		String mobileNumber) {
        boolean isDeleted = accountsService.deleteAccount(mobileNumber);
        if(isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_DELETE));
        }
    }
	
	@Retry(name = "getBuildInfo", fallbackMethod = "fallbackmethod")
	@GetMapping("/build-info")
	public ResponseEntity<String> getBuildInfo(){
		log.debug("Fallback method invoked");
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(buildVersion);
	}
	
	public ResponseEntity<String> fallbackmethod(Throwable t) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body("0.9"); 
	}
	
	@GetMapping("/java-version")
	public ResponseEntity<String> getJavaVersion(){
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(environment.getProperty("JAVA_HOME"));
	}
	
	@GetMapping("/contact-info")
	public ResponseEntity<AccountsContactInfoDto> getContactinfo(){
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(accountsContactInfoDto);
	}
	
}
