package com.sayem.accounts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class Accounts extends BaseEntity {
	
	@Column(name = "customer_id")
	private Long customerId;
	
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY) // commented bez we want to generate this PK value manually
	@Column(name = "account_Number") // optional as column name is same
	private Long accountNumber;
	
	
	@Column(name = "account_type")
	private String accountType;
	
	@Column(name = "branch_address")
	private String branchAddress;
	
}
