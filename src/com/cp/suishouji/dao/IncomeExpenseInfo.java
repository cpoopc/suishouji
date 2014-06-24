package com.cp.suishouji.dao;

public class IncomeExpenseInfo {
	public double income = 0;
	public double expense = 0;
	public double total = 0;
	public IncomeExpenseInfo(double income, double expense) {
		super();
		this.income = income;
		this.expense = expense;
		this.total = income - expense;
	}
	@Override
	public String toString() {
		return "IncomeExpenseInfo [income=" + income + ", expense=" + expense
				+ ", total=" + total + "]";
	}
	
}
