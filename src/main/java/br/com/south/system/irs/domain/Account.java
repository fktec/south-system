package br.com.south.system.irs.domain;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvNumber;

import br.com.south.system.irs.common.CsvModel;
import br.com.south.system.irs.common.helpers.CSVHelper;

public class Account implements CsvModel {

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
    private Boolean approved;
    
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

	public Boolean getApproved() {
		return approved;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

	@Override
	public String[] generateHeaders() {
		return new String[]{"agencia", "conta", "saldo", "status", "approved"};
	}

	@Override
	public String[] generateValues() {
		return new String[]{branch, number, CSVHelper.csvValue(balance), status, CSVHelper.csvValue(approved)};
	}

}
