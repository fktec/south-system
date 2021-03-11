package br.com.south.system.irs.domain;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvNumber;

public class Account {

	@CsvBindByPosition(position = 0)
    private String branch;
	
    @CsvBindByPosition(position = 1)
    private String number;
    
    @CsvNumber(value = "###,00")
    @CsvBindByPosition(position = 2)
    private Double balance;
    
    @CsvBindByPosition(position = 3)
    private String status;
    
    @CsvBindByPosition(position = 4)
    private boolean approved;
    
    public Account() {}

	public Account(String branch, String account, Double balance, String status) {
		this.branch = branch;
		this.number = account;
		this.balance = balance;
		this.status = status;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getNumber() {
		return number;
	}
	
	public String getOnlyNumber() {
		if (number != null && !number.isEmpty()) {
			return number.replace("-", "");
		}
		return null;
	}

	public void setNumber(String account) {
		this.number = account;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

}
